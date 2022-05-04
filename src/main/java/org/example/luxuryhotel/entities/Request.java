package org.example.luxuryhotel.entities;



import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.framework.data.ConnectionPool;

import java.sql.Connection;
import java.time.LocalDate;

public class Request {

    private Integer id;

    private  User userId;
    public ApartmentStatus answerStatus;
    private Integer beds;
    private LocalDate arrivalDay;
    private LocalDate endDay;
    private Clazz clazz;
    private String text;


    public ApartmentStatus getAnswerStatus() {
        return answerStatus;
    }

    public void setAnswerStatus(ApartmentStatus answerStatus) {
        this.answerStatus = answerStatus;
    }

    public Request() {
    }

    public Request(User userId, Integer beds, LocalDate arrivalDay, LocalDate endDay, Clazz clazz, String text) {
        this.userId = userId;
        this.beds = beds;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.clazz = clazz;
        this.text = text;
    }


    public Request(Integer id, User userId, ApartmentStatus answerStatus, Integer beds,
                   LocalDate arrivalDay, LocalDate endDay, Clazz clazz, String text) {
        this.id = id;
        this.userId = userId;
        this.answerStatus = answerStatus;
        this.beds = beds;
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.clazz = clazz;
        this.text = text;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public LocalDate getArrivalDay() {
        return arrivalDay;
    }

    public void setArrivalDay(LocalDate arrivalDay) {
        this.arrivalDay = arrivalDay;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getEndDay() {
        return endDay;
    }

    public void setEndDay(LocalDate endDay) {
        this.endDay = endDay;
    }

    public static Request ofId(Integer id){
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        RequestRepository requestRepo = new RequestRepository(conn);
        Request request = requestRepo.findRequestById(id);
        connectionPool.close(conn);
        return request;
    }

}
