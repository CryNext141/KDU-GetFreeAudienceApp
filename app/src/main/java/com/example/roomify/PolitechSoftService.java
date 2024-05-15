package com.example.roomify;
import java.util.List;

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

    @GET("/cgi-bin/timetable_export.cgi")
    Call<GroupResponse> getGroups(
            @Query("req_mode") String reqMode,
            @Query("req_type") String reqType,
            @Query("show_ID") String showID,
            @Query("req_format") String reqFormat,
            @Query("coding_mode") String codingMode
    );

    @GET("/cgi-bin/timetable_export.cgi")
    Call<ScheduleResponse> getSchedule(
            @Query("req_mode") String reqMode,
            @Query("req_type") String reqType,
            @Query("begin_date") String beginDate,
            @Query("end_date") String endDate,
            @Query("OBJ_ID") String groupId,
            @Query("req_format") String reqFormat,
            @Query("coding_mode") String codingMode
    );
}


