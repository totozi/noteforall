package com.blog.noteforall.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ArchiveController {

    @GetMapping("/archive")
    public String archive(Model model, @RequestParam("folder") String folderName) {

        String directoryPath  = "/root/noteforall/src/main/resources/static/archive/" + folderName;
        //String directoryPath = "C:/z_workspace/noteforall/src/main/resources/static/archive/" + folderName;  // 로컬 경로로 설정
        
        try {
            // 디렉토리 내 파일 목록 가져오기
            List<String> fileNames = getFileNamesInDirectory(directoryPath);
            model.addAttribute("fileNames", fileNames);

            // 폴더 이름 출력
            for (String fileName : fileNames) {
                System.out.println(fileName);

            }
            model.addAttribute("fileNames", fileNames);
            model.addAttribute("folderName", folderName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        return "archive"; // src/main/resources/templates/archive.html
    }
    

    public static List<String> getFileNamesInDirectory(String directoryPath) throws IOException {
    List<String> fileNames = new ArrayList<>();
    
    // Paths.get을 사용하여 디렉토리 경로를 가져옴
    Path dirPath = Paths.get(directoryPath);

    // 디렉토리의 파일 목록을 스트림으로 가져와서 파일 이름만 추출
    try (Stream<Path> paths = Files.walk(dirPath)) {
        paths.filter(Files::isRegularFile) // 파일만 필터링
             .forEach(path -> fileNames.add(path.getFileName().toString())); // 파일 이름을 리스트에 추가
    }

    return fileNames;
    }
}
