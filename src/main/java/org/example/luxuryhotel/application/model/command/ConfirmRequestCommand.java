package org.example.luxuryhotel.application.model.command;


import org.example.luxuryhotel.application.model.service.ApartmentManager;

import java.util.List;

public class ConfirmRequestCommand extends Command {
    Integer request;
    List<String> status;

    public ConfirmRequestCommand(Integer request) {
        this.request = request;
    }

    @Override
    public List<String> execute() {
        ApartmentManager apartmentManager = new ApartmentManager();
        status = apartmentManager.confirmRequest(request);
        if (status.size() == 0)
            save();
        return status;
    }

    @Override
    protected void save() {

    }
}
