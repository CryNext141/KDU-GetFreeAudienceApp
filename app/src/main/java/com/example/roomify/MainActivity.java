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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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
    private TextView apiErrorTextView;
    private final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(2, TimeUnit.SECONDS)
            .connectTimeout(2, TimeUnit.SECONDS)
            .build();
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://195.162.83.28")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
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

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allRooms = new ArrayList<>();

        noInternetTextView = findViewById(R.id.noInternetTextView);
        apiErrorTextView = findViewById(R.id.apiErrorTextView);

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
        dateTextView.setText("Classes for:\n " + currentDate);

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

        //array of buttons identifiers
        int[] buttonId = {R.id.button1, R.id.button2, R.id.button3, R.id.button4, R.id.button5, R.id.button6};

        for (int i = 0; i < buttonId.length; i++) {
            final int finalI = i;
            setupButton(buttonId[i], () -> loadRooms(finalI+1), true);
        }

        //array of filter buttons identifiers
        int[] filterButtonIds = {R.id.button_all, R.id.button_2, R.id.button_3, R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_other};
        String[] floors = {"all", "2", "3", "4", "5", "6", "other"};

        for (int i = 0; i < filterButtonIds.length; i++) {
            String floor = floors[i];
            final int finalI = i;
            setupButton(filterButtonIds[i], () -> {
                List<Room> filteredRooms = filterRoomsByFloor(allRooms, floor);
                recyclerView.setAdapter(new RoomAdapter(filteredRooms));
            }, false);
        }
    }

    //Checks if the time is within the specified range
    private boolean isTimeInRange(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }


    //Loads the list of available rooms for a specified lesson. It makes an API call using Retrofit and updates the UI accordingly
    private void loadRooms(int lesson) {
        PolitechSoftService api = retrofit.create(PolitechSoftService.class);

        Call<Root> call = api.getRooms("free_rooms_list", lesson, "json", "UTF8", "ok");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {

                apiErrorTextView.setVisibility(View.GONE);
                getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.MainColor));

                Root root = response.body();
                assert root != null;

                if (root.getPsrozklad_export().getError() != null) {
                    String errorMessage = root.getPsrozklad_export().getError().getError_message();
                    String errorCode = root.getPsrozklad_export().getError().getErrorcode();
                    Toast.makeText(MainActivity.this, "Error: " + errorMessage + ", code: " + errorCode, Toast.LENGTH_LONG).show();
                    return;
                }

                List<FreeRooms> freeRoomsList = root.getPsrozklad_export().getFree_rooms();

                if (freeRoomsList != null) {
                    allRooms.clear();

                    RecyclerView recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                    for (FreeRooms freeRooms : freeRoomsList) {
                        List<Room> rooms = freeRooms.getRooms();
                        allRooms.addAll(rooms);
                    }
                    recyclerView.setAdapter(new RoomAdapter(allRooms));
                } else {
                    Toast.makeText(MainActivity.this, "No free rooms", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Root> call, @NonNull Throwable t) {
                if (t instanceof SocketTimeoutException) {
                    apiErrorTextView.setVisibility(View.VISIBLE);
                    getWindow().setStatusBarColor(ContextCompat.getColor(MainActivity.this, R.color.apiError));
                }
            }
        });
    }

    //Filters the list of rooms based on the floor number
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

    //Checks if the network is available
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    //Updates the UI to reflect the current network connectivity status
    private void updateConnectivityStatus() {
        if (!isNetworkAvailable()) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.noInternet));
            noInternetTextView.setVisibility(View.VISIBLE);
        } else {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.MainColor));
            noInternetTextView.setVisibility(View.GONE);
        }
    }

    //Sets up buttons in the UI and assigns event handlers to them based on whether they are class buttons or filter buttons
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