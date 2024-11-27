package cn.tm.aibro.controller;

import cn.tm.aibro.service.PdfStoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.net.http.HttpResponse.ResponseInfo;
import java.util.List;

@Controller
@RequestMapping("/api/v1/pdf")
@RequiredArgsConstructor
public class PdfUploadController {
    private final PdfStoreService pdfStoreService;

    @PostMapping("/upload")
    public ResponseInfo upload(@RequestParam MultipartFile file) {
        return pdfStoreService.saveSource(file);
    }

    @PostMapping("/upload/files")
    public void upload(@RequestParam List<MultipartFile> files) {
        files.forEach(pdfStoreService::saveSource);
    }
}
