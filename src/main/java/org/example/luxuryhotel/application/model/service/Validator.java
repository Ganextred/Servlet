package org.example.luxuryhotel.application.model.service;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Apartment;
import org.example.luxuryhotel.entities.ApartmentStatus;
import org.example.luxuryhotel.entities.Clazz;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.application.model.repository.UserRepository;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class Validator {
    private final static Logger logger = Logger.getLogger(Validator.class);

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    private static final Pattern VALID_USERNAME_REGEX =
            Pattern.compile("^[a-zA-Z0-9_]{4,30}$");
    private static final Pattern VALID_PASSWORD_REGEX =
            Pattern.compile("(?=^.{6,}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");

    public static boolean email(String email){return (email!=null && email.matches(VALID_EMAIL_ADDRESS_REGEX.pattern()));}
    public static boolean userName(String un){return (un!=null && un.matches(VALID_USERNAME_REGEX.pattern()));}
    public static boolean password(String pass){return (pass!=null && pass.matches(VALID_PASSWORD_REGEX.pattern()));}

    public List<String> regUser(User user){
        List<String> s=new ArrayList<>();
        if (!userName(user.getUsername())) {
            s.add("incorrectUsername");
        }
        if (!email(user.getEmail())) {
            s.add("Incorrect_email");
        }
        if (!password(user.getPassword())){
            s.add("Password_problem");
        }
        return s;
    }
    public List<String> logUser(User user){
        List<String> s=new ArrayList<>();
        if (!userName(user.getUsername())) {
            s.add("incorrectUsername");
        }
        if (!password(user.getPassword())){
            s.add("Password_problem");
        }
        return s;
    }

    public static boolean checkSortParams(String[] sortParams) {
        if (sortParams == null || sortParams.length != 3)
            return false;
        for (int i = 0; i<3; i++)
            if (!(sortParams[i].equals("price") || sortParams[i].equals("beds") || sortParams[i].equals("clazz")))
                return false;
        return true;
    }
    public static boolean checkOrderParams(String[] orderParams) {
        if (orderParams == null || orderParams.length != 3)
            return false;
        for (int i = 0; i<3; i++)
            if (!(orderParams[i].equals("true") || orderParams[i].equals("false")))
                return false;
        return true;
    }


    public List<String> bookApartment(String arrivalDayStr, String endDayStr, Apartment apartment, User user){
        List<String> messages = new LinkedList<>();
        if (user == null)
            messages.add("accessDenied");
        messages.addAll(timeInterval(arrivalDayStr, endDayStr));
        if (apartment == null){
            messages.add("weCouldn'tFindSomething");
            logger.warn("validator got null apartment");
        }else
        if (messages.size() == 0){
            try{
                LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
                LocalDate endDateT = LocalDate.parse(endDayStr);
                for (ApartmentStatus as: apartment.getApartmentStatuses()){
                    if ((as.getArrivalDay().compareTo(endDateT) <= 0 && as.getEndDay().compareTo(arrivalDateT)>=0 &&
                            (as.getPayTimeLimit() == null || as.getPayTimeLimit().compareTo(LocalDateTime.now()) >=0))
                            || arrivalDateT.compareTo(LocalDate.now()) <0
                            || endDateT.compareTo(LocalDate.now()) <0){
                        messages.add("apartmentNotAvailableOnTime");
                        break;
                    }
                }
            }catch (NullPointerException e){
                messages.add("weCouldn'tFindSomething");
                logger.error("validator got nullPointer");
            }
        }
        return messages;
    }

    public List<String> updateApartment(Integer price, Clazz clazz, Integer beds){
        List<String> messages = new ArrayList<>();
        if(clazz == null)
            messages.add("choseClazz");
        if(beds == null || beds < 0)
            messages.add("incorrectBeds");
        if(price == null || price< 0)
            messages.add("incorrectPrice");
        return messages;
    }

    public List<String> sendRequest(String arrivalDay, String endDay, Clazz clazz, Integer beds, String wishes){
        List<String> messages = new ArrayList<>();
        if(clazz == null)
            messages.add("choseClazz");
        if(beds == null || beds < 0)
            messages.add("incorrectBeds");
        if(wishes == null)
            messages.add("writeWishes");
        messages.addAll(timeInterval(arrivalDay,endDay));
        return messages;
    }

    public List<String> timeInterval(String arrivalDayStr, String endDayStr){
        List<String> messages = new LinkedList<>();
        try {
            LocalDate arrivalDateT = LocalDate.parse(arrivalDayStr);
            LocalDate endDateT = LocalDate.parse(endDayStr);
            if (arrivalDateT.compareTo(endDateT) > 0) {
                messages.add("wrongDayOrder");
            }
            if (arrivalDateT.compareTo(LocalDate.now()) < 0) {
                messages.add("tooEarlyArrival");
                logger.debug("Arrival day cant bee yesterday");
            }
        }catch (DateTimeParseException e){
            messages.add("weCantRecognizeDay");
            logger.warn("Validator got unparsed arrival or end day");
        }catch (NullPointerException e){
            messages.add("chooseArrivalOrEndDay");
            logger.warn("Validator got null(or 0length) arrival or end day");
        }
        return messages;
    }
}
