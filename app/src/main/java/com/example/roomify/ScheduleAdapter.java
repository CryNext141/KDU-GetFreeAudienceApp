package com.example.roomify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// ScheduleAdapter.java
public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> schedules;

    public ScheduleAdapter(List<Schedule> schedules)     {
        this.schedules = schedules;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.subjectTextView.setText(schedule.getSubject());
        holder.timeTextView.setText(schedule.getTime());
        holder.roomTextView.setText(schedule.getRoom());
        holder.teacherTextView.setText(schedule.getTeacher());
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void updateSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
        notifyDataSetChanged();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {

        TextView subjectTextView;
        TextView timeTextView;
        TextView roomTextView;
        TextView teacherTextView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectTextView = itemView.findViewById(R.id.subject_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            roomTextView = itemView.findViewById(R.id.room_text_view);
            teacherTextView = itemView.findViewById(R.id.teacher_text_view);
        }
    }
}
