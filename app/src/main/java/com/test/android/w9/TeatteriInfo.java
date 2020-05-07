package com.test.android.w9;

public class TeatteriInfo {

    private String alue = "";
    private String id = "";

    public TeatteriInfo(String alue, String id) {
        this.alue = alue;
        this.id = id;
    }

    public String getArea() {
        return alue;
    }

    public String getID() {
        return id;
    }
}
