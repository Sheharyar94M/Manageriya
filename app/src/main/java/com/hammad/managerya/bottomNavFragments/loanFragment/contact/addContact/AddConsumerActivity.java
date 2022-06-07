package com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.hammad.managerya.R;
import com.hammad.managerya.RoomDB.RoomDBHelper;
import com.hammad.managerya.bottomNavFragments.loanFragment.DB.LoanEntity;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.ConsumerDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddConsumerActivity extends AppCompatActivity implements ContactAdapter.OnContactListInterface {

    List<ContactModel> contactModelList = new ArrayList<>();

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextInputEditText editTextSearch;
    private ContactAdapter adapter;
    private ContactModel contactModel;

    //DB instance
    private RoomDBHelper database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumer);

        //database instance initialization
        database = RoomDBHelper.getInstance(this);

        //initializing toolbar
        setToolbar();

        //initializing views
        recyclerView = findViewById(R.id.recycler_view_contacts);
        editTextSearch=findViewById(R.id.edit_text_search);

        //getting the contact list
        getContactList();

        //searching contact names
        searchContactName();
    }

    private void setToolbar() {

        toolbar = findViewById(R.id.toolbar_add_cons);

        //setting the toolbar as action bar
        setSupportActionBar(toolbar);

        //setting the back button on toolbar
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //for handling the back pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getContactList() {
        long contactId;
        String name, phoneNo;

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

        ContentResolver contentResolver = getContentResolver();

        Cursor cursor = contentResolver.query(uri, null, null, null, sortOrder);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {

                contactModel=new ContactModel();

                contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contactModel.setContactId(contactId);
                contactModel.setContactName(name);
                contactModel.setPhoneNo(phoneNo);
                contactModel.setContactLetters(getContactLetters(name));

                //for removing the duplicate contacts
                removeDuplicateContacts();

            }

            //setting the recyclerview
            setRecyclerView();
        }
    }

    private String getContactLetters(String name) {
        String letters = "";

        for (int i = 0; i < name.length(); i++) {
            letters = Character.toString(name.charAt(0));

            if (Character.isWhitespace(name.charAt(i)) || Character.isSpaceChar(name.charAt(i))) {
                letters = letters.concat(Character.toString(name.charAt(i + 1)));
                break;
            }
        }

        return letters;
    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ContactAdapter(this, contactModelList, this);
        recyclerView.setAdapter(adapter);
    }

    //for searching the contact name
    private void searchContactName() {

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {

                //calling the adapter setSearchedText
                adapter.setSearchText(editable.toString());

                //calling the filter function
                filter(editable.toString());
            }
        });
    }

    private void filter(String text) {

        List<ContactModel> searchContactName=new ArrayList<>();

        for(ContactModel modelItem:contactModelList){

            if(modelItem.getContactName().toLowerCase().contains(text.toLowerCase())){

                searchContactName.add(modelItem);
            }
        }

        adapter.filteredNote(searchContactName);
    }

    private void removeDuplicateContacts() {
        int flag = 0;

        if (contactModelList.size() == 0) {
            contactModelList.add(contactModel);
        }

        for (int i = 0; i < contactModelList.size(); i++) {

            if (contactModelList.get(i).getContactId() == contactModel.getContactId()) {
                flag = 0;
                break;
            } else {
                flag = 1;
            }
        }

        if (flag == 1) {
            contactModelList.add(contactModel);
        }
    }

    //Contact Adapter interface listener
    @Override
    public void onContactClick(String phoneNo, String contactName, String contactLetters, long contactId) {

        //saving the contact into database
        database.loanDao().addContact(new LoanEntity(phoneNo,contactName,contactLetters,contactId));

        //forwarding the added details to next activity
        Intent contactIntent=new Intent(this, ConsumerDetailActivity.class);
        contactIntent.putExtra("conName",contactName);
        contactIntent.putExtra("conPhone",phoneNo);
        contactIntent.putExtra("conLetters",contactLetters);
        startActivity(contactIntent);
        finish();
    }
}