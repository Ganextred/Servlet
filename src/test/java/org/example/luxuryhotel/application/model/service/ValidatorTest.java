package org.example.luxuryhotel.application.model.service;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

public class ValidatorTest {
    Validator valid = new Validator();
    @Test
    public void nullEmail() {
        Assert.assertFalse(valid.email(null));
    }
    @Test
    public void validEmail() {
        Assert.assertTrue(valid.email("gregor@gmail.com"));
        Assert.assertTrue(valid.email("gregordsadasfasfasfa@gmail.com"));
    }
    @Test
    public void invalidEmail() {
        Assert.assertFalse(valid.email("gregor@gmail.commm"));
        Assert.assertFalse(valid.email("gregor@gmail..com"));
        Assert.assertFalse(valid.email("gregor@гмаил.com"));
    }

    @Test
    public void nullUsername() {
        Assert.assertFalse(valid.userName(null));
    }
    @Test
    public void validUsername() {
        Assert.assertTrue(valid.userName("Jhon"));
        Assert.assertTrue(valid.userName("123_HjonF_fd"));
    }
    @Test
    public void invalidUsername() {
        Assert.assertFalse(valid.email("TooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongNameTooLongName"));
        Assert.assertFalse(valid.email("ДЖОН"));
        Assert.assertFalse(valid.email("S"));
        Assert.assertFalse(valid.email("Jhon."));
        Assert.assertFalse(valid.email("Jhon$"));
    }

    @Test
    public void updateApartmentTst(){
        List<String> messages = valid.updateApartment(null,null, null);
        Assert.assertTrue(messages.containsAll(List.of("choseClazz", "incorrectBeds", "incorrectPrice")));
    }

    @Test
    public void sendRequest() {
        List<String> messages = valid.sendRequest(LocalDate.now().toString(),LocalDate.now().toString(),null,null, null);
        Assert.assertTrue(messages.containsAll(List.of("choseClazz", "incorrectBeds", "writeWishes")));
    }


    @Test
    public void timeInterval() {
        List<String> messages = valid.timeInterval(LocalDate.now().toString(), LocalDate.now().toString());
        Assert.assertEquals(messages.size(), 0);
        messages = valid.timeInterval(LocalDate.now().plusDays(2).toString(), LocalDate.now().toString());
        Assert.assertTrue(messages.contains("wrongDayOrder"));
    }
}