package org.example.luxuryhotel.entities;


import java.time.LocalDate;
import java.time.LocalDateTime;

public class ApartmentStatus {

    private Integer id;

    private Apartment apartmentId;


    private  User user;

    private LocalDate arrivalDay;
    private LocalDate endDay;

    private Status status;

    private LocalDateTime payTimeLimit;




    public void clone(ApartmentStatus as){
        this.id = as.id;
        this.apartmentId = as.apartmentId;
        this.user = as.user;
        this.arrivalDay = as.arrivalDay;
        this.endDay = as.endDay;
        this.status = as.status;
        this.payTimeLimit = as.payTimeLimit;
    }

    public ApartmentStatus() {
    }

    public ApartmentStatus(Apartment apartmentId, User user, LocalDate arrivalDay, LocalDate endDay, LocalDateTime payTimeLimit,Status status) {
        this.apartmentId = apartmentId;
        this.user = user;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.payTimeLimit = payTimeLimit;
        this.status = status;
    }
    public ApartmentStatus(Integer id,Apartment apartmentId, User user, LocalDate arrivalDay, LocalDate endDay, LocalDateTime payTimeLimit,Status status) {
        this.id = id;
        this.apartmentId = apartmentId;
        this.user = user;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.payTimeLimit = payTimeLimit;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Apartment getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(Apartment apartmentId) {
        this.apartmentId = apartmentId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getArrivalDay() {
        return arrivalDay;
    }

    public void setArrivalDay(LocalDate arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPayTimeLimit() {
        return payTimeLimit;
    }

    public void setPayTimeLimit(LocalDateTime payTimeLimit) {
        this.payTimeLimit = payTimeLimit;
    }

    public Status isStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getTimeLimit() {
        return payTimeLimit;
    }

    public void setTimeLimit(LocalDateTime timeLimit) {
        this.payTimeLimit = timeLimit;
    }

}
