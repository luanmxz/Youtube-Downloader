package com.luanmxz.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luanmxz.record.request.UrlRecordRequest;
import com.luanmxz.services.DownloadService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController()
@RequestMapping("/api")
public class Controller {

    DownloadService downloadService = new DownloadService();

    @PostMapping("/convert")
    public ResponseEntity<Void> downloadVideoAndConvertToAudio(@RequestBody UrlRecordRequest urlRecord) {
        downloadService.downloadAndConvertToAudio(urlRecord.url());
        return ResponseEntity.ok().build();
    }
}
