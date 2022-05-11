package org.example.luxuryhotel.application.model.command;

import java.io.Serializable;
import java.util.List;

public abstract class Command implements Serializable {
    public abstract List<String> execute();
    protected abstract void save();
}
