package org.example.luxuryhotel.application.model.repository;

import org.example.luxuryhotel.application.model.service.ApartmentManager;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.framework.data.Pageable;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApartmentRepositoryTest {
    private  static  String url = "jdbc:postgresql://localhost:5432/servlet";
    private  static  String user = "postgres";
    private  static  String password = "postgres";

    @Test
    public void findWithStatus() throws SQLException {
        Connection conn = DriverManager.getConnection(url, user, password);
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        ApartmentManager apartmentManager = new ApartmentManager();
        Map<String,Boolean> m = new HashMap<>();
        final int size = 10;
        Pageable pageable = Pageable.of(0,size,apartmentManager.getSort(new String[]{"price"}, new String[]{"true"}));
        List<Apartment> apartments = apartmentRepo.findWithStatus(
                LocalDate.now(),LocalDate.now(),true,true,true,true, pageable);
        Assert.assertTrue(apartments.size() <= size);
        for (int i = 1; i< apartments.size(); i++)
            Assert.assertTrue(apartments.get(i).getPrice() >= apartments.get(i - 1).getPrice());
        conn.close();
    }


}