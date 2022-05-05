package org.example.luxuryhotel.application.model.repository;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;

import java.sql.*;
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
    private final static String findApartmentStatusById = "SELECT * FROM apartment_status as1 WHERE as1.id = ?";
    private final static String findApartmentStatusByApartmentId = "SELECT * FROM apartment_status as1 WHERE as1.apartment_id = ?";
    private final static String insertApartmentStatusAdEdPSAU = "INSERT INTO apartment_status (arrival_day , end_day, pay_time_limit, status, apartment_id, user_id) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private final static String updateApartmentStatusPayTimeLimitAndStatus = "UPDATE apartment_status" +
            " SET pay_time_limit = ?, status = ? WHERE id = ?";
    private final static String findApartmentStatusByStatusAndPayTimeLimitAfter =
            "SELECT * FROM apartment_status as1 WHERE as1.status = ? AND pay_time_limit > ?";
    private final static String findApartmentStatusByUserAndStatusAndPayTimeLimitAfter =
            "SELECT * FROM apartment_status as1 WHERE as1.user_id = ? AND as1.status = ? AND pay_time_limit > ?";
    private final static String findApartmentStatusByUserAndStatus =
            "SELECT * FROM apartment_status as1 WHERE as1.user_id = ? AND as1.status = ?";
    private final static String deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore =
            "DELETE FROM apartment_status a " +
                    "WHERE a.pay_time_limit <= ? OR a.end_day <= ?";

    private List<ApartmentStatus> extractApartmentStatuses(ResultSet rs) throws SQLException {
        List<ApartmentStatus> apartmentStatuses= new LinkedList<>();
        ApartmentRepository apartmentRepo = new ApartmentRepository(connection);
        UserRepository userRepos = new UserRepository(connection);
        while (rs.next()){
            User user = userRepos.findUserById(rs.getInt("user_id"));
            Apartment apartment = apartmentRepo.findApartmentById(rs.getInt("apartment_id"));
            apartmentStatuses.add(new ApartmentStatus(rs.getInt("id"),apartment, user,
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    rs.getTimestamp("pay_time_limit") == null?null: rs.getTimestamp("pay_time_limit").toLocalDateTime(),
                    Status.valueOf(rs.getString("status"))));
        }
        return apartmentStatuses;
    }
    private List<ApartmentStatus> extractApartmentStatuses(User user, ResultSet rs) throws SQLException {
        List<ApartmentStatus> apartmentStatuses= new LinkedList<>();
        ApartmentRepository apartmentRepo = new ApartmentRepository(connection);
        while (rs.next()){
            Apartment apartment = apartmentRepo.findApartmentById(rs.getInt("apartment_id"));
            apartmentStatuses.add(new ApartmentStatus(rs.getInt("id"),apartment, user,
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    rs.getTimestamp("pay_time_limit") == null?null: rs.getTimestamp("pay_time_limit").toLocalDateTime(),
                    Status.valueOf(rs.getString("status"))));
        }
        return apartmentStatuses;
    }
    private List<ApartmentStatus> extractApartmentStatuses(Apartment apartment, ResultSet rs) throws SQLException {
        List<ApartmentStatus> apartmentStatuses= new LinkedList<>();
        UserRepository userRepos = new UserRepository(connection);
        while (rs.next()){
            User user = userRepos.findUserById(rs.getInt("user_id"));
            apartmentStatuses.add(new ApartmentStatus(rs.getInt("id"),apartment, user,
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    rs.getTimestamp("pay_time_limit") == null?null: rs.getTimestamp("pay_time_limit").toLocalDateTime(),
                    Status.valueOf(rs.getString("status"))));
        }
        return apartmentStatuses;
    }

    public ApartmentStatus findApartmentStatusById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentStatusById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<ApartmentStatus> apartmentStatuses= extractApartmentStatuses(rs);
            return  (apartmentStatuses.size()>0)?apartmentStatuses.get(0):null;
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public List<ApartmentStatus> findApartmentStatusByApartmentId(Apartment apartment){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentStatusByApartmentId);
            statement.setInt(1, apartment.getId());
            ResultSet rs = statement.executeQuery();
            return extractApartmentStatuses(apartment,rs);
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    };

    public void insertApartmentStatus(ApartmentStatus as) {
        try{
            PreparedStatement statement = connection.prepareStatement(insertApartmentStatusAdEdPSAU);
            statement.setDate(1,Date.valueOf(as.getArrivalDay()));
            statement.setDate(2,Date.valueOf(as.getEndDay()));
            statement.setTimestamp(3,Timestamp.valueOf(as.getPayTimeLimit()));
            statement.setString(4,as.getStatus().name());
            statement.setInt(5,as.getApartmentId().getId());
            statement.setInt(6,as.getUser().getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            as.setId(resultSet.getInt("id"));
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void updateApartmentStatusPayTimeLimitAndStatus(ApartmentStatus as) {
        try{
            PreparedStatement statement = connection.prepareStatement(updateApartmentStatusPayTimeLimitAndStatus);
            statement.setTimestamp(1,as.getPayTimeLimit() == null?null:Timestamp.valueOf(as.getPayTimeLimit()));
            statement.setString(2,as.getStatus().name());
            statement.setInt(3,as.getId());
            statement.execute();
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

   public List<ApartmentStatus> findApartmentStatusByUserAndStatusAndPayTimeLimitAfter(User user,Status status, LocalDateTime payTimeLimit){
       try {
           PreparedStatement statement = connection.prepareStatement(findApartmentStatusByUserAndStatusAndPayTimeLimitAfter);
           statement.setInt(1,user.getId());
           statement.setString(2,status.name());
           statement.setTimestamp(3,Timestamp.valueOf(payTimeLimit));
           ResultSet rs = statement.executeQuery();
           return extractApartmentStatuses(user, rs);
       } catch (SQLException e) {
           ConnectionPool.getInstance().close(connection);
           e.printStackTrace();
           throw new RepositoryException(e);
       }
   }

    ;
    public List<ApartmentStatus> findApartmentStatusByUserAndStatus(User user,Status status){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentStatusByUserAndStatus);
            statement.setInt(1,user.getId());
            statement.setString(2,status.name());
            ResultSet rs = statement.executeQuery();
            return extractApartmentStatuses(user, rs);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    };


    public List<ApartmentStatus> findApartmentStatusByStatusAndPayTimeLimitAfter(Status status, LocalDateTime payTimeLimit){
        try {
            PreparedStatement statement = connection.prepareStatement(findApartmentStatusByStatusAndPayTimeLimitAfter);
            statement.setString(1,status.name());
            statement.setTimestamp(2,Timestamp.valueOf(payTimeLimit));
            ResultSet rs = statement.executeQuery();
            return extractApartmentStatuses(rs);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore(LocalDateTime payTimeLimit, LocalDate endDay) {
        try {
            PreparedStatement statement = connection.prepareStatement(deleteApartmentStatusByPayTimeLimitBeforeOrEndDayBefore);
            statement.setTimestamp(1,Timestamp.valueOf(payTimeLimit));
            statement.setDate(2,Date.valueOf(endDay));
            statement.execute();
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

}
