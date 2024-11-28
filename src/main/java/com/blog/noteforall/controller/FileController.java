package com.blog.noteforall.controller;

import java.io.File;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FileController {
    
    @GetMapping("/file")
    public String archive(Model model, 
    @RequestParam("fileName") String fileName, 
    @RequestParam("folderName") String folderName) {

        String fileUrl = "/fileurl?fileName=" + fileName + "&folderName=" + folderName;
        model.addAttribute("fileUrl", fileUrl);
        model.addAttribute("folderName", folderName);
        
        return "file"; 
    }


    @GetMapping("/fileurl")
    public ResponseEntity<FileSystemResource> getPdfFile(
        @RequestParam("fileName") String fileName,
        @RequestParam("folderName") String folderName) {

        //String filePath = "C:/z_workspace/noteforall/src/main/resources/static/archive";    
        String filePath = "/root/noteforall/src/main/resources/static/archive"; // 서버 루트 경로의 PDF 파일
        //filePath = filePath + folderName;
        filePath = filePath +  "/" + fileName;

        System.out.println("filePath : " + filePath);
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build(); // 파일이 없을 경우 404 반환
        }

        FileSystemResource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=sample.pdf") // 브라우저에서 표시
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
    

}
