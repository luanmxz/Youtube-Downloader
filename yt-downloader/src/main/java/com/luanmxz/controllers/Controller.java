package com.luanmxz.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.luanmxz.record.request.UrlRecordRequest;
import com.luanmxz.services.DownloadService;

@RestController()
@RequestMapping("/api")
public class Controller {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public ResponseEntity<Void> downloadVideoAndConvertToAudio(@RequestBody UrlRecordRequest urlRecord) {
        downloadService.downloadAndConvertToAudio(urlRecord.url());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
