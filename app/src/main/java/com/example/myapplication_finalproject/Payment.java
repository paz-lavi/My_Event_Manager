package com.example.myapplication_finalproject;

import com.paypal.android.sdk.payments.ProofOfPayment;

public class Payment {
    private int totalAmount;
    private int amountPaid;
    private int paidByCash;
    private int paidByPayPal;
    private int amountleft;
    private ProofOfPayment paypalProof;

    public Payment setAmountleft(int amountleft) {
        this.amountleft = amountleft;
        return this;
    }

    public ProofOfPayment getPaypalProof() {
        return paypalProof;
    }

    public Payment setPaypalProof(ProofOfPayment paypalProof) {
        this.paypalProof = paypalProof;
        return this;
    }

    public Payment(int totalAmount) {
        this.totalAmount = totalAmount;
        this.amountleft = totalAmount;
        this.amountPaid = 0;
        this.paidByCash = 0;
        this.paidByPayPal = 0;


    }


    public int getTotalAmount() {
        return totalAmount;
    }

    public int getAmountPaid() {
        return amountPaid;
    }

    public int getPaidByCash() {
        return paidByCash;
    }

    public int getPaidByPayPal() {
        return paidByPayPal;
    }


    private void setPaidByCash(int paidByCash) {
        this.paidByCash = paidByCash;
    }

    private void setAmountPaid(int amountPaid) {
        this.amountPaid = amountPaid;
        setAmountleft();

    }

    public Payment setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
        return this;
    }

    public int getAmountleft() {
        return amountleft;
    }

    public void setAmountleft() {
        amountleft = totalAmount - amountPaid;

    }

    private void setPaidByPayPal(int paidByPayPal) {
        this.paidByPayPal = paidByPayPal;
    }


    public void payWithCash(int amount) {
        paidByCash += amount;
        setAmountPaid(paidByCash + paidByPayPal);
    }


    public void setpaidByPayPal(ProofOfPayment paypalProof) {
        this.paypalProof = paypalProof;
        paidByPayPal = amountleft;
        setAmountPaid(paidByCash + paidByPayPal);


    }
}
