package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Transaction {
    private String date;
    private String time;
    private String description;
    private String vendor;
    private double amount;

    public Transaction(String date, String time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
    }

    public double getAmount() {
        return amount;
    }

    public String getVendor() {
        return vendor;
    }

    public String toCsv() {
        return date + "|" + time + "|" + description + "|" + vendor + "|" + amount;
    }

    @Override
    public String toString() {
        return date + " " + time + " | " + description + " | " + vendor + " | " + amount;
    }
}


