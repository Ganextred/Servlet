package org.example.luxuryhotel.application.model.command;



import org.example.luxuryhotel.application.model.service.ApartmentManager;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.Request;

import java.util.List;

public class AnswerRequestCommand extends Command {
    Integer request;
    Integer apartment;
    List<String> status;


    public AnswerRequestCommand(Integer requestId, Integer apartmentId) {
        this.request = requestId;
        this.apartment = apartmentId;
    }

    @Override
    public List<String> execute() {
        ApartmentManager apartmentManager = new ApartmentManager();
        status = apartmentManager.answerRequest(request, apartment);
        if (status.size() == 0)
            save();
        return status;
    }

    @Override
    protected void save() {

    }
}
