package com.example.myapplication_finalproject;

public class OrderService extends MyService {
    //  private MyService service;
    private int count;

    public OrderService(MyService service, int count) {
        super(service.getServiceName(), service.getServicePrice(), service.getMaxPerEvent());
        this.count = count;
    }

    public OrderService(MyService service) {
        super(service.getServiceName(), service.getServicePrice(), service.getMaxPerEvent());
        this.count = 0;
    }

    public OrderService(String serviceName, int servicePrice, int maxPerEvent) {
        super(serviceName, servicePrice, maxPerEvent);
    }

    public OrderService() {
        super();
    }

    ;

    public int getCount() {
        return count;
    }


    public OrderService setCount(int count) {
        this.count = count;
        return this;
    }

    public int getTotalPrice() {
        return getServicePrice() * count;
    }


    @Override
    public String toString() {
        return super.toString() + "\nסה״כ:" + count * getServicePrice();
    }
}
