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

    public ScheduleAdapter(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    @Override
    public ScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleViewHolder holder, int position) {
        Schedule schedule = schedules.get(position);
        holder.bind(schedule);
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void updateSchedules(List<Schedule> newSchedules) {
        schedules.clear();
        schedules.addAll(newSchedules);
        notifyDataSetChanged();
    }

    class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private TextView lessonNameTextView;
        private TextView lessonTimeTextView;
        private TextView lessonDescriptionTextView;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            lessonNameTextView = itemView.findViewById(R.id.lesson_name);
            lessonTimeTextView = itemView.findViewById(R.id.lesson_time);
            lessonDescriptionTextView = itemView.findViewById(R.id.lesson_description);
        }

        public void bind(Schedule schedule) {
            lessonNameTextView.setText(schedule.getLessonName());
            lessonTimeTextView.setText(schedule.getLessonTime());
            lessonDescriptionTextView.setText(schedule.getLessonDescription());
        }
    }
}
