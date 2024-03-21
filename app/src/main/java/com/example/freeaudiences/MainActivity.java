package com.example.freeaudiences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.ColorStateList;
import android.graphics.Color;
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
import java.util.List;
import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {
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

    public interface MyApi {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LocalTime currentTime = LocalTime.now();

        ColorStateList rippleColor = ColorStateList.valueOf(Color.argb(255, 29,171, 222));

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.HeaderColor));

        MaterialButton button1 = findViewById(R.id.button1);
        MaterialButton button2 = findViewById(R.id.button2);
        MaterialButton button3 = findViewById(R.id.button3);
        MaterialButton button4 = findViewById(R.id.button4);
        MaterialButton button5 = findViewById(R.id.button5);
        MaterialButton button6 = findViewById(R.id.button6);

        button1.setOnClickListener(v -> loadRooms(1));

        button2.setOnClickListener(v -> loadRooms(2));

        button3.setOnClickListener(v -> loadRooms(3));
        button4.setOnClickListener(v -> loadRooms(4));
        button5.setOnClickListener(v -> loadRooms(5));
        button6.setOnClickListener(v -> loadRooms(6));

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
            public void onResponse(@NonNull Call<Root> call, @NonNull Response<Root> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Root root = response.body();
                assert root != null;
                List<FreeRooms> freeRoomsList = root.getPsrozklad_export().getFree_rooms();

                RecyclerView recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                List<Room> allRooms = new ArrayList<>();

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
}
