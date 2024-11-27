package com.blog.noteforall.controller;

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

        String fileUrl = "/archive/" + fileName;
        model.addAttribute("fileUrl", fileUrl);
        model.addAttribute("folderName", folderName);
        
        return "file"; 
    }

}
