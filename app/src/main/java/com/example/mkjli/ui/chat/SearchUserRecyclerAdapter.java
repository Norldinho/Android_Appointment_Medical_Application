package com.example.mkjli.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.mkjli.R;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<UserId, SearchUserRecyclerAdapter.UserModelViewHolder> {

    private Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<UserId> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull UserId model) {
        holder.usernameText.setText(model.getId_medcin());

        holder.itemView.setOnClickListener(v -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("username",model.getId_medcin());
            intent.putExtra("source", "FragmentTwo");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pour_chat, parent, false);
        return new UserModelViewHolder(view);
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
