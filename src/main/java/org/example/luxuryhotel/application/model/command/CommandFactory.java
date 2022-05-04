package org.example.luxuryhotel.application.model.command;


import org.example.luxuryhotel.entities.ApartmentStatus;
import org.example.luxuryhotel.entities.User;



public class CommandFactory {

    public static BookCommand getBookCommand (String arrivalDay, String endDay, Integer apartmentId, User user){
        return new BookCommand(arrivalDay, endDay, user, apartmentId);
    }
    public static ConfirmBookCommand getConfirmBookCommand (Integer apartmentStatusId){
        return new ConfirmBookCommand(apartmentStatusId);
    }
    public static AnswerRequestCommand getAnswerRequestCommand (Integer request, Integer apartment){
        return new AnswerRequestCommand(request, apartment);
    }
    public static ConfirmRequestCommand getConfirmRequestCommand (Integer request){
        return new ConfirmRequestCommand(request);
    }

}
