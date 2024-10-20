package com.luanmxz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> downloadVideoAndConvertToAudio(@RequestParam("url") String url) {
        downloadService.downloadAndConvertToAudio(url);
        return ResponseEntity.ok("<p>Download concluído com sucesso!</p>");
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
