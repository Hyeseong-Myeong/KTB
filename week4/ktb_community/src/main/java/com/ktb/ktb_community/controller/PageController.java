package com.ktb.ktb_community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/terms")
    public String termsPage() {
        return "terms";
    }
}
