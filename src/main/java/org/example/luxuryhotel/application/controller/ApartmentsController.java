
package org.example.luxuryhotel.application.controller;


import org.example.luxuryhotel.application.model.command.BookCommand;
import org.example.luxuryhotel.application.model.command.CommandFactory;
import org.example.luxuryhotel.application.model.repository.UserRepository;
import org.example.luxuryhotel.application.model.service.ApartmentManager;
import org.example.luxuryhotel.application.model.service.Validator;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.annotation.*;
import org.example.luxuryhotel.framework.security.SecurityManager;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.addAll;


@Controller
public class ApartmentsController {
//    CommandFactory commandFactory;



    @GetMapping(path = "/main/apartments")
    public String apartments (Model model, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                                 @RequestParam (name = "arrivalDay", required = false) String arrivalDay,
                                 @RequestParam (name = "endDay", required = false) String endDay,
                                 @RequestParam(name = "sortParams", required = false) String[] sortParams,
                                 @RequestParam(name = "orderParams", required = false) String[] orderParams,
                                 @RequestParam (name = "AVAILABLE", required = false, defaultValue = "true") Boolean available,
                                 @RequestParam (name = "BOOKED", required = false, defaultValue = "true") Boolean booked,
                                 @RequestParam (name = "BOUGHT", required = false, defaultValue = "true") Boolean bought,
                                 @RequestParam (name = "INACCESSIBLE", required = false, defaultValue = "true") Boolean inaccessible){
        ApartmentManager apartmentManager = new ApartmentManager();
        if (page <1)
            page =1;
        if (!Validator.checkSortParams(sortParams))
            sortParams = new String[]{"price", "price", "price"};
        if (!Validator.checkOrderParams(orderParams))
            orderParams =  new String[]{"true"," true", "true"};
        apartmentManager.addModelSortParams(model,arrivalDay, endDay, sortParams, orderParams, available,booked,bought,inaccessible, page);
        return "/jsp/apartments.jsp";
    }
    //разрешить для неафторизированных
    @PostMapping(path = "/main/apartments/applySort")
    public String applySort (@RequestParam (name = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam (name = "arrivalDay") String arrivalDay,
                             @RequestParam (name = "endDay") String endDay,
                             @RequestParam(name = "sortParams[]") String[] sortParams,
                             @RequestParam(name = "orderParams[]") String[] orderParams,
                             @RequestParam (name = "AVAILABLE", required = false, defaultValue = "false") Boolean available,
                             @RequestParam (name = "BOOKED", required = false, defaultValue = "false") Boolean booked,
                             @RequestParam (name = "BOUGHT", required = false, defaultValue = "false") Boolean bought,
                             @RequestParam (name = "INACCESSIBLE", required = false, defaultValue = "false") Boolean inaccessible,
                             RedirectAttributes rA,
                             Model model){
        ApartmentManager apartmentManager = new ApartmentManager();
        if (page <1)
            page =1;
        apartmentManager.addRedirectSortParams(rA,arrivalDay, endDay, sortParams, orderParams, available,booked,bought,inaccessible, page);
        return "redirect:/main/apartments";
    }

    @GetMapping(path = "/main/apartment")
    public String apartment(Model model,
                            @RequestParam(name = "messages", required = false) String[] messages,
                            @RequestParam(name = "apartment") Integer apartment){
        model.addAttribute("messages", messages);
        ApartmentManager apartmentManager = new ApartmentManager();
        apartmentManager.addModelApartmentParam(model,apartment);
        return "/jsp/apartment.jsp";
    }

    @PreAuthorize("USER")
    @PostMapping(path = "/main/apartment/book")
    public String applySort (Model model,
                             @RequestParam(name = "apartment") Integer apartment,
                             @RequestParam (name = "arrivalDay", required=true ) String arrivalDay,
                             @RequestParam (name = "endDay", required=true) String endDay,
                             RedirectAttributes rA){
        List<String> messages = new LinkedList<>();
        User user = SecurityManager.getUserFromSession(model);
        if (user == null)
            messages.add("accessDenied");
        else {
            BookCommand bc = CommandFactory.getBookCommand(arrivalDay, endDay, apartment, user);
            messages.addAll(bc.execute());
        }
        if (messages.size() != 0) {
            rA.addAttributes("messages", messages);
            rA.addAttribute("apartment", apartment.toString());
            return ("redirect:/main/apartment");
        } else{
            return "redirect:/main/apartments";
        }
    }



}
