package com.luanmxz.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.Resource;
import java.nio.charset.StandardCharsets;

import com.luanmxz.record.request.AudioFile;
import com.luanmxz.services.DownloadService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public ModelAndView downloadVideoAndConvertToAudio(@RequestParam("url") String url,
            @RequestParam("format") String audioFormat, @RequestParam("quality") String audioQuality,
            HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        AudioFile audioFileRequest = new AudioFile(url, audioFormat, audioQuality);

        String[] result = downloadService.downloadAndConvertToAudio(audioFileRequest);
        String fullPath = result[0];
        String fileName = result[1];

        String encodedFileName = Base64.getEncoder().encodeToString(fullPath.getBytes(StandardCharsets.UTF_8));

        String itemId = UUID.randomUUID().toString();

        ModelAndView modelAndView = new ModelAndView("convertedItem");
        modelAndView.addObject("itemId", itemId);
        modelAndView.addObject("encodedFileName", encodedFileName);
        modelAndView.addObject("fileName", fileName);
        modelAndView.addObject("url", url);

        return modelAndView;
    }

    @GetMapping("/download/{audioUrl}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String audioUrl)
            throws IOException, InterruptedException {
        String decodedAudioUrl = new String(Base64.getDecoder().decode(audioUrl), StandardCharsets.UTF_8);
        Resource resource = downloadService.loadFileAsResource(decodedAudioUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("index");

        return modelAndView;
    }

    @GetMapping("/history")
    public ModelAndView getConvertedFilesHistory(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("itemHistory");

        return modelAndView;
    }
}
