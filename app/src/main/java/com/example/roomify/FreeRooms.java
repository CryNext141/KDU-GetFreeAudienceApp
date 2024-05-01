package com.example.roomify;

import java.util.List;

//FreeRooms represents a model for available rooms on a specific date and lesson, containing information about the date, lesson, and a list of available rooms
public class FreeRooms {
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
