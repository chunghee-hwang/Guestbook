package com.goodperson.layered.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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

    @GetMapping("/download/{fileName}")
    public String download(HttpServletResponse response, @PathVariable(required = false) String fileName) {
        File uploadDirectory = new File("d:/uploads");
        File srcFile = new File(uploadDirectory, fileName);
        try {
            final String contentType = Files.probeContentType(Paths.get(fileName));
            if (fileName == null || contentType == null || !srcFile.exists()) {
                return "redirect:/uploadform";
            }

            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
            response.setHeader("Content-Transfer-Encoding", "binary");
            response.setHeader("Content-Type", contentType);
            response.setHeader("Content-Length", String.valueOf(srcFile.length()));
            response.setHeader("Pragma", "no-cache;");
            response.setHeader("Expires", "-1;");
            try (FileInputStream fis = new FileInputStream(srcFile); OutputStream out = response.getOutputStream();) {
                int readCount = 0;
                byte[] buffer = new byte[256];
                while ((readCount = fis.read(buffer)) != -1) {
                    out.write(buffer, 0, readCount);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("File to download file.");
        }
        return "redirect:/uploadform";
    }
}