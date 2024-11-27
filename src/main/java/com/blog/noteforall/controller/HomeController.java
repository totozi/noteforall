package com.blog.noteforall.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        System.out.println("check");
        
        String directoryPath = "src/main/resources/static/archive"; // 경로를 자신의 프로젝트에 맞게 수정

        try {
            // 디렉토리 내 파일 목록 가져오기
            List<String> folderNames = getFolderNamesInDirectory(directoryPath);
            model.addAttribute("folderNames", folderNames);
            
            
            // 파일 이름 출력
            for (String folderName : folderNames) {
                System.out.println(folderName);
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return "index"; // src/main/resources/templates/index.html
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

public static List<String> getFolderNamesInDirectory(String directoryPath) throws IOException {
    List<String> folderNames = new ArrayList<>();
    
    // Paths.get을 사용하여 디렉토리 경로를 가져옴
    Path dirPath = Paths.get(directoryPath);

    // 디렉토리의 하위 폴더 목록만 가져옴
    try (Stream<Path> paths = Files.walk(dirPath, 1)) {  // 1 깊이까지만 탐색
        paths.filter(Files::isDirectory)  // 폴더만 필터링
             .filter(path -> !path.equals(dirPath))  // 자기 자신(현재 디렉토리)은 제외
             .forEach(path -> folderNames.add(path.getFileName().toString())); // 폴더 이름만 리스트에 추가
    }

    return folderNames;
}

}