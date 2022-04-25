package org.example.luxuryhotel.application.model.repository;



import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
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
    private final static String findWthStatus = "SELECT DISTINCT * FROM apartment a LEFT JOIN apartment_status as2 " +
            "ON a.id = as2.apartment_id " +
            "AND (as2.end_day >= ? AND as2.arrival_day <= ?) AND (as2.pay_time_limit IS NULL OR as2.pay_time_limit >=NOW()) " +
            "WHERE (as2.status IS NULL AND ? = true) OR (as2.status LIKE 'BOOKED%' AND ? = true) OR " +
            "(as2.status = 'BOUGHT' AND ? = true) OR (as2.status = 'INACCESSIBLE' AND ? = true)";
    private final static String findImagesById = "SELECT * FROM apartment_image ai WHERE ai.apartment_id = ?";
    //public List<Apartment> findAll(Sort sort){};


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
            List<Apartment> apartments = new LinkedList<>();
            ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(connection);
            while (rs.next()){
                Apartment apartment = new Apartment(rs.getInt("id"), rs.getInt("beds"),
                        rs.getInt("clazz"), rs.getInt("price"));
                apartment.setApartmentStatuses(apartmentStatusRepo.findApartmentStatusByApartmentId(apartment));
                apartments.add(apartment);
                apartment.setImages(findImagesById(apartment.getId()));
            }
            return apartments;
        } catch (SQLException e) {
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
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

}
