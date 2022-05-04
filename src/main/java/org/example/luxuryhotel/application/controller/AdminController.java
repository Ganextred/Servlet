package org.example.luxuryhotel.application.controller;



import org.example.luxuryhotel.application.model.command.AnswerRequestCommand;
import org.example.luxuryhotel.application.model.command.CommandFactory;
import org.example.luxuryhotel.application.model.command.ConfirmBookCommand;
import org.example.luxuryhotel.application.model.repository.ApartmentRepository;
import org.example.luxuryhotel.application.model.repository.ApartmentStatusRepository;
import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.application.model.service.ApartmentManager;
import org.example.luxuryhotel.application.model.service.Validator;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.annotation.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;


@Controller
public class AdminController {

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/main/admin/adminPanel")
    public String adminPanel(Model model){
        Connection conn = ConnectionPool.getInstance().getConnection();
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
        RequestRepository requestRepo = new RequestRepository(conn);
        ApartmentRepository apartmentRepo = new ApartmentRepository(conn);
        List<ApartmentStatus> bStatuses = apartmentStatusRepo.findApartmentStatusByStatusAndPayTimeLimitAfter(Status.BOOKED, LocalDateTime.now());
        bStatuses.addAll(apartmentStatusRepo.findApartmentStatusByStatusAndPayTimeLimitAfter(Status.BOOKEDREQUEST, LocalDateTime.now()));
        List<Request> requests = requestRepo.findByAndAnswerStatusIsNull();
        model.addAttribute("bStatuses", bStatuses);
        model.addAttribute("requests", requests);
        model.addAttribute("apartments", apartmentRepo.findAll());
        model.addAttribute("existingRoles", Role.values());
        ConnectionPool.getInstance().close(conn);
        return "/jsp/adminPanel.jsp";
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/confirmStatus")
    public String confirmBook(@RequestParam(name = "status") Integer apartmentStatusId){
        ConfirmBookCommand command= CommandFactory.getConfirmBookCommand(apartmentStatusId);
        command.execute();
        return"redirect:/admin/adminPanel";
    }


    @PreAuthorize("ADMIN")
    @GetMapping(path = "/main/admin/seeRequest")
    public String editUser(Model model, @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
                           @RequestParam (name = "arrivalDay", required = false) String arrivalDay,
                           @RequestParam (name = "endDay", required = false) String endDay,
                           @RequestParam(name = "sortParams", required = false) String[] sortParams,
                           @RequestParam(name = "orderParams", required = false) String[] orderParams,
                           @RequestParam (name = "AVAILABLE", required = false, defaultValue = "true") Boolean available,
                           @RequestParam (name = "BOOKED", required = false, defaultValue = "true") Boolean booked,
                           @RequestParam (name = "BOUGHT", required = false, defaultValue = "true") Boolean bought,
                           @RequestParam (name = "INACCESSIBLE", required = false, defaultValue = "true") Boolean inaccessible,
                           @RequestParam(name = "request") Integer requestId){
        Request request = Request.ofId(requestId);
        ApartmentManager apartmentManager = new ApartmentManager();
        if (page <1)
            page =1;
        if (!Validator.checkSortParams(sortParams))
            sortParams = new String[]{"price", "clazz", "beds"};
        if (!Validator.checkOrderParams(orderParams))
            orderParams =  new String[]{"true"," true", "true"};
        apartmentManager.addModelSortParams(model,arrivalDay, endDay, sortParams, orderParams, available,booked,bought,inaccessible, page);
        model.addAttribute("request",request);
        return "/jsp/seeRequest.jsp";
    }


    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/seeRequest/applySort")
    public String applySort (@RequestParam (name = "page", required = false, defaultValue = "1") Integer page,
                             @RequestParam(name="request") Integer requestId,
                             @RequestParam(name = "sortParams[]") String[] sortParams,
                             @RequestParam(name = "orderParams[]") String[] orderParams,
                             RedirectAttributes rA){
        Request request = Request.ofId(requestId);
        ApartmentManager apartmentManager = new ApartmentManager();
        if (page <1)
            page =1;
        rA.addAttribute("request",requestId.toString());
        apartmentManager.addRedirectSortParams(rA,request.getArrivalDay().toString(), request.getEndDay().toString(), sortParams, orderParams, true,false,false,false, page);
        return "redirect:/main/admin/seeRequest";
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/answerRequest")
    public String answerRequest (@RequestParam(name="request") Integer requestId,
                                 @RequestParam(name="apartment") Integer apartmentId,
                                 RedirectAttributes ra){
        AnswerRequestCommand command = CommandFactory.getAnswerRequestCommand(requestId, apartmentId);
        List<String> messages = command.execute();
        if (messages.size() != 0) {
            ra.addAttribute("request", requestId.toString());
            return "redirect:/main/admin/seeRequest";
        }
        return "redirect:/main/admin/adminPanel";
    }

    @PreAuthorize("ADMIN")
    @GetMapping(path = "/main/admin/editApartment")
    public String apartment(Model model,
                            @RequestParam(name = "messages", required = false) String[] messages,
                            @RequestParam(name = "apartment") Integer apartmentId){
        model.addAttribute("messages", messages);
        Apartment apartment = Apartment.ofId(apartmentId);
        if (apartment == null)
            return "redirect:/main/admin/adminPanel";
        else {
            model.addAttribute("apartment", apartment);
            return "/jsp/editApartment.jsp";
        }
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/editApartment/save/")
    public String applySort ( Model model,
                              @RequestParam(name = "apartment") Integer apartment,
                              @RequestParam (name = "price") Integer price,
                              @RequestParam (name = "beds") Integer beds,
                              @RequestParam (name = "clazz") String clazz,
//                              @RequestParam("image") MultipartFile file,
                              RedirectAttributes rA){
        rA.addAttributes("messages", new LinkedList<String>());
        ApartmentManager apartmentManager = new ApartmentManager();
        List<String> messages = apartmentManager.updateApartment(apartment, price,Clazz.valueOf(clazz),beds, model);
        if (messages.size() != 0) {
            rA.addAttributes("messages", messages);
        }
        rA.addAttribute("apartment", apartment.toString());
        return ("redirect:/main/admin/editApartment");
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/newApartment")
    public String applySort (RedirectAttributes rA){
        ApartmentManager apartmentManager = new ApartmentManager();
        Apartment apartment = apartmentManager.newApartment();
        rA.addAttribute("apartment",apartment.getId().toString());
        return ("redirect:/main/admin/editApartment");
    }

    @PreAuthorize("ADMIN")
    @PostMapping(path = "/main/admin/editApartment/delete")
    public String applySort (@RequestParam(name = "apartment") Integer apartment){
        ApartmentManager  apartmentManager= new ApartmentManager();
        apartmentManager.deleteApartment(apartment);
        return ("redirect:/main/admin/adminPanel");
    }
}