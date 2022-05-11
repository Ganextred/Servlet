package org.example.luxuryhotel.application.model.service;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;


public class TimeIntervalTest {

    @Test
    public void commonTimeIntervalTest(){
        ApartmentManager.TimeInterval ti = ApartmentManager.TimeInterval.getValidInterval("",null);
        assertEquals(LocalDate.now().toString(),LocalDate.now().toString());
    }
    @Test
    public void changingArrivalIntervalTest(){
        ApartmentManager.TimeInterval ti=
                ApartmentManager.TimeInterval.getValidInterval(LocalDate.now().toString(),LocalDate.now().toString());
        ti.setArrivalDay(LocalDate.now().plusDays(5));
        assertEquals(ti.getArrivalDay(),LocalDate.now());
    }
    @Test
    public void changingEndIntervalTest(){
        ApartmentManager.TimeInterval ti=
                ApartmentManager.TimeInterval.getValidInterval(LocalDate.now().toString(),LocalDate.now().toString());
        ti.setEndDay(LocalDate.now().minusDays(5));
        assertEquals(ti.getEndDay(),LocalDate.now());
    }
}
