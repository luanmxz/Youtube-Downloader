package com.luanmxz.controllers;

import java.io.IOException;

import org.apache.catalina.util.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.core.io.Resource;
import java.nio.charset.StandardCharsets;

import com.luanmxz.services.DownloadService;

@Controller
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public ResponseEntity<String> downloadVideoAndConvertToAudio(@RequestParam("url") String url)
            throws IOException, InterruptedException {
        String videoTitle = downloadService.getVideoTitleFromUrl(url);
        String fileName = downloadService.downloadAndConvertToAudio(url);

        String encodedFileName = new URLEncoder().encode(fileName, StandardCharsets.UTF_8);

        return ResponseEntity.ok(String.format(
                """
                        <div class="bg-red-500 text-white rounded flex items-center justify-between overflow-hidden h-14 max-w-sm mx-auto">
                            <div class="flex items-center h-full">
                                <a href="/api/download/%s" download class="flex-shrink-0 w-8 h-8 flex items-center justify-center ml-2">⬇️</a>
                                <span class="flex-grow truncate px-3 text-xs">%s</span>
                            </div>
                            <div class="flex-shrink-0 flex space-x-1 pr-2">
                                <button class="w-6 h-6 flex items-center justify-center">📁</button>
                                <button class="w-6 h-6 flex items-center justify-center">🗑️</button>
                            </div>
                        </div>
                        """,
                encodedFileName, videoTitle));
    }

    @GetMapping("/download/{audioUrl}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String audioUrl)
            throws IOException, InterruptedException {
        Resource resource = downloadService.loadFileAsResource(audioUrl);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
