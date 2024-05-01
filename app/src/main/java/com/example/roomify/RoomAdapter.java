package com.example.roomify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//RoomAdapter is responsible for the adapter that provides the connection between the room data and its display in a RecyclerView. It creates new instances of RoomViewHolder to represent individual list items and populates them with room data
//
public class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
    private final List<Room> rooms;
    public RoomAdapter(List<Room> rooms) {
        this.rooms = rooms;
    }
    @NonNull
    @Override
    public RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewHolder(view);
    }
    @Override
    public void onBindViewHolder(RoomViewHolder holder, int position) {
        Room room = rooms.get(position);
        String roomNumber = room.getName().replace("ауд.", "").trim();
        holder.audience.setText(roomNumber);
        holder.type.setText(room.getType());
        holder.comment.setText(room.getComment());
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }
}