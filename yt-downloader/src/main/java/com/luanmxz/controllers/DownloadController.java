package com.luanmxz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.luanmxz.services.DownloadService;

@Controller
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public String downloadVideoAndConvertToAudio(@RequestParam("url") String url) {

        downloadService.downloadAndConvertToAudio(url);
        return "redirect:/api/resultSuccess";

    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/resultSuccess")
    public ModelAndView resultSuccess() {
        return new ModelAndView("resultSuccess");
    }

    @GetMapping("/resultFailed")
    public ModelAndView resultFailed() {
        return new ModelAndView("resultFailed");
    }
}
