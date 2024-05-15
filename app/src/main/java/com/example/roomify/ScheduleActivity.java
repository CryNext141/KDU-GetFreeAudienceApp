package com.example.roomify;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleActivity extends AppCompatActivity {

    private PolitechSoftService service;
    private EditText groupNameEditText;
    private Button fetchScheduleButton;
    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private GestureDetector gestureDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());


        groupNameEditText = findViewById(R.id.group_name_edit_text);
        fetchScheduleButton = findViewById(R.id.fetch_schedule_button);
        scheduleRecyclerView = findViewById(R.id.recyclerView);

        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(new ArrayList<>());
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://195.162.83.28")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PolitechSoftService.class);

        fetchScheduleButton.setOnClickListener(v -> fetchGroupIdAndSchedule());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector != null) {
            return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    private void fetchGroupIdAndSchedule() {
        String groupName = groupNameEditText.getText().toString();

        Call<List<Group>> call = service.getGroups("group", "obj_list", "yes", "json", "UTF8");
        call.enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                if (response.isSuccessful()) {
                    List<Group> groups = response.body();
                    if (groups != null) {
                        for (Group group : groups) {
                            if (group.getName().equalsIgnoreCase(groupName)) {
                                fetchSchedule(group.getID());
                                break;
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private void fetchSchedule(String groupId) {
        // Here you can use the current date and add 7 days to get the end date
        String beginDate = "2024-05-14";  // Example start date
        String endDate = "2024-05-21";    // Example end date

        Call<List<Schedule>> call = service.getSchedule("group", "rozklad", beginDate, endDate, groupId, "json", "UTF8");
        call.enqueue(new Callback<List<Schedule>>() {
            @Override
            public void onResponse(Call<List<Schedule>> call, Response<List<Schedule>> response) {
                if (response.isSuccessful()) {
                    List<Schedule> schedules = response.body();
                    if (schedules != null) {
                        scheduleAdapter.updateSchedules(schedules);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Schedule>> call, Throwable t) {
                // Handle failure
            }
        });
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 15;
        private static final int SWIPE_THRESHOLD_VELOCITY = 30;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // Left to right swipe, start MainActivity
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                return true;
            }
            return false;
        }
    }
}