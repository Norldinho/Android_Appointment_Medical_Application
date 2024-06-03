package com.example.mkjli.medcin.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkjli.R;
import com.example.mkjli.ui.chat.AndroidUtil;
import com.example.mkjli.ui.chat.ChatActivity;
import com.example.mkjli.ui.chat.UserId;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecycler extends FirestoreRecyclerAdapter<UserId, SearchUserRecycler.UserModelViewHolder> {

    private Context context;

    public SearchUserRecycler(@NonNull FirestoreRecyclerOptions<UserId> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull SearchUserRecycler.UserModelViewHolder holder, int position, @NonNull UserId model) {
        holder.usernameText.setText(model.getId_patient());

        holder.itemView.setOnClickListener(v -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("koki",model.getId_patient());
            intent.putExtra("source", "FragmentOne");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public SearchUserRecycler.UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pour_chat, parent, false);
        return new SearchUserRecycler.UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.textView4);
            // Supprimer ces lignes si elles ne sont pas nécessaires
            // phoneText = itemView.findViewById(R.id.textView19);
            // profilePic = itemView.findViewById(R.id.textView18);
        }
    }
}