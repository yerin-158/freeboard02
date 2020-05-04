package com.freeboard01.controller;

import com.freeboard01.api.user.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/board")
    public String board(HttpSession httpSession, Model model) {
        UserForm loginUser = (UserForm) httpSession.getAttribute("USER");
        if(loginUser != null ) {
            model.addAttribute("accountId", loginUser.getAccountId());
        }
        return "board";
    }

    @GetMapping("/join")
    public String join() { return "join"; }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute("USER");
        return "index";
    }

}
