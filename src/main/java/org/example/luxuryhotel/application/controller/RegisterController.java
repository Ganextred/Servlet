package org.example.luxuryhotel.application.controller;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.entities.Role;
import org.example.luxuryhotel.entities.User;
import org.example.luxuryhotel.application.model.service.Validator;
import org.example.luxuryhotel.application.model.repository.UserRepository;
import org.example.luxuryhotel.framework.annotation.*;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.web.Model;
import org.example.luxuryhotel.framework.web.RedirectAttributes;


import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.luxuryhotel.framework.security.PasswordEncoder.encode;


@Controller
public class RegisterController {
    Validator valid = new Validator();
    private final static Logger logger = Logger.getLogger(RegisterController.class);
    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @GetMapping(path = "/main/register")
    public String register (@RequestParam(name = "message", required = false) String[] message, Model model){
        model.addAttribute("message", message);
        return "/jsp/register.jsp";
    }

    @PostMapping(path = "/main/register")
    public String addUser (@RequestParam(name = "username") String name,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           RedirectAttributes rA){
        User user = new User();
        user.setUsername(name).setEmail(email).setPassword(password);
        List<String> s =valid.regUser(user);
        Connection connection = connectionPool.getConnection();
        UserRepository userRepo = new UserRepository(connection);
//        User userFromDb = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail());
//        if (userFromDb != null) {
//            s.add("User_already_exist");
//        }
//        if (s.size()!=0) {
//            rA.addAttributes("message",s);
//            connectionPool.close(connection);
//            return ("redirect:/main/register");
//        }
        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles).setPassword(encode(user.getPassword()));
        try {
            userRepo.insertUser(user);
            logger.info("new user was registered, username: " + user.getUsername());
        } catch (Exception e){
            s.add("User_already_exist");
            rA.addAttributes("message",s);
            connectionPool.close(connection);
            e.printStackTrace();
            return "redirect:/main/register";
        }
        connectionPool.commit(connection);
        connectionPool.close(connection);
        return ("redirect:/main/login");
    }

}
