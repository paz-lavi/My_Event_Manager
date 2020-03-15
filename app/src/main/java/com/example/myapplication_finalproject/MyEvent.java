package com.example.myapplication_finalproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class MyEvent {
    private long timeStamp;
    private String customerName;
    private String customerPhone;
    private String eMail;
    private String date;
    private String startingTime;
    private String address;
    private String status;
    private ArrayList<OrderService> services;
    private int totalPrice;
    private int validFor_Days;
    private String[] dateSplit;
    private Payment payment;
    private String originalInvoiceURL;
    private String copyInvoiceURL;
    private int invoiceNumber;
    private String bidURL;
    private int bidNumber;
    private boolean remainder;


    public MyEvent setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
        return this;
    }

    public MyEvent setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
        return this;
    }

    public MyEvent setDateSplit(String[] dateSplit) {
        this.dateSplit = dateSplit;
        return this;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public MyEvent setInvoiceNumber(int invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public int getBidNumber() {
        return bidNumber;
    }

    public MyEvent setBidNumber(int bidNumber) {
        this.bidNumber = bidNumber;
        return this;
    }

    public String getBidURL() {
        return bidURL;
    }

    public MyEvent setBidURL(String bidURL) {
        this.bidURL = bidURL;
        return this;
    }

    public String getOriginalInvoiceURL() {
        return originalInvoiceURL;
    }

    public MyEvent setOriginalInvoiceURL(String invoiceURL) {
        this.originalInvoiceURL = invoiceURL;
        return this;
    }

    public String getCopyInvoiceURL() {
        return copyInvoiceURL;
    }

    public MyEvent setCopyInvoiceURL(String copyInvoiceURL) {
        this.copyInvoiceURL = copyInvoiceURL;
        return this;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public MyEvent setStatus(String status) {
        this.status = status;


        return this;
    }

    public MyEvent seteMail(String eMail) {
        this.eMail = eMail;
        return this;
    }

    public MyEvent setDate(String date) {
        this.date = date;
        dateSplit = date.split("/");
        return this;
    }

    public MyEvent setStartingTime(String startingTime) {
        this.startingTime = startingTime;
        return this;
    }

    public MyEvent setAddress(String address) {
        this.address = address;
        return this;
    }

    public String geteMail() {
        return eMail;
    }

    public String getDate() {
        return date;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public String getAddress() {
        return address;
    }

    public MyEvent(String customerName, String customerPhone, String eMail,
                   String date, String startingTime, String address, int validFor_Days,
                   boolean remainder, ArrayList<OrderService> services) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.eMail = eMail;
        this.date = date;
        this.startingTime = startingTime;
        this.address = address;
        this.remainder = remainder;
        this.services = services;
        this.validFor_Days = validFor_Days;
        this.status = Constants.STATUS_WAIT_FOR_CONFIRM;
        this.timeStamp = System.currentTimeMillis();
        this.dateSplit = date.split("/");
        calcTotalPrice();
        this.payment = new Payment(totalPrice);


    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public boolean isRemainder() {
        return remainder;
    }

    public MyEvent setRemainder(boolean remainder) {
        this.remainder = remainder;
        return this;
    }

    public int getValidFor_Days() {
        return validFor_Days;
    }

    public MyEvent setValidFor_Days(int validFor_Days) {
        this.validFor_Days = validFor_Days;
        return this;
    }

    public Payment getPayment() {
        return payment;
    }

    public MyEvent() {
    }

    public MyEvent(String customerName, String customerPhone) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.status = Constants.STATUS_WAIT_FOR_CONFIRM;
        this.timeStamp = System.currentTimeMillis();

        calcTotalPrice();


    }

    public MyEvent(String customerName, String customerPhone, ArrayList<OrderService> services) {
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.services = services;
        this.status = Constants.STATUS_WAIT_FOR_CONFIRM;
        this.timeStamp = System.currentTimeMillis();

        calcTotalPrice();

    }

    public MyEvent setCustomerName(String customerName) {
        this.customerName = customerName;
        return this;
    }

    public MyEvent setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
        return this;
    }

    public MyEvent setServices(ArrayList<OrderService> services) {
        this.services = services;
        return this;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public ArrayList<OrderService> getServices() {
        return services;
    }

    public void addService(OrderService service) {
        services.add(service);
        calcTotalPrice();
    }

    public MyEvent setPayment(Payment payment) {
        this.payment = payment;
        return this;
    }

    public boolean addAllService(Collection<? extends OrderService> c) {
        boolean modified = false;
        for (OrderService e : c)
            if (services.add(e))
                modified = true;
        calcTotalPrice();
        return modified;
    }

    private String servicesToString() {
        StringBuilder sb = new StringBuilder();
        for (OrderService s : services) {
            sb.append(s.toString() + "\n");
        }
        return sb.toString();
    }

    private void calcTotalPrice() {
        int temp = 0;
        for (OrderService s : services
        ) {
            temp += s.getTotalPrice();
        }
        totalPrice = temp;
    }


    public String getDay() {
        return dateSplit[0];
    }

    public String getMonth() {
        return dateSplit[1];
    }

    public String getYear() {
        return dateSplit[2];
    }

    @Override
    public String toString() {
        calcTotalPrice();
        StringBuilder sb = new StringBuilder();
        sb.append("\nשם הלקוח : " + customerName);
        sb.append("\nטלפון : " + customerPhone);
        sb.append("\nכתובת : " + address);
        sb.append("\nמייל : " + eMail);
        sb.append("\nתאריך : " + date);
        sb.append("\nשעת התחלה : " + startingTime);
        sb.append("\nסטטוס : " + status);
        sb.append("\nשירותים : " + servicesToString());
        sb.append("\nסה״כ לתשלום : " + totalPrice);

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyEvent event = (MyEvent) o;
        return timeStamp == event.timeStamp &&
                totalPrice == event.totalPrice &&
                Objects.equals(customerName, event.customerName) &&
                Objects.equals(customerPhone, event.customerPhone) &&
                Objects.equals(eMail, event.eMail) &&
                Objects.equals(date, event.date) &&
                Objects.equals(startingTime, event.startingTime) &&
                Objects.equals(address, event.address) &&
                Objects.equals(status, event.status) &&
                Objects.equals(services, event.services);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(timeStamp, customerName, customerPhone, eMail);
        result = 31 * result + Arrays.hashCode(dateSplit);
        return result;
    }


}








