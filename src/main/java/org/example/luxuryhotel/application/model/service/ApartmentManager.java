package org.example.luxuryhotel.application.model.service;




import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.model.repository.ApartmentRepository;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.data.Pageable;
import org.example.luxuryhotel.framework.data.Sort;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;


public class ApartmentManager {


    private final static Integer pageCapacity = 9;
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



//    @Transactional
//    public Pair<List<String>, ApartmentStatus> book(String arrivalDay, String endDay, User user, Apartment apartment, boolean forRequest) {
//        Status bookTypeStatus = forRequest?Status.BOOKEDREQUEST:Status.BOOKED;
//        List<String> status = valid.bookApartment(arrivalDay,endDay,apartment);
//        ApartmentStatus apartmentStatus = new ApartmentStatus();
//        if (status.size()==0){
//            apartmentStatus =
//                    new ApartmentStatus(apartment,user,LocalDate.parse(arrivalDay),LocalDate.parse(endDay),
//                           LocalDateTime.now().plusDays(2), bookTypeStatus);
//            apartmentStatusRepo.save(apartmentStatus);
//            sendBill(user.getEmail()+"dfsfs",apartment.getPrice());
//        }
//        return Pair.of(status, apartmentStatus);
//    }
//
//
//    @Transactional
//    public List<String> confirmBook(ApartmentStatus apartmentStatus) {
//        List<String> status = new ArrayList<>();
//        apartmentStatus.setStatus(Status.BOUGHT);
//        apartmentStatus.setPayTimeLimit(null);
//        apartmentStatusRepo.save(apartmentStatus);
//        return status;
//    }
//
//    @Transactional
//    public List<String> confirmRequest(Request request) {
//        List<String> status = new ArrayList<>();
//        request.getAnswerStatus().setStatus(Status.BOOKED);
//        request.getAnswerStatus().setPayTimeLimit(LocalDateTime.now().plusDays(2));
//        apartmentStatusRepo.save(request.getAnswerStatus());
//        requestRepo.delete(request);
//        return status;
//    }
//
//    @Transactional
//    public List<String> answerRequest(Request request, Apartment apartment) {
//        Pair<List<String>,ApartmentStatus> bookResult = book(request.getArrivalDay().toString(), request.getEndDay().toString(), request.getUserId(), apartment, true);
//        List<String> status = bookResult.getFirst();
//        ApartmentStatus apartmentStatus = bookResult.getSecond();
//        if (status.size()==0){
//                request.setAnswerStatus(apartmentStatus);
//                requestRepo.save(request);
//        }
//        return status;
//    }
//
//    @Transactional
//    public List<String> updateApartment(Apartment apartment, Integer price, Clazz clazz, Integer beds, MultipartFile file) {
//        List<String> status = valid.updateApartment(price,clazz,beds);
//        if (status.size()==0){
//            try {
//                if (!file.isEmpty()){
//                    file.transferTo(new File(new File("").getAbsolutePath()+ "/src/main/resources/static/img/room/room-"
//                            +apartment.getId()+'-'+apartment.getImages().size()));
//                    apartment.getImages().add("room-" + apartment.getId().toString()+'-'+apartment.getImages().size());
//                }
//                apartment.setBeds(beds);
//                apartment.setClazz(clazz);
//                apartment.setPrice(price);
//                apartmentRepo.save(apartment);
//            } catch (IOException | IllegalStateException e) {
//                status.add("problemsWithFile");
//            }
//        }
//        return status;
//    }
//
//    @Transactional
//    public Apartment newApartment(){
//        List<String> messages = new ArrayList<>();
//        Apartment apartment = new Apartment();
//        apartment.setPrice(9999).setBeds(1).setClazz(Clazz.LUX);
//        return apartmentRepo.save(apartment);
//    }
//
//    @Transactional
//    public void deleteApartment(Apartment apartment) {
//        apartmentRepo.delete(apartment);
//        List<String> images = apartment.getImages();
//        for (String image: images) {
//            File file = new File((new File("").getAbsolutePath() + "/src/main/resources/static/img/room/" + image));
//            if (!file.delete())
//                logger.warn("Room image wasn`t found in file system");
//        }
//    }
//
//    public void sendBill(String to, Integer price){}
//

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
