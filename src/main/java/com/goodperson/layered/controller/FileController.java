package com.goodperson.layered.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/gallery")
    public ModelAndView gallery(ModelAndView modelAndView) {
        final File uploadDirectory = new File("d:/uploads");
        modelAndView.setViewName("gallery");
        if (uploadDirectory.exists()) {
            String[] pictures = uploadDirectory.list();
            if (pictures != null) {
                String joinedPictures = Arrays.stream(uploadDirectory.list()).collect(Collectors.joining(","))
                        .toString();
                modelAndView.addObject("pictures", joinedPictures);
            }
        }
        return modelAndView;
    }

    @GetMapping("/uploadresult")
    public String uploadresult() {
        return "uploadresult";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam(required = false) MultipartFile[] files, RedirectAttributes redirectAttributes) {
        final String viewName = "redirect:/uploadresult";
        if (files == null) {
            redirectAttributes.addFlashAttribute("message", "적어도 한 개의 파일을 업로드해주세요.");
        } else {
            for (MultipartFile file : files) {
                try {
                    if (file.getSize() == 0) {
                        throw new Exception("적어도 한 개의 파일을 업로드해주세요. 파일의 크기가 0일 수도 있습니다.");
                    }

                    final String contentType = Files.probeContentType(Paths.get(file.getOriginalFilename()));
                    if (!contentType.startsWith("image/")) {
                        throw new Exception("이미지만 첨부 가능합니다.");
                    }

                    final File uploadDirectory = new File("d:/uploads");
                    if (!uploadDirectory.exists()) {
                        boolean mkSuccess = uploadDirectory.mkdirs();
                        if (!mkSuccess)
                            throw new Exception("업로드 폴더 생성 실패");
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
                    redirectAttributes.addFlashAttribute("message", "사진 업로드 실패: " + e.getMessage());
                    return viewName;
                }
            }
            redirectAttributes.addFlashAttribute("message", "사진 업로드 성공");
        }
        return viewName;
    }

    @GetMapping("/download/{fileName}")
    public void download(HttpServletResponse response, @PathVariable(required = false) String fileName) {
        File uploadDirectory = new File("d:/uploads");
        File srcFile = new File(uploadDirectory, fileName);
        try {
            final String contentType = Files.probeContentType(Paths.get(fileName));
            if (fileName == null || contentType == null || !srcFile.exists()) {
                return;
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
    }
}