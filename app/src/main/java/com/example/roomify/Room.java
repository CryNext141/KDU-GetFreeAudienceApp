package com.example.roomify;

//Room is responsible for the data model representing an individual room. It contains information about the room's name, type, and comments
public class Room {
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