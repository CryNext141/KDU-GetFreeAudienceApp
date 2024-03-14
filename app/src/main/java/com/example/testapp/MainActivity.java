package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

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

        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(1);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(2);
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(3);
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(4);
            }
        });

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(5);
            }
        });

        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadRooms(6);
            }
        });
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
                        Log.d(TAG, "Room: " + roomName);
                        textView.append(roomName + "\n");
                    }
                }
            }


        });
    }
}
