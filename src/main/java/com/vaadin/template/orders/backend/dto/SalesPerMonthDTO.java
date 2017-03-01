package com.vaadin.template.orders.backend.dto;

public class SalesPerMonthDTO {

    private int year;
    private int month;
    private double sales;

    public SalesPerMonthDTO(int year, int month, double sales){
        this.year = year;
        this.month = month;
        this.sales = sales;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public double getSales() {
        return sales;
    }
}
