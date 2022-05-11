package org.example.luxuryhotel.application.model.command;



import org.example.luxuryhotel.application.model.service.ApartmentManager;

import java.util.List;

public class ConfirmBookCommand extends Command{
    Integer apartmentStatusId;
    List<String>status;

    public ConfirmBookCommand(Integer apartmentStatusId) {
        this.apartmentStatusId = apartmentStatusId;
    }

    @Override
    public List<String> execute() {
        ApartmentManager apartmentManager = new ApartmentManager();
        List<String>status = apartmentManager.confirmBook(apartmentStatusId);
        if (status.size() == 0)
            save();
        return status;
    }

    @Override
    protected void save() {

    }

}
