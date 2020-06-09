package com.goodperson.layered.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/uploadform")
    public String uploadform() {
        return "uploadform";
    }

    @GetMapping("/uploadresult")
    public String uploadresult() {
        return "uploadresult";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(required = false) MultipartFile[] files, RedirectAttributes redirectAttributes) {
        final String viewName = "redirect:/uploadresult";
        if (files == null || files.length == 0) {
            redirectAttributes.addFlashAttribute("message", "Please upload at least one file.");
        } else {
            for (MultipartFile file : files) {
                try {
                    final File uploadDirectory = new File("d:/uploads");
                    if (!uploadDirectory.exists()) {
                        boolean mkSuccess = uploadDirectory.mkdirs();
                        if (!mkSuccess)
                            throw new Exception("Cannot create the upload directory.");
                    }
                    if (file.getSize() == 0) {
                        throw new Exception("Please upload at least one file.");
                    }
                    try (FileOutputStream fos = new FileOutputStream(
                            new File(uploadDirectory, file.getOriginalFilename()));
                            InputStream is = file.getInputStream();) {

                        logger.info("파일 이름: {}\n파일 크기: {}", file.getOriginalFilename(), file.getSize());

                        int readCount = 0;
                        byte[] buffer = new byte[256];
                        while ((readCount = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, readCount);
                        }
                    }
                } catch (Exception e) {
                    redirectAttributes.addFlashAttribute("message", "Fail to upload the files: " + e.getMessage());
                    return viewName;
                }
            }
            redirectAttributes.addFlashAttribute("message", "Uploaded successfully");
        }
        return viewName;
    }
}