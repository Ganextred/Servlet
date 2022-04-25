package org.example.luxuryhotel.entities;

import java.io.File;
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
            File f = new File(new File("").getAbsolutePath() + "/src/main/resources/static/img/room/" + path);
            if(f.exists() && f.isFile()) {
                image = path;
                break;
            }
        }
        if (image == null)
            return "defaultRoom.jpg";
        return image;
    }


}
