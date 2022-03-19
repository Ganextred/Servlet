package org.example.luxuryhotel.application.controller;


import org.apache.log4j.Logger;
import org.example.luxuryhotel.application.entities.Role;
import org.example.luxuryhotel.application.entities.User;
import org.example.luxuryhotel.application.model.service.Validator;
import org.example.luxuryhotel.application.model.repository.UserRepository;
import org.example.luxuryhotel.framework.annotation.Controller;
import org.example.luxuryhotel.framework.annotation.GetMapping;
import org.example.luxuryhotel.framework.annotation.PostMapping;
import org.example.luxuryhotel.framework.annotation.RequestParam;
import org.example.luxuryhotel.framework.data.ConnectionPool;
import org.example.luxuryhotel.framework.web.Model;


import java.sql.Connection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.example.luxuryhotel.framework.security.PasswordEncoder.encode;


@Controller
public class RegisterController {
    Validator valid = new Validator();
    private final static Logger logger = Logger.getLogger(RegisterController.class);
    UserRepository userRepo = new UserRepository();
    ConnectionPool connectionPool = ConnectionPool.getInstance();

    @GetMapping(path = "/main/register")
    public String register (@RequestParam(name = "message", required = false) String[] message, Model model){
        model.addAttribute("message", message);
        for (String m : message)
            System.out.println(m);
        return "/jsp/register.jsp";
    }

    @PostMapping(path = "/main/register")
    public String addUser (@RequestParam(name = "username") String name,
                           @RequestParam(name = "email") String email,
                           @RequestParam(name = "password") String password,
                           Model model){
        User user = new User();
        user.setUsername(name).setEmail(email).setPassword(password);
        Connection connection = connectionPool.getConnection();
        List<String> s =valid.regUser(user);
        User userFromDb = userRepo.findByUsernameOrEmail(user.getUsername(), user.getEmail(), connection);
        if (userFromDb != null) {
            s.add("User_already_exist");
        }
        if (s.size()!=0) {
            model.addAttribute("message",s);
            connectionPool.close(connection);
            return ("redirect:/main/register");
        }
        Set<Role> roles= new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles).setPassword(encode(user.getPassword()));
        try {
            userRepo.save(user, connection);
            logger.info("new user was registered, username: " + user.getUsername());
        } catch (Exception e){
            s.add("User_already_exist");
            model.addAttribute("message",s);
            connectionPool.close(connection);
            e.printStackTrace();
            return "redirect:/main/register";
        }
        connectionPool.commit(connection);
        connectionPool.close(connection);
        return ("redirect:/main/login");
    }

}
