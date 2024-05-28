package com.example.roomify;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());


        groupNameEditText = findViewById(R.id.group_name_edit_text);
        fetchScheduleButton = findViewById(R.id.fetch_schedule_button);
        scheduleRecyclerView = findViewById(R.id.recyclerView);

        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scheduleAdapter = new ScheduleAdapter(new ArrayList<>(), new ArrayList<>());
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://195.162.83.28")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(PolitechSoftService.class);

        fetchScheduleButton.setOnClickListener(v -> fetchGroupIdAndSchedule());

        scheduleRecyclerView.setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

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

        Call<GroupResponse> call = service.getGroups("group", "obj_list", "yes", "json", "UTF8");
        Log.d("ScheduleActivity", "Request URL: " + call.request().url());

        call.enqueue(new Callback<GroupResponse>() {
            @Override
            public void onResponse(Call<GroupResponse> call, Response<GroupResponse> response) {
                if (response.isSuccessful()) {
                    GroupResponse groupResponse = response.body();
                    if (groupResponse != null && groupResponse.getDepartmentData() != null) {
                        List<GroupResponse.DepartmentData.Department> departments = groupResponse.getDepartmentData().getDepartments();
                        for (GroupResponse.DepartmentData.Department department : departments) {
                            List<Group> groups = department.getObjects();
                            for (Group group : groups) {
                                if (group.getName().equalsIgnoreCase(groupName)) {
                                    Log.d("ScheduleActivity", "Group found: " + group.getName());
                                    fetchSchedule(group.getID());
                                    return;
                                }
                            }
                        }
                    } else {
                        Log.e("ScheduleActivity", "No groups found");
                    }
                } else {
                    Log.e("ScheduleActivity", "Response not successful: " + response.code() + " " + response.message());
                    try {
                        Log.e("ScheduleActivity", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("ScheduleActivity", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<GroupResponse> call, Throwable t) {
                Log.e("ScheduleActivity", "API call failed", t);
            }
        });
    }


    private void fetchSchedule(String groupId) {
        String beginDate = "15.05.2024";
        String endDate = "30.05.2024";

        Call<ScheduleResponse> call = service.getSchedule("group", "rozklad", beginDate, endDate, groupId, "json", "UTF8");
        Log.d("ScheduleActivity", "Request URL: " + call.request().url());

        call.enqueue(new Callback<ScheduleResponse>() {
            @Override
            public void onResponse(Call<ScheduleResponse> call, Response<ScheduleResponse> response) {
                if (response.isSuccessful()) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse != null && scheduleResponse.getScheduleData() != null) {
                        List<Schedule> schedules = scheduleResponse.getScheduleData().getRozItems();
                        if (schedules != null) {
                            Log.d("ScheduleActivity", "Schedules fetched: " + schedules.size());
                            scheduleAdapter.updateSchedules(new ArrayList<>(schedules));
                        } else {
                            Log.e("ScheduleActivity", "No schedules found");
                        }
                    }
                } else {
                    Log.e("ScheduleActivity", "Response not successful: " + response.code() + " " + response.message());
                    try {
                        Log.e("ScheduleActivity", "Error body: " + response.errorBody().string());
                    } catch (Exception e) {
                        Log.e("ScheduleActivity", "Error parsing error body", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ScheduleResponse> call, Throwable t) {
                Log.e("ScheduleActivity", "API call failed", t);
            }
        });
    }

    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_MIN_DISTANCE = 170;
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