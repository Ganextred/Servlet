package org.example.luxuryhotel.application.model.repository;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class ApartmentStatusRepository extends Repository {
    public ApartmentStatusRepository(Connection connection) {
        super(connection);
    }
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findApartmentStatusByApartmentId = "SELECT * FROM apartment_status as1 WHERE as1.apartment_id = ?";
    List<ApartmentStatus> findApartmentStatusByApartmentId(Apartment apartment){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentStatusByApartmentId);
            statement.setInt(1, apartment.getId());
            ResultSet rs = statement.executeQuery();
            List<ApartmentStatus> apartmentStatuses= new LinkedList<>();
            UserRepository userRepo = new UserRepository(connection);
            while (rs.next()){
                User user = userRepo.findUserById(rs.getInt("user_id"));
                apartmentStatuses.add(new ApartmentStatus(rs.getInt("id"),apartment, user,
                        rs.getDate("arrival_day").toLocalDate(),
                        rs.getDate("end_day").toLocalDate(),
                        rs.getTimestamp("pay_time_limit") == null?null:rs.getTimestamp("pay_time_limit").toLocalDateTime(),
                        Status.valueOf(rs.getString("status"))));
            }
            return  apartmentStatuses;
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    };
//    @Transactional
//    Integer deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime dateTime, LocalDate localDate);

//    List<ApartmentStatus> findApartmentStatusByStatusAndPayTimeLimitAfter(Status status, LocalDateTime localDateTime);
//    List<ApartmentStatus> findApartmentStatusByUserAndStatusAndPayTimeLimitAfter(User user,Status status, LocalDateTime localDateTime);
//    List<ApartmentStatus> findApartmentStatusByUserAndStatus(User user,Status status);


}
