package org.example.luxuryhotel.entities;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ApartmentTest {
    @Test
    public void getImage() {
        Apartment a = new Apartment();
        List<String> inputImages = new ArrayList<>();
        inputImages.add("randomSymbolsnfdsjgsfhgjs");
        a.setImages(inputImages);
        Assert.assertEquals(a.getImage(), "defaultRoom.jpg");
    }
}