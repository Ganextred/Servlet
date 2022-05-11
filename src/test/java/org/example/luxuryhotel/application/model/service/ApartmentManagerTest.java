package org.example.luxuryhotel.application.model.service;




import org.example.luxuryhotel.application.model.repository.ApartmentStatusRepository;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.ApartmentStatus;
import org.example.luxuryhotel.framework.AppContext;
import org.example.luxuryhotel.framework.data.Sort;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


public class ApartmentManagerTest {
    ApartmentManager apartmentManager = new ApartmentManager();
    private  static  String url = "jdbc:postgresql://localhost:5432/servlet";
    private  static  String user = "postgres";
    private  static  String password = "postgres";


    @BeforeAll
    static void setUp() throws IOException {
        try (InputStream fis = new FileInputStream("src/main/webapp/framework.properties")) {
            Properties property = new Properties();
            property.load(fis);
            AppContext.property = property;
        }
    }

    @Test
    public void getSort() {
        Sort sort = apartmentManager.getSort(null,null);
        assertNotEquals("", sort.getSqlOrder().toString());
        String[] op = new String[]{};
        String[] sp = new String[]{};
        sort = apartmentManager.getSort(sp, op);
        assertNotEquals("", sort.getSqlOrder().toString());
        op = new String[]{"true", "true"};
        sp = new String[]{"price"};
        sort = apartmentManager.getSort(sp,op);
        assertNotEquals("", sort.getSqlOrder().toString());
    }

    @Test
    public void getSortedApartments() throws SQLException {
        Map<String,Boolean> m = new HashMap<>();
        m.put ("BOUGHT", true);
        List<Apartment> apartments = apartmentManager.getSortedApartments(LocalDate.now().toString(),
                LocalDate.now().toString(),new String[]{"price"}, new String[]{"true"},
                false, false,true, false,1);
        for (int i = 1; i< apartments.size(); i++) {
            assertTrue(apartments.get(i).getPrice() >= apartments.get(i - 1).getPrice());

            Connection conn = DriverManager.getConnection(url, user, password);
            ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
            List<ApartmentStatus> apartmentStatuses = apartmentStatusRepo.findApartmentStatusByApartmentId(apartments.get(i));
            conn.close();

            boolean f = false;
            if (apartmentStatuses.size() != 0) {
                for (ApartmentStatus as : apartmentStatuses)
                    if (as.getStatus().toString().equals("BOUGHT"))
                        f = true;
            }
            assertTrue(f);
        }
    }


    @Test
    public void addModelSortParams() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = new Model(request, response);
        ApartmentManager apartmentManager = new ApartmentManager();
        apartmentManager.addModelSortParams(model, "2010-10-10", "2009-10-10",
                new String[]{"price", "price", "price"}, new String[]{"true"," true", "true"},
                true, true,true,true, 1);
        assertEquals(LocalDate.parse("2009-10-10"), model.attributes.get("arrivalDayD"));
        assertEquals(LocalDate.parse("2010-10-10"), model.attributes.get("endDayD"));
        assertArrayEquals(new String[]{"price", "price", "price"},(String[]) model.attributes.get("sortParamsD"));
        assertArrayEquals(new String[]{"true"," true", "true"},(String[]) model.attributes.get("orderParamsD"));
        assertEquals(true, model.attributes.get("AVAILABLE"));
        assertEquals(true, model.attributes.get("BOOKED"));
        assertEquals(true, model.attributes.get("BOUGHT"));
        assertEquals(true, model.attributes.get("INACCESSIBLE"));
        assertEquals(1, model.attributes.get("page"));
        assertTrue(model.attributes.containsKey("apartments"));
    }

    @Test
    public void addRedirectSortParams() {
        RedirectAttributes ra = new RedirectAttributes();
        ApartmentManager apartmentManager = new ApartmentManager();
        apartmentManager.addRedirectSortParams(ra, "2010-10-10", "2009-10-10",
                new String[]{"price", "price", "price"}, new String[]{"true"," true", "true"},
                true, true,true,true, 1);
        Assertions.assertEquals("2009-10-10", ra.attributes.get("arrivalDay").get(0));
        Assertions.assertEquals("2010-10-10", ra.attributes.get("endDay").get(0));
        Assertions.assertArrayEquals(new String[]{"price", "price", "price"}, ra.attributes.get("sortParams").toArray());
        Assertions.assertArrayEquals(new String[]{"true"," true", "true"}, ra.attributes.get("orderParams").toArray());
        Assertions.assertEquals("true", ra.attributes.get("AVAILABLE").get(0));
        Assertions.assertEquals("true", ra.attributes.get("BOOKED").get(0));
        Assertions.assertEquals("true", ra.attributes.get("BOUGHT").get(0));
        Assertions.assertEquals("true", ra.attributes.get("INACCESSIBLE").get(0));
        Assertions.assertEquals("1", ra.attributes.get("page").get(0));
    }
}