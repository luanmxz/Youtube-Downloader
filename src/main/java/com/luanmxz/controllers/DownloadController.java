package com.luanmxz.controllers;

import java.io.IOException;
import java.util.Base64;

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

import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
public class DownloadController {

    @Autowired
    DownloadService downloadService;

    @PostMapping("/convert")
    public ResponseEntity<String> downloadVideoAndConvertToAudio(@RequestParam("url") String url,
            @RequestParam("format") String audioFormat, @RequestParam("quality") String audioQuality)
            throws IOException, InterruptedException {

        AudioFile audioFileRequest = new AudioFile(url, audioFormat, audioQuality);

        String[] result = downloadService.downloadAndConvertToAudio(audioFileRequest);
        String fullPath = result[0];
        String fileName = result[1];

        String encodedFileName = Base64.getEncoder().encodeToString(fullPath.getBytes(StandardCharsets.UTF_8));

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
                encodedFileName, fileName));
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
    public ModelAndView index() {
        return new ModelAndView("index");
    }
}
