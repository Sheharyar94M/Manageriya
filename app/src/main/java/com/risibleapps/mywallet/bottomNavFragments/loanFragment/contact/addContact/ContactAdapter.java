package com.risibleapps.mywallet.bottomNavFragments.loanFragment.contact.addContact;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.risibleapps.mywallet.R;
import com.risibleapps.mywallet.RoomDB.RoomDBHelper;
import com.risibleapps.mywallet.bottomNavFragments.loanFragment.DB.LoanEntity;

import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    List<ContactModel> contactModelList;
    String searchedContactName;

    //click listener interface
    OnContactListInterface mOnContactListInterface;

    //hashmap for identifying added contact
    HashMap<Long,String> hashMap=new HashMap<>();

    RoomDBHelper database;

    //list of added contacts
    List<LoanEntity> addedContactsList;

    public ContactAdapter(Context context, List<ContactModel> contactModelList,OnContactListInterface onContactListInterface) {
        this.context = context;
        this.contactModelList = contactModelList;
        this.mOnContactListInterface = onContactListInterface;

        database = RoomDBHelper.getInstance(context);

        addedContactsList = database.loanDao().getAddedContactList();
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_contact_recycler,parent,false);
        return new MyViewHolder(view,mOnContactListInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {

        //contact model list item
        ContactModel item = contactModelList.get(position);

        //retrieved added contact list is added to hashmap
        for (LoanEntity loanItem : addedContactsList)
        {
            hashMap.put(loanItem.getContactId(),loanItem.getContactName());
        }

        //searching if a contact is already added or not
        if(hashMap.containsKey(item.getContactId())){

            holder.textViewContactStatus.setText("Added");

            //removes the drawable from any view
            holder.textViewContactStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            //contact click listener
            holder.constraintLayout.setOnClickListener( view -> {
                Toast.makeText(context, "Contact Already Added", Toast.LENGTH_SHORT).show();
            });
        }
        else if(!(hashMap.containsKey(item.getContactId()))){

            holder.textViewContactStatus.setText("Add");

            //setting the left drawable to textview
            holder.textViewContactStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_add_small,0,0,0);

            //setting the tint to drawable
            holder.textViewContactStatus.getCompoundDrawables()[0].setTint(context.getResources().getColor(R.color.colorPrimary));

            //contact click listener
            holder.constraintLayout.setOnClickListener( view -> {
                //interface click listener
                mOnContactListInterface.onContactClick(item.getPhoneNo(),item.getContactName(),item.getContactLetters(),item.getContactId());
            });
        }

        //for filtering name
        String content= item.getContactName().toLowerCase();

        if (searchedContactName != null && !searchedContactName.isEmpty()) {

            String htmlText = content.replace(searchedContactName, "<font color='#ff0000'>" + searchedContactName + "</font>");
            holder.textViewName.setText(Html.fromHtml(htmlText));
        }
        else {
            holder.textViewName.setText(item.getContactName());
        }

        //setting the data from contact model list to views
        holder.textViewLetters.setText(item.getContactLetters());

        holder.textViewPhoneNo.setText(String.valueOf(item.getPhoneNo()));

       /* //contact click listener
        holder.constraintLayout.setOnClickListener( view -> {
            //interface click listener
            mOnContactListInterface.onContactClick(item.getPhoneNo(),item.getContactName(),item.getContactLetters(),item.getContactId());
        });*/

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView textViewLetters,textViewName,textViewPhoneNo,textViewContactStatus;
        OnContactListInterface onContactListInterface;

        public MyViewHolder(@NonNull View itemView,OnContactListInterface onContactListInterface) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraint_contact);
            textViewLetters=itemView.findViewById(R.id.txt_contact_letters);
            textViewName=itemView.findViewById(R.id.txt_contact_name);
            textViewPhoneNo=itemView.findViewById(R.id.txt_contact_no);
            textViewContactStatus=itemView.findViewById(R.id.txt_contact_status);

            //interface initialization
            this.onContactListInterface = onContactListInterface;
        }
    }

    //for searching contact
    public void filteredNote(List<ContactModel> contactModel) {
        contactModelList = contactModel;
        notifyDataSetChanged();
    }

    public String setSearchText(String text) {
        return this.searchedContactName = text;
    }

    //Adapter interface listener
    public interface OnContactListInterface{
        void onContactClick(String phoneNo,String contactName,String contactLetters,long contactId);
    }

}
