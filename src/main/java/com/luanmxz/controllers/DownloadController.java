package com.luanmxz.controllers;

import java.io.IOException;
import java.util.Base64;
import java.util.UUID;
import java.net.URLEncoder;

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
    public ResponseEntity<String> downloadVideoAndConvertToAudio(@RequestParam("url") String url,
            @RequestParam("format") String audioFormat, @RequestParam("quality") String audioQuality,
            HttpServletRequest request, HttpServletResponse response) throws IOException, InterruptedException {

        AudioFile audioFileRequest = new AudioFile(url, audioFormat, audioQuality);

        String urlFormatted = URLEncoder.encode(url, StandardCharsets.UTF_8);

        String[] result = downloadService.downloadAndConvertToAudio(audioFileRequest);
        String fullPath = result[0];
        String fileName = result[1];

        String encodedFileName = Base64.getEncoder().encodeToString(fullPath.getBytes(StandardCharsets.UTF_8));

        String itemId = UUID.randomUUID().toString();

        return ResponseEntity.ok(String.format(
                """
                        <div class="bg-red-500 text-white rounded flex items-center justify-between overflow-hidden h-14 max-w-sm mx-auto" id="item_%s">
                            <div class="flex items-center h-full">
                                <a href="/api/download/%s" download class="flex-shrink-0 w-8 h-8 flex items-center justify-center ml-2 text-3xl hover:scale-105">⬇️</a>
                                <span id="videoName" class="flex-grow truncate px-3 text-sm max-w-[54%%]">%s</span>
                                 <div class="flex-shrink-0 flex space-x-1 pr-2">
                                    <button class="w-6 h-6 flex items-center justify-center hover:scale-105" onclick="copyUrlValue('%s')">
                                        <input id="hidden_url" type="text" value="%s" class="hidden">
                                        📁
                                    </button>
                                    <button class="w-6 h-6 flex items-center justify-center hover:scale-105"
                                        onclick="removeItem('item_%s')"
                                        hx-target="#item_%s"
                                        hx-swap="outerHTML">
                                        🗑️
                                    </button>
                                </div>
                            </div>
                        </div>
                        """,
                itemId, encodedFileName, fileName, urlFormatted, urlFormatted, itemId, itemId));
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
    public ResponseEntity<String> getConvertedFilesHistory(HttpServletRequest request) {
        return ResponseEntity.ok(
                """
                        <script>
                            let convertedFiles = [];
                            for (let i = 0; i < localStorage.length; i++) {
                                let key = localStorage.key(i);
                                if (key.startsWith('item_')) {
                                    let item = JSON.parse(localStorage.getItem(key));
                                    convertedFiles.push(`
                                        <div class="bg-red-500 text-white rounded flex items-center justify-between overflow-hidden h-14 max-w-sm mx-auto" id="${key}">
                                            <div class="flex items-center h-full">
                                                <button type="button" disabled class="flex-shrink-0 w-8 h-8 flex items-center justify-center ml-2 text-3xl">⬇️</button>
                                                <span id="videoName" class="flex-grow truncate px-3 text-sm max-w-[54%]">${item.fileName}</span>
                                                <div class="flex-shrink-0 flex space-x-1 pr-2">
                                                    <button class="w-6 h-6 flex items-center justify-center hover:scale-105" onclick="copyUrlValue(${item.url})">
                                                    <input id="hidden_url" type="text" value="${item.url}" class="hidden">
                                                    📁
                                                    </button>
                                                    <button class="w-6 h-6 flex items-center justify-center hover:scale-105"
                                                            onclick="removeItem('${key}')"
                                                            hx-target="#${key}"
                                                            hx-swap="outerHTML">🗑️</button>
                                                </div>
                                            </div>
                                        </div>
                                    `);
                                }
                            }
                            document.getElementById('result').innerHTML = convertedFiles.join('');
                        </script>
                        """);
    }
}
