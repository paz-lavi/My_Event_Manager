package com.example.myapplication_finalproject;

public class User {
    private String name;
    private String id;
    private String phone;
    private String street;
    private String houseNumber;
    private String city;
    private String mail;
    private int lastReceiptNumber;
    private int lastBidNumber;
    private String paypalClientID;
    private String signatureURL;
    private int signatureVersion;

    public int getSignatureVersion() {
        return signatureVersion;
    }

    public User setSignatureVersion(int signatureVersion) {
        this.signatureVersion = signatureVersion;
        return this;
    }

    public String getSignatureURL() {
        return signatureURL;
    }

    public User setSignatureURL(String signatureURL) {
        this.signatureURL = signatureURL;
        return this;
    }


    public User(String name, String id, String phone, String street, String houseNumber, String city, String mail,
                int lastReceiptNumber, int lastBidNumber, String paypalClientID) {
        this.name = name;
        this.id = id;
        this.phone = phone;
        this.street = street;
        this.houseNumber = houseNumber;
        this.city = city;
        this.mail = mail;
        this.lastReceiptNumber = lastReceiptNumber;
        this.lastBidNumber = lastBidNumber;
        this.paypalClientID = paypalClientID;
    }

    public int getLastBidNumber() {
        return lastBidNumber;
    }

    public User setLastBidNumber(int lastBidNumber) {
        this.lastBidNumber = lastBidNumber;
        return this;
    }

    public User() {
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getStreet() {
        return street;
    }

    public User setStreet(String street) {
        this.street = street;
        return this;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public User setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
        return this;
    }

    public String getCity() {
        return city;
    }

    public User setCity(String city) {
        this.city = city;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public User setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public int getLastReceiptNumber() {
        return lastReceiptNumber;
    }

    public User setLastReceiptNumber(int lastReceiptNumber) {
        this.lastReceiptNumber = lastReceiptNumber;
        return this;
    }

    public String getPaypalClientID() {
        return paypalClientID;
    }

    public User setPaypalClientID(String paypalClientID) {
        this.paypalClientID = paypalClientID;
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", phone='" + phone + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", city='" + city + '\'' +
                ", mail='" + mail + '\'' +
                ", lastReceiptNumber='" + lastReceiptNumber + '\'' +
                ", paypalClientID='" + paypalClientID + '\'' +
                '}';
    }

    public void newInvoice() {
        lastReceiptNumber += 1;
    }

    public void newBid() {
        lastBidNumber += 1;
    }
}