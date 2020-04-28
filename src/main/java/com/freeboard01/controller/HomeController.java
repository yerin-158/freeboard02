package com.freeboard01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("msg", "Hello World!");
        return "index";
    }

    @GetMapping("/anotherpage")
    public ModelAndView buttonTest() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("msg", "다른 페이지로 이동하였습니다!!!");
        return modelAndView;
    }

}
