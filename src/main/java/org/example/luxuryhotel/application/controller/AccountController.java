package org.example.luxuryhotel.application.controller;


import org.example.luxuryhotel.application.model.command.CommandFactory;
import org.example.luxuryhotel.application.model.command.ConfirmRequestCommand;
import org.example.luxuryhotel.application.model.repository.ApartmentStatusRepository;
import org.example.luxuryhotel.application.model.repository.RequestRepository;
import org.example.luxuryhotel.application.model.service.UserManager;
import org.example.luxuryhotel.entities.*;
import org.example.luxuryhotel.framework.annotation.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.security.SecurityManager;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AccountController {

    @PreAuthorize("USER")
    @GetMapping(path = "/main/account")
    public String account(Model model) {
        User user = SecurityManager.getUserFromSession(model);
        Connection conn = ConnectionPool.getInstance().getConnection();
        ApartmentStatusRepository apartmentStatusRepo = new ApartmentStatusRepository(conn);
        RequestRepository requestRepo = new RequestRepository(conn);
        List<ApartmentStatus> bStatuses =
                apartmentStatusRepo.findApartmentStatusByUserAndStatusAndPayTimeLimitAfter(user, Status.BOOKED,LocalDateTime.now());
        List<ApartmentStatus> bgStatuses =
                apartmentStatusRepo.findApartmentStatusByUserAndStatus(user,Status.BOUGHT);
        List<Request> requests = requestRepo.findByUserIdAndAnswerStatusIsNotNull(user);
        model.addAttribute("user", user);
        model.addAttribute("bStatuses", bStatuses);
        model.addAttribute("bgStatuses", bgStatuses);
        model.addAttribute("requests", requests);
        ConnectionPool.getInstance().close(conn);
        return "/jsp/account.jsp";
    }

    @PreAuthorize("USER")
    @GetMapping(path = "/main/requestForm")
    public String requestForm(Model model, @RequestParam(name = "messages", required = false) String[] messages) {
        model.addAttribute ("messages", messages);
        return "/jsp/request.jsp";
    }

    @PreAuthorize("USER")
    @PostMapping(path = "/main/sendRequest")
    public String sendRequest (Model model,
                             @RequestParam(name = "arrivalDay", required=true ) String arrivalDay,
                              @RequestParam (name = "endDay", required=true) String endDay,
                              @RequestParam (name = "clazz", required=true) String clazz,
                              @RequestParam (name = "beds", required=true) Integer beds,
                               @RequestParam (name = "wishes", required=true) String wishes,
                              RedirectAttributes rA){
        rA.addAttributes("messages", new ArrayList<String>());
        User user = SecurityManager.getUserFromSession(model);
        List<String> messages = UserManager.sendRequest(arrivalDay,endDay,Clazz.valueOf(clazz),beds,wishes,user);
        if (messages.size() != 0) {
            rA.addAttributes("messages", messages);
            return ("redirect:/main/requestForm");
        } else{
            return "redirect:/main/account";
        }
    }

    @PreAuthorize("USER")
    @PostMapping(path = "/main/confirmRequest")
    public String confirmRequest(Model model, @RequestParam(name = "request") Integer request, RedirectAttributes rA){
        ConfirmRequestCommand command = CommandFactory.getConfirmRequestCommand(request);
        List<String> messages = command.execute();
        if (messages.size() != 0) {
            rA.addAttributes("messages",messages);
            return "redirect:/main/account";
        }
        return "redirect:/main/account";
    }
}
