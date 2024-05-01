package com.example.roomify;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//RoomViewHolder class is responsible for representing a single item in a RecyclerView, initializing and managing visual elements such as text fields for displaying room data
public  class RoomViewHolder extends RecyclerView.ViewHolder {
    TextView audience, type, comment;

    public RoomViewHolder(View itemView) {
        super(itemView);
        audience = itemView.findViewById(R.id.audience);
        type = itemView.findViewById(R.id.type);
        comment = itemView.findViewById(R.id.comment);
    }
}