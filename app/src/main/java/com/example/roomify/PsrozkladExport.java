package com.example.roomify;

import java.util.List;

//PsrozkladExport is responsible for the data model representing the schedule export result. It contains a list of free rooms, a code, and error information in case the request was unsuccessful
public class PsrozkladExport {
    private List<FreeRooms> free_rooms;
    private String code;
    private com.example.roomify.ErrorHandling error;
    public com.example.roomify.ErrorHandling getError() {
        return error;
    }

    public void setError(com.example.roomify.ErrorHandling error) {
        this.error = error;
    }
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