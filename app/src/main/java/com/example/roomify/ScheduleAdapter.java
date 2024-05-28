package com.example.roomify;

import android.net.ParseException;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private ArrayList<Schedule> schedules;
    private ArrayList<String> uniqueDates;
    private String currentDate;

    public ScheduleAdapter(ArrayList<Schedule> schedules, ArrayList<String> uniqueDates) {
        this.schedules = schedules;
        this.uniqueDates = uniqueDates;
    }

    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
        return new ScheduleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule currentSchedule = schedules.get(position);
        holder.bind(currentSchedule);
        holder.lessonNameTextView.setText(currentSchedule.getLessonName());
        holder.lessonTimeTextView.setText(currentSchedule.getLessonTime());
        holder.lessonDescriptionTextView.setText(stripHtmlTags(currentSchedule.getLessonDescription()));


        if (isFirstLessonForDay(position)) {
            holder.dateTextView.setText(formatDate(currentSchedule.getDate()));
            holder.dateTextView.setVisibility(View.VISIBLE);
        } else {
            holder.dateTextView.setVisibility(View.GONE);
        }
    }

    private boolean isFirstLessonForDay(int position) {
        if (position == 0) {
            return true;
        } else {
            String currentDate = schedules.get(position).getDate();
            String previousDate = schedules.get(position - 1).getDate();
            return !currentDate.equals(previousDate);
        }
    }
    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return schedules.size();
    }

    public void updateSchedules(ArrayList<Schedule> newSchedules) {
        schedules.clear();
        schedules.addAll(newSchedules);
        notifyDataSetChanged();
    }

    private String stripHtmlTags(String html) {
        return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
    }

    private boolean isDifferentDay(String previousDate, String currentDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        try {
            Date prevDate = dateFormat.parse(previousDate);
            Date currDate = dateFormat.parse(currentDate);

            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(prevDate);
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(currDate);

            return cal1.get(Calendar.DAY_OF_YEAR) != cal2.get(Calendar.DAY_OF_YEAR);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    private String formatDate(String date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yyyy EEEE", Locale.getDefault());
        try {
            Date inputDate = inputFormat.parse(date);
            return outputFormat.format(inputDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private TextView lessonNameTextView;
        private TextView lessonTimeTextView;
        private TextView lessonDescriptionTextView;
        private TextView dateTextView;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            lessonNameTextView = itemView.findViewById(R.id.lesson_name);
            lessonTimeTextView = itemView.findViewById(R.id.lesson_time);
            lessonDescriptionTextView = itemView.findViewById(R.id.lesson_description);
            dateTextView = itemView.findViewById(R.id.dateTextView);

        }

        public void bind(Schedule schedule) {
            lessonNameTextView.setText(schedule.getLessonName());
            lessonTimeTextView.setText(schedule.getLessonTime());
        }
    }
}
