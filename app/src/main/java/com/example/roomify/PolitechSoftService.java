package com.example.roomify;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//PolitechSoftService defines a method used for interacting with a remote API using the Retrofit library. It is responsible for creating a request to the API to retrieve a list of available rooms based on certain parameters
public interface PolitechSoftService {
    @GET("/cgi-bin/timetable_export.cgi")
    Call<com.example.roomify.Root> getRooms(@Query("req_type") String req_type,
                                     @Query("lesson") int lesson,
                                     @Query("req_format") String req_format,
                                     @Query("coding_mode") String coding_mode,
                                     @Query("bs") String bs);
}

