package org.soneech.springcourse.controller;

import org.soneech.springcourse.model.User;
import org.soneech.springcourse.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


@Controller
public class MainController {
    private final AdminService adminService;

    @Autowired
    public MainController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String mainPage() {
        return "index";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "/admin";
    }

    @GetMapping("/users")
    @ResponseBody
    public List<User> usersInfo() {
        return adminService.getAllUsers();
    }
}
