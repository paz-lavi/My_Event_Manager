package com.example.myapplication_finalproject;

public class MyService {
    private int ServicePrice;
    private String ServiceName;
    private int maxPerEvent;


    public MyService() {
    }

    public MyService(String serviceName, int servicePrice, int maxPerEvent) {
        ServicePrice = servicePrice;
        ServiceName = serviceName;
        this.maxPerEvent = maxPerEvent;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public MyService setServiceName(String serviceName) {
        ServiceName = serviceName;
        return this;
    }

    public int getServicePrice() {
        return ServicePrice;
    }

    public MyService setServicePrice(int servicePrice) {
        ServicePrice = servicePrice;
        return this;
    }

    public int getMaxPerEvent() {
        return maxPerEvent;
    }

    public MyService setMaxPerEvent(int maxPerEvent) {
        this.maxPerEvent = maxPerEvent;
        return this;
    }

    @Override
    public String toString() {
        return ServiceName + "\nמחיר ליחידה:" + ServicePrice;
    }
}
