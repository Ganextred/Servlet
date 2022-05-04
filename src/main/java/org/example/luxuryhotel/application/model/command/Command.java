package org.example.luxuryhotel.application.model.command;

import java.io.Serializable;
import java.util.List;

public interface Command extends Serializable {
    public List<String> execute();
}
