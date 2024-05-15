package com.example.roomify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ScheduleResponse {
    @SerializedName("psrozklad_export")
    private ScheduleData scheduleData;

    public ScheduleData getScheduleData() {
        return scheduleData;
    }

    public void setScheduleData(ScheduleData scheduleData) {
        this.scheduleData = scheduleData;
    }

    public static class ScheduleData {
        @SerializedName("roz_items")
        private List<Schedule> rozItems;

        public List<Schedule> getRozItems() {
            return rozItems;
        }

        public void setRozItems(List<Schedule> rozItems) {
            this.rozItems = rozItems;
        }
    }
}