package org.example.luxuryhotel.entities;

import org.example.luxuryhotel.application.model.repository.ApartmentRepository;
import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.framework.data.ConnectionPool;

import java.io.File;
import java.sql.Connection;
import java.util.List;


public class Apartment {
    private Integer id;
    private Integer price;
    private Integer beds;

    private Clazz clazz;

    private List<String> images;

    private List<ApartmentStatus> apartmentStatuses;

    public Apartment(int id, int beds, int clazz, int price) {
        this.id = id;
        this.beds = beds;
        this.clazz = Clazz.values()[clazz];
        this.price = price;
    }

    public List<ApartmentStatus> getApartmentStatuses() {
        return apartmentStatuses;
    }

    public void setApartmentStatuses(List<ApartmentStatus> apartmentStatuses) {
        this.apartmentStatuses = apartmentStatuses;
    }

    public Apartment() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPrice() {
        return price;
    }

    public Apartment setPrice(Integer price) {
        this.price = price; return this;
    }

    public Integer getBeds() {
        return beds;
    }

    public Apartment setBeds(Integer beds) {
        this.beds = beds;
        return this;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public Apartment setClazz(Clazz clazz) {
        this.clazz = clazz;
        return this;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getImage() {
        List<String> images = getImages();
        String image = null;
        for (String path : images){
            File f = new File("C:/Users/Professional/IdeaProjects/HotelServlet/HotelServlet" +
                    "/src/main/webapp/static/img/room/" + path);
            if(f.exists() && f.isFile()) {
                image = path;
                break;
            }
        }
        if (image == null)
            return "defaultRoom.jpg";
        return image;
    }

    public static Apartment ofId(Integer id){
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        Apartment apartment= apartmentRepo.findApartmentById(id);
        connectionPool.close(conn);
        return apartment;
    }


}
