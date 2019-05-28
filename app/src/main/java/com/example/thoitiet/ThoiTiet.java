package com.example.thoitiet;

public class ThoiTiet {
    public String day;
    public String status;
    public String temp;
    public Integer icon;
    public String dayformat;

    public ThoiTiet(String day, String status, String temp, Integer icon, String dayformat) {
        this.day = day;
        this.status = status;
        this.temp = temp;
        this.icon = icon;
        this.dayformat=dayformat;
    }
}
