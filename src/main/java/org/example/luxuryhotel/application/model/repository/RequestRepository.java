package org.example.luxuryhotel.application.model.repository;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;


import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class RequestRepository extends Repository{
    public RequestRepository(Connection connection) { super(connection); }
    private final static Logger logger = Logger.getLogger(UserRepository.class);
    private final static String findRequestById = "SELECT * FROM request r WHERE r.id = ?";
    private final static String findByUserAndAnswerStatusIsNotNull = "SELECT * FROM request as1 WHERE as1.user_id = ? " +
            "AND as1.answer_status_id IS NOT NULL";
    private final static String findByAnswerStatusIsNull = "SELECT * FROM request as1 WHERE " +
            "as1.answer_status_id IS NULL";
    private final static String insertRequestAdBCEdTU = "INSERT INTO request (arrival_day, beds, clazz, end_day, text, user_id) " +
            "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    private final static String updateRequestAnswerStatus = "UPDATE request" +
            " SET answer_status_id = ? WHERE id = ?";
    private final static String deleteRequest = "DELETE FROM request r WHERE r.id = ?";
    private final static String deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore =
            "DELETE FROM request r USING apartment_status a " +
                    "WHERE (r.answer_status_id = a.id AND a.pay_time_limit <= ?) OR (r.end_day <= ?)";

    private List<Request> extractRequests(ApartmentStatus as, ResultSet rs) throws SQLException {
        List<Request> requests= new LinkedList<>();
        UserRepository userRepo = new UserRepository(connection);
        while (rs.next()){
            User user = userRepo.findUserById(rs.getInt("user_id"));
            requests.add(new Request(rs.getInt("id"), user, as,
                    rs.getInt("beds"),
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    Clazz.valueOf(rs.getString("clazz")),
                    rs.getString("text")));
        }
        return requests;
    }
    private List<Request> extractRequests(User user, ResultSet rs) throws SQLException {
        List<Request> requests= new LinkedList<>();
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(connection);
        while (rs.next()){
            ApartmentStatus as =apartmentStatusRepo.findApartmentStatusById(rs.getInt("answer_status_id"));
            requests.add(new Request(rs.getInt("id"), user, as,
                    rs.getInt("beds"),
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    Clazz.valueOf(rs.getString("clazz")),
                    rs.getString("text")));
        }
        return requests;
    }
    private List<Request> extractRequests(ResultSet rs) throws SQLException {
        List<Request> requests= new LinkedList<>();
        UserRepository userRepo = new UserRepository(connection);
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(connection);
        while (rs.next()){
            User user = userRepo.findUserById(rs.getInt("user_id"));
            ApartmentStatus as =apartmentStatusRepo.findApartmentStatusById(rs.getInt("answer_status_id"));
            requests.add(new Request(rs.getInt("id"), user, as,
                    rs.getInt("beds"),
                    rs.getDate("arrival_day").toLocalDate(),
                    rs.getDate("end_day").toLocalDate(),
                    Clazz.valueOf(rs.getString("clazz")),
                    rs.getString("text")));
        }
        return requests;
    }

    public Request findRequestById(int id){
        try {
            PreparedStatement statement = connection.prepareStatement(findRequestById);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<Request> requests = extractRequests(rs);
            return (requests.size()>0)?requests.get(0):null;
        }catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void insertRequest(Request r) {
        try{
            PreparedStatement statement = connection.prepareStatement(insertRequestAdBCEdTU);
            statement.setDate(1, Date.valueOf(r.getArrivalDay()));
            statement.setInt(2,r.getBeds());
            statement.setString(3,r.getClazz().toString());
            statement.setDate(4,Date.valueOf(r.getEndDay()));
            statement.setString(5,r.getText());
            statement.setInt(6, r.getUserId().getId());
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            r.setId(resultSet.getInt("id"));
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void updateRequestAnswerStatus(Request request) {
        try{
            PreparedStatement statement = connection.prepareStatement(updateRequestAnswerStatus);
            statement.setInt(1,request.getAnswerStatus() == null?null:request.getAnswerStatus().getId());
            statement.setInt(2,request.getId());
            statement.execute();
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

//    @Transactional
//    Integer deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore(LocalDateTime dateTime, LocalDate localDate);
    public List<Request> findByAndAnswerStatusIsNull(){
        try {
            PreparedStatement statement = connection.prepareStatement(findByAnswerStatusIsNull);
            ResultSet rs = statement.executeQuery();
            ApartmentStatus as = null;
            return extractRequests(as,rs);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    };
    public List<Request> findByUserIdAndAnswerStatusIsNotNull(User user){
        try {
            PreparedStatement statement = connection.prepareStatement(findByUserAndAnswerStatusIsNotNull);
            statement.setInt(1,user.getId());
            ResultSet rs = statement.executeQuery();
            return extractRequests(user, rs);
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }
    }

    public void deleteRequest(Request request){
        try {
            PreparedStatement statement = connection.prepareStatement(deleteRequest);
            statement.setInt(1,request.getId());
            statement.execute();
        } catch (SQLException e) {
            ConnectionPool.getInstance().close(connection);
            e.printStackTrace();
            throw new RepositoryException(e);
        }

    }

    public void deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore(LocalDateTime payTimeLimit, LocalDate endDay) {
        try {
            PreparedStatement statement = connection.prepareStatement(deleteRequestByAnswerStatus_PayTimeLimitBeforeOrEndDayBefore);
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
