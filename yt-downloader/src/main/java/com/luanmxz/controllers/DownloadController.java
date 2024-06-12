package com.luanmxz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.luanmxz.services.DownloadService;

@Controller
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public String downloadVideoAndConvertToAudio(@RequestParam("url") String url,
            RedirectAttributes redirectAttributes) {
        downloadService.downloadAndConvertToAudio(url);
        redirectAttributes.addFlashAttribute("message", "Conversion successful!");
        return "redirect:/api/result";
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/result")
    public ModelAndView result() {
        return new ModelAndView("result");
    }
}
