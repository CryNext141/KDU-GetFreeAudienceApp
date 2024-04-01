package com.example.roomify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalTime;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private List<Room> allRooms;
    int defaultClassButtonColor;
    int selectedClassButtonColor;
    int defaultFilterButtonColor;
    int selectedFilterButtonColor;
    MaterialButton currentClassButton = null;
    MaterialButton currentFilterButton = null;
    MaterialButton currentTimeButton = null;
    private TextView noInternetTextView;
    public static class Room {
        private String name;
        private String type;
        private String comment;
        public String getName() {
            return name;
        }
        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getComment() {
            return comment;
        }
        public void setComment(String comment) {
            this.comment = comment;
        }
    }

    public static class FreeRooms {
        private String date;
        private String lesson;
        private List<Room> rooms;
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }
        public String getLesson() {
            return lesson;
        }
        public void setLesson(String lesson) {
            this.lesson = lesson;
        }
        public List<Room> getRooms() {
            return rooms;
        }
        public void setRooms(List<Room> rooms) {
            this.rooms = rooms;
        }
    }
    public static class PsrozkladExport {
        private List<FreeRooms> free_rooms;
        private String code;
        public List<FreeRooms> getFree_rooms() {
            return free_rooms;
        }
        public void setFree_rooms(List<FreeRooms> free_rooms) {
            this.free_rooms = free_rooms;
        }
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class Root {
        private PsrozkladExport psrozklad_export;

        public PsrozkladExport getPsrozklad_export() {
            return psrozklad_export;
        }

        public void setPsrozklad_export(PsrozkladExport psrozklad_export) {
            this.psrozklad_export = psrozklad_export;
        }
    }

    public interface PolitechSoft {
        @GET("/cgi-bin/timetable_export.cgi")
        Call<Root> getRooms(@Query("req_type") String req_type,
                            @Query("lesson") int lesson,
                            @Query("req_format") String req_format,
                            @Query("coding_mode") String coding_mode,
                            @Query("bs") String bs);
    }

    public static class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView audience, type, comment;

        public RoomViewHolder(View itemView) {
            super(itemView);
            audience = itemView.findViewById(R.id.audience);
            type = itemView.findViewById(R.id.type);
            comment = itemView.findViewById(R.id.comment);
        }
    }

    public static class RoomAdapter extends RecyclerView.Adapter<RoomViewHolder> {
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

    private final BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateConnectivityStatus();
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkChangeReceiver);
    }

    @SuppressLint("InflateParams")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allRooms = new ArrayList<>();

        noInternetTextView = findViewById(R.id.noInternetTextView);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LocalTime currentTime = LocalTime.now();


        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.HeaderColor));

        defaultClassButtonColor = ContextCompat.getColor(this, R.color.defaultButtonColor);
        selectedClassButtonColor = ContextCompat.getColor(this, R.color.pressedButtonColor);

        defaultFilterButtonColor = ContextCompat.getColor(this, R.color.floorsButtonColor);
        selectedFilterButtonColor = ContextCompat.getColor(this, R.color.floorsPressedButtonColor);

        MaterialButton button1 = findViewById(R.id.button1);
        MaterialButton button2 = findViewById(R.id.button2);
        MaterialButton button3 = findViewById(R.id.button3);
        MaterialButton button4 = findViewById(R.id.button4);
        MaterialButton button5 = findViewById(R.id.button5);
        MaterialButton button6 = findViewById(R.id.button6);


        final TextView dateTextView = findViewById(R.id.date);
        final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMMM", new Locale("en","UA"));
        String currentDate = sdf.format(new Date());
        dateTextView.setText(" Classes for:\n " + currentDate);

        if (isTimeInRange(currentTime, LocalTime.of(8, 0), LocalTime.of(9, 50))) {
            currentTimeButton = button1;

        } else if (isTimeInRange(currentTime, LocalTime.of(9, 50), LocalTime.of(11, 20))) {
            currentTimeButton = button2;
        }
        else if (isTimeInRange(currentTime, LocalTime.of(11, 20), LocalTime.of(13, 20))) {
            currentTimeButton = button3;
        }
        else if (isTimeInRange(currentTime, LocalTime.of(13, 20), LocalTime.of(14, 50))) {
            currentTimeButton = button4;
        }
        else if (isTimeInRange(currentTime, LocalTime.of(14, 50), LocalTime.of(16, 30))) {
            currentTimeButton = button5;
        }
        else if (isTimeInRange(currentTime, LocalTime.of(16, 30), LocalTime.of(18, 0))) {
            currentTimeButton = button6;
        }

        if (currentTimeButton != null) {
            currentTimeButton.setBackgroundColor(Color.argb(255, 223,224,255));
            currentTimeButton.setTextColor(Color.argb(255, 94,103,163));
        }


        setupButton(R.id.button1, () -> loadRooms(1), true);
        setupButton(R.id.button2, () -> loadRooms(2), true);
        setupButton(R.id.button3, () -> loadRooms(3), true);
        setupButton(R.id.button4, () -> loadRooms(4), true);
        setupButton(R.id.button5, () -> loadRooms(5), true);
        setupButton(R.id.button6, () -> loadRooms(6), true);


        setupButton(R.id.button_all, () -> recyclerView.setAdapter(new RoomAdapter(allRooms)), false);
        setupButton(R.id.button_2, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "2");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
        setupButton(R.id.button_3, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "3");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
        setupButton(R.id.button_4, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "4");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
        setupButton(R.id.button_5, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "5");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
        setupButton(R.id.button_6, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "6");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
        setupButton(R.id.button_other, () -> {
            List<Room> filteredRooms = filterRoomsByFloor(allRooms, "other");
            recyclerView.setAdapter(new RoomAdapter(filteredRooms));
        }, false);
    }
    private boolean isTimeInRange(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }

    private void loadRooms(int lesson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://195.162.83.28")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PolitechSoft api = retrofit.create(PolitechSoft.class);

        Call<Root> call = api.getRooms("free_rooms_list", lesson, "json", "UTF8", "ok");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Root root = response.body();
                assert root != null;
                List<FreeRooms> freeRoomsList = root.getPsrozklad_export().getFree_rooms();

                allRooms.clear();

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                for (FreeRooms freeRooms : freeRoomsList) {
                    List<Room> rooms = freeRooms.getRooms();
                    allRooms.addAll(rooms);
                }

                recyclerView.setAdapter(new RoomAdapter(allRooms));
            }
            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
            }
        });
    }
    private List<Room> filterRoomsByFloor(List<Room> rooms, String floor) {
        List<Room> filteredRooms = new ArrayList<>();
        for (Room room : rooms) {
            String roomNumber = room.getName().replace("ауд.", "").trim();
            if (floor.equals("other")) {
                if (!Character.isDigit(roomNumber.charAt(0))) {
                    filteredRooms.add(room);
                }
            } else if (roomNumber.startsWith(floor)) {
                filteredRooms.add(room);
            }
        }
        return filteredRooms;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void updateConnectivityStatus() {
        if (!isNetworkAvailable()) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.noInternet));
            noInternetTextView.setVisibility(View.VISIBLE);
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.MainColor));
            noInternetTextView.setVisibility(View.GONE);
        }
    }
    @SuppressLint("ResourceAsColor")
    private void setupButton(int buttonId, Runnable action, boolean isClassButton) {
        MaterialButton button = findViewById(buttonId);
        button.setOnClickListener(v -> {
            if (isClassButton) {
                if (currentClassButton != null) {
                    currentClassButton.setBackgroundColor(defaultClassButtonColor);
                    currentClassButton.setTextColor(Color.argb(255,226,225,239));
                }
                currentClassButton = button;
                currentClassButton.setBackgroundColor(selectedClassButtonColor);
                currentClassButton.setTextColor(Color.argb(255,226,225,239));

                if (currentFilterButton != null) {
                    currentFilterButton.setBackgroundColor(defaultFilterButtonColor);
                    currentFilterButton = null;
                }

                if (currentTimeButton != null && currentTimeButton != currentClassButton) {
                    currentTimeButton.setBackgroundColor(Color.argb(255, 223,224,255));
                    currentTimeButton.setTextColor(Color.argb(255, 94,103,163));
                }
            } else {
                if (currentFilterButton != null) {
                    currentFilterButton.setBackgroundColor(defaultFilterButtonColor);
                }
                currentFilterButton = button;
                currentFilterButton.setBackgroundColor(selectedFilterButtonColor);
            }
            if (action != null) {
                action.run();
            }
        });
    }
}

