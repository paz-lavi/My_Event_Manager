package com.example.myapplication_finalproject;

public class Expenses {
    private String date;
    private String purpose;
    private double amount;
    private String image_url;
    private long timestampe;

    public Expenses() {
    }

    public Expenses(String date, String purpose, double amount, String uri, long timestampe) {
        this.date = date;
        this.purpose = purpose;
        this.amount = amount;
        this.image_url = uri;
        this.timestampe = timestampe;

    }


    public String getDate() {
        return date;
    }

    public Expenses setDate(String date) {
        this.date = date;
        return this;
    }

    public String getPurpose() {
        return purpose;
    }

    public Expenses setPurpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public double getAmount() {
        return amount;
    }

    public Expenses setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public String getImage_url() {
        return image_url;
    }

    public Expenses setImage_url(String image_url) {
        this.image_url = image_url;
        return this;
    }

    @Override
    public String toString() {
        return "Expenses{" +
                "date='" + date + '\'' +
                ", purpose='" + purpose + '\'' +
                ", amount=" + amount +
                ", image_url=" + image_url +
                ", timestampe=" + timestampe +
                '}';
    }
}
