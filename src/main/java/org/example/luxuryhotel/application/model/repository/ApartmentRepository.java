package org.example.luxuryhotel.application.model.repository;



import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.ApartmentStatus;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.data.Pageable;
import org.example.luxuryhotel.framework.data.Sort;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ApartmentRepository extends Repository {
    public ApartmentRepository(Connection connection) {
        super(connection);
    }
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findApartmentById = "SELECT * FROM apartment a WHERE a.id = ?";
    private final static String findAll = "SELECT * FROM apartment";
    private final static String findWthStatus = "SELECT DISTINCT * FROM apartment a LEFT JOIN apartment_status as2 " +
            "ON a.id = as2.apartment_id " +
            "AND (as2.end_day >= ? AND as2.arrival_day <= ?) AND (as2.pay_time_limit IS NULL OR as2.pay_time_limit >=NOW()) " +
            "WHERE (as2.status IS NULL AND ? = true) OR (as2.status LIKE 'BOOKED%' AND ? = true) OR " +
            "(as2.status = 'BOUGHT' AND ? = true) OR (as2.status = 'INACCESSIBLE' AND ? = true)";
    private final static String findImagesById = "SELECT * FROM apartment_image ai WHERE ai.apartment_id = ?";
    private final static String insertApartmentBCP ="INSERT INTO apartment (beds, clazz, price) " +
            "VALUES (?, ?, ?) RETURNING id";
    private final static String updateApartmentBCPI = "UPDATE apartment" +
            " SET beds = ?, clazz = ?, price = ? WHERE id = ?";
    private final static String deleteApartment = "DELETE FROM apartment WHERE id = ?";
    private final static String deleteImage= "DELETE FROM apartment_image WHERE apartment_id = ? AND images = ?";
    private final static String insertImage ="INSERT INTO apartment_image (apartment_id, images) " +
            "VALUES (?, ?)";



    private List<Apartment> extractApartments(ResultSet rs) throws SQLException {
        List<Apartment> apartments = new LinkedList<>();
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(connection);
        while (rs.next()){
            Apartment apartment = new Apartment(rs.getInt("id"), rs.getInt("beds"),
                    rs.getInt("clazz"), rs.getInt("price"));
            apartment.setApartmentStatuses(apartmentStatusRepo.findApartmentStatusByApartmentId(apartment));
            apartment.setImages(findImagesById(apartment.getId()));
            apartments.add(apartment);
        }
        return apartments;
    }

    public Apartment findApartmentById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(connection);
            if (rs.next()){
                Apartment apartment = new Apartment(rs.getInt("id"), rs.getInt("beds"),
                        rs.getInt("clazz"), rs.getInt("price"));
                apartment.setApartmentStatuses(apartmentStatusRepo.findApartmentStatusByApartmentId(apartment));
                apartment.setImages(findImagesById(apartment.getId()));
                return  apartment;
            }else return null;
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public List<Apartment> findAll(){
        try {
            PreparedStatement statement = connection.prepareStatement(findAll);
            ResultSet rs = statement.executeQuery();
            return extractApartments(rs);
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }


    public List<Apartment> findWithStatus (LocalDate arrivalDay, LocalDate endDay,
                                            Boolean needAvailable, Boolean needBooked,
                                            Boolean needBOUGHT, Boolean needInaccessible,
                                            Pageable pageable){
        try {
            PreparedStatement statement = connection.prepareStatement(pageable.upgradeStatement(findWthStatus));
            statement.setDate(1, Date.valueOf(arrivalDay));
            statement.setDate(2, Date.valueOf(endDay));
            statement.setBoolean(3, needAvailable);
            statement.setBoolean(4, needBooked);
            statement.setBoolean(5, needBOUGHT);
            statement.setBoolean(6, needInaccessible);
            ResultSet rs = statement.executeQuery();
            return extractApartments(rs);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    };

    public List<String> findImagesById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findImagesById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<String> images= new LinkedList<>();
            while (rs.next()){
                images.add(rs.getString("images"));
            }
            return  images;
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void insertApartment(Apartment apartment) {
        try{
            PreparedStatement statement = connection.prepareStatement(insertApartmentBCP);
            statement.setInt(1,apartment.getBeds());
            statement.setInt(2,apartment.getClazz().ordinal());
            statement.setInt(3,apartment.getPrice());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            apartment.setId(resultSet.getInt("id"));
            rewriteImages(apartment);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void updateApartment(Apartment apartment) {
        try{
            PreparedStatement statement = connection.prepareStatement(updateApartmentBCPI);
            statement.setInt(1,apartment.getBeds());
            statement.setInt(2,apartment.getClazz().ordinal());
            statement.setInt(3,apartment.getPrice());
            statement.setInt(4,apartment.getId());
            statement.execute();
            rewriteImages(apartment);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void deleteApartment(Apartment apartment) {
        try{
            apartment.setImages(null);
            rewriteImages(apartment);
            PreparedStatement statement = connection.prepareStatement(deleteApartment);
            statement.setInt(1,apartment.getId());
            statement.execute();
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void rewriteImages(Apartment apartment) throws  SQLException{
        if (apartment.getId() == null)
            throw new SQLException("apartment id cant be null while roles updating");
        if (apartment.getImages() == null) {
            List<String> images= new LinkedList<>();
            apartment.setImages(images);
        }
        List<String> images = findImagesById(apartment.getId());
        List<String> inserted = new LinkedList<>();
        for (String image : images){
            if (!apartment.getImages().contains(image)){
                PreparedStatement deleteImageStm = connection.prepareStatement(deleteImage);
                deleteImageStm.setInt(1,apartment.getId());
                deleteImageStm.setString(2,image);
                deleteImageStm.execute();
            }
            else inserted.add(image);
        }
        for (String image : apartment.getImages()){
            if (!inserted.contains(image)) {
                PreparedStatement insertImageStm = connection.prepareStatement(insertImage);
                insertImageStm.setInt(1, apartment.getId());
                insertImageStm.setString(2, image);
                insertImageStm.execute();
            }
        }
    }

}
