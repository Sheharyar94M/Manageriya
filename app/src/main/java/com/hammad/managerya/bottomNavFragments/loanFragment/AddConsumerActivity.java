package com.hammad.managerya.bottomNavFragments.loanFragment;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.hammad.managerya.R;

import java.util.ArrayList;
import java.util.List;

public class AddConsumerActivity extends AppCompatActivity implements ContactAdapter.OnContactInterfaceListener {

    List<ContactModel> contactModelList = new ArrayList<>();
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextInputEditText editTextSearch;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_consumer);

        setToolbar();

        //initializing views
        recyclerView = findViewById(R.id.recycler_view_contacts);
        editTextSearch=findViewById(R.id.edit_text_search);

        //getting the contact list
        getContactList();

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
                contactId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                phoneNo = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));

                contactModelList.add(new ContactModel(contactId, name, phoneNo, getContactLetters(name)));
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

    //Contact Adapter click listener
    @Override
    public void onContactClick(int position) {
        Toast.makeText(this, "Name: " + contactModelList.get(position).getContactName(), Toast.LENGTH_SHORT).show();
    }

}