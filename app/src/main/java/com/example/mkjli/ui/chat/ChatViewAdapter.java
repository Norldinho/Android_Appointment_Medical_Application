package com.example.mkjli.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;

import java.util.ArrayList;

public class ChatViewAdapter extends RecyclerView.Adapter<ChatViewAdapter.ViewHolder> {

    private ArrayList<ChatView> chatViewArrayList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String idMedcin);
    }

    public ChatViewAdapter(ArrayList<ChatView> chatViewArrayList, Context context, OnItemClickListener listener) {
        this.chatViewArrayList = chatViewArrayList;
        this.context = context;
        this.listener = listener;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(parent.getContext()).inflate(R.layout.pour_chat, parent, false);
        context = parent.getContext();
        return new ViewHolder(inflater);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.heur.setText(chatViewArrayList.get(position).getHeur());
        holder.doctorname.setText(chatViewArrayList.get(position).getNamedoctor());
        holder.lastmessage.setText(chatViewArrayList.get(position).getLastMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    String idMedcin = chatViewArrayList.get(position).getId_medcin();
                    listener.onItemClick(idMedcin);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatViewArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView heur;
        TextView doctorname;
        TextView lastmessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            heur = itemView.findViewById(R.id.textView19);
            doctorname = itemView.findViewById(R.id.textView4);
            lastmessage = itemView.findViewById(R.id.textView18);
        }
    }
}

