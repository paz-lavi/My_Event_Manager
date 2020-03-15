package com.example.myapplication_finalproject;

public interface UpdateServices {
    boolean update(MyService service);

    void remove(MyService service);

    void toast(String msg);

    void gone();

    void visible();
}
