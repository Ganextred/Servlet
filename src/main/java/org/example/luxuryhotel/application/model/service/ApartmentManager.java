package org.example.luxuryhotel.application.model.service;




import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.model.repository.ApartmentRepository;
import org.example.luxuryhotel.application.model.repository.ApartmentStatusRepository;
import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.Util.Pair;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.data.Pageable;
import org.example.luxuryhotel.framework.data.Sort;
import org.example.luxuryhotel.framework.exaptions.RepositoryException;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;


import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ApartmentManager {


    private final static Integer pageCapacity = 9;
    private final static String path = "C:/Users/Professional/IdeaProjects/HotelServlet/HotelServlet";
    private final static Logger logger = Logger.getLogger(ApartmentManager.class);


    public void addModelSortParams(Model model, String arrivalDay, String endDay,
                                          String[] sortParams, String[] orderParams, Boolean available,Boolean booked,
                                          Boolean bought,Boolean inaccessible, Integer page){
        model.addAttribute("page", page);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        model.addAttribute("arrivalDayD", timeInterval.arrivalDay);
        model.addAttribute("endDayD", timeInterval.endDay);
        model.addAttribute("sortParamsD", sortParams);
        model.addAttribute("orderParamsD",orderParams);
        model.addAttribute("AVAILABLE", available);
        model.addAttribute("BOOKED", booked);
        model.addAttribute("BOUGHT", bought);
        model.addAttribute("INACCESSIBLE", inaccessible);
        model.addAttribute("apartments", getSortedApartments(arrivalDay,endDay,sortParams,orderParams,
                                available,booked,bought,inaccessible,page));
    }

    public void addRedirectSortParams(RedirectAttributes rA, String arrivalDay, String endDay,
                                      String[] sortParams, String[] orderParams, Boolean available,Boolean booked,Boolean bought,Boolean inaccessible, Integer page){
        rA.addAttribute("page", page.toString());
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
//        List<Apartment> apartments= getSortedApartments(arrivalDay, endDay, sortParams, orderParams, status, page);
//        rA.addFlashAttribute("apartments", apartments);
        rA.addAttribute("arrivalDay", timeInterval.arrivalDay.toString());
        rA.addAttribute("endDay", timeInterval.endDay.toString());
        rA.addAttributes("sortParams", Arrays.asList(sortParams));
        rA.addAttributes("orderParams", Arrays.asList(orderParams));
        rA.addAttribute("AVAILABLE", available.toString());
        rA.addAttribute("BOOKED", booked.toString());
        rA.addAttribute("BOUGHT", bought.toString());
        rA.addAttribute("INACCESSIBLE", inaccessible.toString());
    }


    public List<Apartment> getSortedApartments(String arrivalDay, String endDay, String[] sortParams, String[] orderParams,
                                               Boolean available,Boolean booked,Boolean bought,Boolean inaccessible, Integer page){
        Sort sortBySortParams = getSort(sortParams, orderParams);
        Pageable pageable = Pageable.of(page-1,pageCapacity,sortBySortParams);
        TimeInterval timeInterval = TimeInterval.getValidInterval(arrivalDay, endDay);
        ApartmentRepository apartmentRepo = new ApartmentRepository(ConnectionPool.getInstance().getConnection());
        List<Apartment> apartments= apartmentRepo.
                findWithStatus(timeInterval.arrivalDay, timeInterval.endDay,
                                available, booked,
                                bought, inaccessible,
                                pageable);
        ConnectionPool.getInstance().close(apartmentRepo.getConnection());
        return apartments;
    }


    public Sort getSort (String[] sortParams, String[] orderParams){
        if (sortParams == null || sortParams.length == 0)
            sortParams = new String[]{"price"};
        if (orderParams == null || orderParams.length == 0)
            orderParams = new String[]{"true"};
        if (orderParams.length != sortParams.length){
            sortParams = new String[]{"price"};
            orderParams = new String[]{" "};
        }
        Sort sortBySortParams = orderParams[0].equals("true")?Sort.by(Sort.Direction.ASC, sortParams[0]):Sort.by(Sort.Direction.DESC, sortParams[0]);
        for (int i = 1; i<orderParams.length; i++)
            if (orderParams[i].equals("true"))
                sortBySortParams = sortBySortParams.and(Sort.by(Sort.Direction.ASC, sortParams[i]));
            else sortBySortParams = sortBySortParams.and(Sort.by(Sort.Direction.DESC, sortParams[i]));
        return sortBySortParams;
    }

    public void addModelApartmentParam(Model model, Integer apartmentId) {
        Connection conn = ConnectionPool.getInstance().getConnection();
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        model.addAttribute("apartment", apartmentRepo.findApartmentById(apartmentId));
    }

    public Pair<List<String>, ApartmentStatus> book(String arrivalDay, String endDay, User user, Integer apartmentId, boolean forRequest) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        Pair<List<String>, ApartmentStatus> r = book(arrivalDay, endDay, user, apartmentId, forRequest, conn);
        connectionPool.commit(conn);
        connectionPool.close(conn);
        return r;
    }

    public Pair<List<String>, ApartmentStatus> book(String arrivalDay, String endDay, User user, Integer apartmentId, boolean forRequest, Connection conn) {
        Status bookTypeStatus = forRequest?Status.BOOKEDREQUEST:Status.BOOKED;
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        Apartment apartment = apartmentRepo.findApartmentById(apartmentId);
        Validator valid = new Validator();
        List<String> status = valid.bookApartment(arrivalDay,endDay,apartment, user);
        ApartmentStatus apartmentStatus = new ApartmentStatus();
        if (status.size()==0){
            apartmentStatus =
                    new ApartmentStatus(apartment,user,LocalDate.parse(arrivalDay),LocalDate.parse(endDay),
                            LocalDateTime.now().plusDays(2), bookTypeStatus);
            ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
            try{
                apartmentStatusRepo.insertApartmentStatus(apartmentStatus);
            }catch (RepositoryException e){
                ConnectionPool.getInstance().close(conn);
                status.add ("weCouldn'tFindSomething");
                return Pair.of(status, apartmentStatus);
            }
        }
        return Pair.of(status, apartmentStatus);
    }


    public List<String> confirmBook(Integer apartmentStatusId) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);

        List<String> status = new ArrayList<>();
        ApartmentStatus apartmentStatus = new ApartmentStatus();
        apartmentStatus.setStatus(Status.BOUGHT);
        apartmentStatus.setPayTimeLimit(null);
        apartmentStatus.setId(apartmentStatusId);
        try {
            apartmentStatusRepo.updateApartmentStatusPayTimeLimitAndStatus(apartmentStatus);
        }catch (RepositoryException e){
            connectionPool.close(conn);
            status.add ("weCouldn'tFindSomething");
            return status;
        }
        connectionPool.commit(conn);
        connectionPool.close(conn);
        return status;
    }

    public List<String> confirmRequest(Integer requestId) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        RequestRepository requestRepo = new RequestRepository(conn);
        Request request = requestRepo.findRequestById(requestId);

        List<String> status = new ArrayList<>();
        request.getAnswerStatus().setStatus(Status.BOOKED);
        request.getAnswerStatus().setPayTimeLimit(LocalDateTime.now().plusDays(2));
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
        apartmentStatusRepo.updateApartmentStatusPayTimeLimitAndStatus(request.getAnswerStatus());
        requestRepo.deleteRequest(request);
        connectionPool.commit(conn);
        connectionPool.close(conn);
        return status;
    }

    public List<String> answerRequest(Integer requestId, Integer apartmentId) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        RequestRepository requestRepo = new RequestRepository(conn);
        Request request = requestRepo.findRequestById(requestId);

        Pair<List<String>,ApartmentStatus> bookResult = book(request.getArrivalDay().toString(), request.getEndDay().toString(), request.getUserId(), apartmentId, true, conn);
        List<String> status = bookResult.getFirst();
        ApartmentStatus apartmentStatus = bookResult.getSecond();
        if (status.size()==0){
                request.setAnswerStatus(apartmentStatus);
                requestRepo.updateRequestAnswerStatus(request);
                connectionPool.commit(conn);
        }
        connectionPool.close(conn);
        return status;
    }

    public List<String> updateApartment(Integer apartmentId, Integer price, Clazz clazz, Integer beds,Model model) {
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        Connection conn = connectionPool.getConnection();
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        Apartment apartment= apartmentRepo.findApartmentById(apartmentId);

        Validator valid = new Validator();
        List<String> status = valid.updateApartment(price,clazz,beds);
        if (status.size()==0){
            try {

                Part part = model.request.getPart("image");
                if (!(part.getSize() == 0)) {
                    String fileName = path+"/src/main/webapp/static/img/room/room-"
                        +apartment.getId()+'-'+apartment.getImages().size() + ".jpeg";
                    part.write(fileName);
                    apartment.getImages().add("room-" + apartment.getId().toString()+'-'+apartment.getImages().size() + ".jpeg");
                }


                apartment.setBeds(beds);
                apartment.setClazz(clazz);
                apartment.setPrice(price);
                apartmentRepo.updateApartment(apartment);
                connectionPool.commit(conn);
            } catch (IOException | IllegalStateException | ServletException e) {
                connectionPool.close(conn);
                e.printStackTrace();
                status.add("problemsWithFile");
            }
        }
        connectionPool.close(conn);
        return status;
    }

    public Apartment newApartment(){
        Apartment apartment = new Apartment();
        apartment.setPrice(9999).setBeds(1).setClazz(Clazz.LUX);
        Connection conn = ConnectionPool.getInstance().getConnection();
        ApartmentRepository apartmentRepo =new ApartmentRepository(conn);
        apartmentRepo.insertApartment(apartment);
        ConnectionPool.getInstance().commit(conn);
        return apartment;
    }

    public void deleteApartment(Integer apartmentId) {
        Connection conn = ConnectionPool.getInstance().getConnection();
        ApartmentRepository apartmentRepo =new ApartmentRepository(conn);
        Apartment apartment = apartmentRepo.findApartmentById(apartmentId);
        List<String> images = apartment.getImages();
        for (String image: images) {
            File file = new File((path+"/src/main/webapp/static/img/room/" + image));
            if (!file.delete())
                logger.warn("Room image wasn`t found in file system");
        }
        apartmentRepo.deleteApartment(apartment);
        ConnectionPool.getInstance().commit(conn);
        ConnectionPool.getInstance().close(conn);
    }


    public static class TimeInterval{
        private LocalDate arrivalDay;
        private LocalDate endDay;

        private TimeInterval(String arrivalDayStr, String endDayStr) {
            this.arrivalDay = LocalDate.parse(arrivalDayStr);
            this.endDay = LocalDate.parse(endDayStr);
        }

        public LocalDate getArrivalDay() {
            return arrivalDay;
        }

        public void setArrivalDay(LocalDate arrivalDay) {
            TimeInterval ti = TimeInterval.getValidInterval(arrivalDay.toString(), endDay.toString());
            this.arrivalDay = ti.getArrivalDay();
            this.endDay = (ti.getEndDay());
        }

        public LocalDate getEndDay() {
            return endDay;
        }

        public void setEndDay(LocalDate endDay) {
            TimeInterval ti = TimeInterval.getValidInterval(arrivalDay.toString(), endDay.toString());
            this.arrivalDay = ti.getArrivalDay();
            this.endDay = (ti.getEndDay());
        }

        public static TimeInterval getValidInterval(String arrivalDayStr, String endDayStr){
            if (arrivalDayStr == null || arrivalDayStr.length() == 0){
                arrivalDayStr = LocalDate.now().toString();
                logger.warn("ApartmentManager.makeDateValid got null arrival day");
            }
            if (endDayStr == null || endDayStr.length() == 0){
                endDayStr = LocalDate.now().toString();
                logger.warn("ApartmentManager.makeDateValid got null end day");
            }
            try{
                LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
                LocalDate endDateT = LocalDate.parse(endDayStr);
                if (arrivalDateT.compareTo(LocalDate.now()) < 0)
                    arrivalDayStr = LocalDate.now().toString();
                if (endDateT.compareTo(LocalDate.now()) < 0)
                    endDayStr = LocalDate.now().toString();
                if (arrivalDateT.compareTo(endDateT)>0){
                    arrivalDayStr = endDateT.toString();
                    endDayStr = arrivalDateT.toString();
                }
            }catch (DateTimeParseException e){
                logger.warn("ApartmentManager.makeDateValid got unparsed date", e);
                arrivalDayStr = LocalDate.now().toString();
                endDayStr = LocalDate.now().toString();
            }
            return new TimeInterval(arrivalDayStr, endDayStr);
        }
    }

}
