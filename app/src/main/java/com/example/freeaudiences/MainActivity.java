package com.example.freeaudiences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.List;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static class Room {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

    public interface MyApi {
        @GET("/cgi-bin/timetable_export.cgi")
        Call<Root> getRooms(@Query("req_type") String req_type,
                            @Query("lesson") int lesson,
                            @Query("req_format") String req_format,
                            @Query("coding_mode") String coding_mode,
                            @Query("bs") String bs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalTime currentTime = LocalTime.now();

        ColorStateList rippleColor = ColorStateList.valueOf(Color.argb(255, 29,171, 222));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.MainColor));

        MaterialButton button1 = findViewById(R.id.button1);
        MaterialButton button2 = findViewById(R.id.button2);
        MaterialButton button3 = findViewById(R.id.button3);
        MaterialButton button4 = findViewById(R.id.button4);
        MaterialButton button5 = findViewById(R.id.button5);
        MaterialButton button6 = findViewById(R.id.button6);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(1);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(2);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(3);
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(4);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(5);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(6);
            }
        });

        if (isTimeInRange(currentTime, LocalTime.of(8, 30), LocalTime.of(9, 50))) {
            button1.setBackgroundColor(Color.argb(255, 223,224,255));
            button1.setTextColor(Color.argb(255, 94,103,163));
            button1.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(10, 0), LocalTime.of(11, 20))) {
            button2.setBackgroundColor(Color.argb(255, 223,224,255));
            button2.setTextColor(Color.argb(255, 94,103,163));
            button2.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(12, 0), LocalTime.of(13, 20))) {
            button3.setBackgroundColor(Color.argb(255, 223,224,255));
            button3.setTextColor(Color.argb(255, 94,103,163));
            button3.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(13, 30), LocalTime.of(14, 50))) {
            button4.setBackgroundColor(Color.argb(255, 223,224,255));
            button4.setTextColor(Color.argb(255, 94,103,163));
            button4.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(15, 10), LocalTime.of(16, 30))) {
            button5.setBackgroundColor(Color.argb(255, 223, 224, 255));
            button5.setTextColor(Color.argb(255, 94, 103, 163));
            button5.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(16, 40), LocalTime.of(18, 0))) {
            button6.setBackgroundColor(Color.argb(255, 223,224,255));
            button6.setTextColor(Color.argb(255, 94,103,163));
            button6.setRippleColor(rippleColor);
        }
        else if (isTimeInRange(currentTime, LocalTime.of(18, 35), LocalTime.of(18, 40))) {
            button6.setBackgroundColor(Color.argb(255, 223,224,255));
            button6.setTextColor(Color.argb(255, 94,103,163));
            button6.setRippleColor(rippleColor);
        }
    }
    private boolean isTimeInRange(LocalTime currentTime, LocalTime startTime, LocalTime endTime) {
        return !currentTime.isBefore(startTime) && !currentTime.isAfter(endTime);
    }

    private void loadRooms(int lesson) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://195.162.83.28")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyApi api = retrofit.create(MyApi.class);

        Call<Root> call = api.getRooms("free_rooms_list", lesson, "json", "UTF8", "ok");
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, Response<Root> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Code: " + response.code());
                    return;
                }
                Root root = response.body();
                List<FreeRooms> freeRoomsList = root.getPsrozklad_export().getFree_rooms();

                TextView textView = findViewById(R.id.textView);
                textView.setText("");

                for (FreeRooms freeRooms : freeRoomsList) {
                    List<Room> rooms = freeRooms.getRooms();
                    for (Room room : rooms) {
                        String roomName = room.getName();
                        textView.append(roomName + "\n");
                    }
                }
            }
            @Override
            public void onFailure(Call<Root> call, Throwable t) {
            }
        });
    }
}