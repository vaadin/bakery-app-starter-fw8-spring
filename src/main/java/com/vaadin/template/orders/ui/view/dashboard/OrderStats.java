package com.vaadin.template.orders.ui.view.dashboard;

public class OrderStats {

    private int deliveredToday;
    private int dueToday;
    private int dueTomorrow;
    private int notAvailableToday;
    private int unverified;

    public int getDeliveredToday() {
        return deliveredToday;
    }

    public void setDeliveredToday(int deliveredToday) {
        this.deliveredToday = deliveredToday;
    }

    public int getDueToday() {
        return dueToday;
    }

    public void setDueToday(int dueToday) {
        this.dueToday = dueToday;
    }

    public int getDueTomorrow() {
        return dueTomorrow;
    }

    public void setDueTomorrow(int dueTomorrow) {
        this.dueTomorrow = dueTomorrow;
    }

    public int getNotAvailableToday() {
        return notAvailableToday;
    }

    public void setNotAvailableToday(int notAvailableToday) {
        this.notAvailableToday = notAvailableToday;
    }

    public int getUnverified() {
        return unverified;
    }

    public void setUnverified(int unverified) {
        this.unverified = unverified;
    }

}
