package com.blog.noteforall.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
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

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(Model model) {
        System.out.println("check");
        
        //String directoryPath = "C:/z_workspace/noteforall/src/main/resources/static/archive";  // 로컬 경로로 설정
        String directoryPath = "/root/noteforall/src/main/resources/static/archive";  // 로컬 경로로 설정
        
        try {
            // 디렉토리 내 폴더 목록 가져오기
            List<String> folderNames = getFolderNamesInDirectory(directoryPath);
            model.addAttribute("folderNames", folderNames);

            // 폴더 이름 출력
            for (String folderName : folderNames) {
                System.out.println(folderName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "index"; // src/main/resources/templates/index.html
    }


    // 로컬 파일 시스템에서 폴더 목록을 가져오는 메서드
    public List<String> getFolderNamesInDirectory(String directoryPath) throws IOException {
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