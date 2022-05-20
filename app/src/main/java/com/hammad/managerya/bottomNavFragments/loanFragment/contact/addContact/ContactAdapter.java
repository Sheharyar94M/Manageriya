package com.hammad.managerya.bottomNavFragments.loanFragment.contact.addContact;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hammad.managerya.R;
import com.hammad.managerya.bottomNavFragments.loanFragment.contact.ConsumerDetailActivity;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    Context context;
    List<ContactModel> contactModelList;
    String searchedContactName;

    public ContactAdapter(Context context, List<ContactModel> contactModelList) {
        this.context = context;
        this.contactModelList = contactModelList;
    }

    @NonNull
    @Override
    public ContactAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.layout_contact_recycler,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.MyViewHolder holder, int position) {
        ContactModel item = contactModelList.get(position);

        //for filtering name
        String content= item.getContactName().toLowerCase();

        if (searchedContactName != null && !searchedContactName.isEmpty())
        {
            String htmlText = content.replace(searchedContactName, "<font color='#ff0000'>" + searchedContactName + "</font>");
            holder.textViewName.setText(Html.fromHtml(htmlText));
        }
        else
        {
            holder.textViewName.setText(item.getContactName());
        }

        holder.textViewLetters.setText(item.getContactLetters());

        holder.textViewPhoneNo.setText(item.getPhoneNo());

        //contact click listener
        holder.constraintLayout.setOnClickListener( view -> {

            Intent contactIntent=new Intent(context, ConsumerDetailActivity.class);
            contactIntent.putExtra("conName",item.getContactName());
            contactIntent.putExtra("conPhone",item.getPhoneNo());
            contactIntent.putExtra("conLetters",item.getContactLetters());
            context.startActivity(contactIntent);

            //Toast.makeText(context, "Name: "+item.getContactName()+"\nPhone NO: "+item.getPhoneNo(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return contactModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView textViewLetters,textViewName,textViewPhoneNo,textViewContactStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout=itemView.findViewById(R.id.constraint_contact);
            textViewLetters=itemView.findViewById(R.id.txt_contact_letters);
            textViewName=itemView.findViewById(R.id.txt_contact_name);
            textViewPhoneNo=itemView.findViewById(R.id.txt_contact_no);
            textViewContactStatus=itemView.findViewById(R.id.txt_contact_status);
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

}
