package org.example.luxuryhotel.application.model.command;

import org.example.luxuryhotel.application.model.service.ApartmentManager;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.ApartmentStatus;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.Util.Pair;

import java.util.List;


public class BookCommand extends  Command{


    String arrivalDay; String endDay;
    User user;
    Integer apartmentId;
    List<String>status;
    ApartmentStatus resultAS;

    public BookCommand(String arrivalDay, String endDay, User user, Integer apartmentId) {
        this.arrivalDay = arrivalDay;
        this.endDay = endDay;
        this.user = user;
        this.apartmentId = apartmentId;
        resultAS = new ApartmentStatus();
    }

    @Override
    public List<String> execute() {
        ApartmentManager apartmentManager = new ApartmentManager();
        Pair<List<String>,ApartmentStatus> bookResult = apartmentManager.book(arrivalDay,endDay,user,apartmentId, false);
        status = bookResult.getFirst();
        resultAS = bookResult.getSecond();
        if (status.size() == 0)
            save();
        return status;
    }

    @Override
    protected void save() {

    }

}
