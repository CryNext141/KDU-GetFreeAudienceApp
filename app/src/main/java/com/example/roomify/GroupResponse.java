package com.example.roomify;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupResponse {
    @SerializedName("psrozklad_export")
    private DepartmentData departmentData;

    public DepartmentData getDepartmentData() {
        return departmentData;
    }

    public void setDepartmentData(DepartmentData departmentData) {
        this.departmentData = departmentData;
    }

    public static class DepartmentData {
        @SerializedName("departments")
        private List<Department> departments;

        public List<Department> getDepartments() {
            return departments;
        }

        public void setDepartments(List<Department> departments) {
            this.departments = departments;
        }

        public static class Department {
            @SerializedName("name")
            private String name;
            @SerializedName("objects")
            private List<Group> objects;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<Group> getObjects() {
                return objects;
            }

            public void setObjects(List<Group> objects) {
                this.objects = objects;
            }
        }
    }
}

