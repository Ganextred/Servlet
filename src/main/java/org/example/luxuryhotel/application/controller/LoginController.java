package org.example.luxuryhotel.application.controller;

import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.model.repository.UserRepository;
import org.example.luxuryhotel.application.model.service.Validator;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.framework.annotation.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.security.SecurityManager;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.util.List;

import static org.example.luxuryhotel.framework.security.PasswordEncoder.encode;

@Controller
public class LoginController {
    private Validator valid = new Validator();
    private final static Logger logger = Logger.getLogger(RegisterController.class);
    private ConnectionPool connectionPool = ConnectionPool.getInstance();

    @GetMapping(path = "/main/login")
    public String loginPage(@RequestParam(name = "message", required = false) String[] message, Model model){
        model.addAttribute("message", message);
        return "/jsp/login.jsp";
    }

    @PostMapping(path = "/main/login")
    public String login(@RequestParam(name = "username") String name,
                        @RequestParam(name = "password") String password,
                        RedirectAttributes rA,
                        Model model){
        User user = new User();
        user.setUsername(name).setPassword(password);
        List<String> s =valid.logUser(user);
        Connection connection = connectionPool.getConnection();
        UserRepository userRepo = new UserRepository(connection);
        User userFromDB = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());
        if (userFromDB == null || !userFromDB.getPassword().equals(encode(password)))
            s.add("Invalid_username");
        if (s.size() == 0){
            SecurityManager.addUserToSession(model, userFromDB);
            logger.debug("User id: "+userFromDB.getId()+" log in.");
            connectionPool.close(connection);
            return "redirect:/main/apartments";
        }else {
            rA.addAttributes("message",s);
            connectionPool.close(connection);
            return ("redirect:/main/login");
        }
    }

    @PostMapping(path = "/main/logout")
    public String logout(Model model){
        SecurityManager.logout(model);
        return "redirect:/main/apartments";
    }
}
