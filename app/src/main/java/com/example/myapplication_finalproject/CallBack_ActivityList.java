package com.example.myapplication_finalproject;

public interface CallBack_ActivityList {
    boolean addService(String name, int price, int max);

    void toast(String msg);

    void visible();

    void gone();
}
