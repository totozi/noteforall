package com.blog.noteforall.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.blog.noteforall.service.PostService;
import com.blog.noteforall.vo.PostVo;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RequestMapping("/post")
@Controller
public class PostController {

    // PostService를 자동 주입
    @Autowired
    private PostService postService;

    @GetMapping("/list")
    public String getListOfPosts(Model model, HttpServletRequest request) {

        
        List<PostVo> voList = postService.getList();

        for (PostVo postVo : voList) {
            System.out.println(postVo.getTitle());
        }

        model.addAttribute("voList", voList);

        return "list";
    }
    

    @GetMapping("/{no}")
    public String getPost(Model model, HttpServletRequest request, @PathVariable("no") int no) throws Exception {
                
        PostVo vo = postService.getPost(no);

        System.out.println(vo.toString());

        model.addAttribute("vo", vo);

        
        return "post";
    }
    

    @GetMapping("/edit")
    public String getEditor(Model model, HttpServletRequest  request) {

        HttpSession session = request.getSession(false);

        System.out.println("session : " + session);

        if (session != null && session.getAttribute("editor").equals("editor")) {
            System.out.println("session.name : " + session.getAttribute("editor"));

            return "editor";
        } else {
            return "login";
        }  
        
    }

    @PostMapping("/login")
    public String postMethodName(@RequestParam("password") String password, HttpServletRequest request) {

        System.out.println("password : " + password);

        boolean isAdmin = false;

        isAdmin = postService.isAdmin(password);

        System.out.println("isAdmin : " + isAdmin);

        if (isAdmin) {
            HttpSession session = request.getSession(true);
            session.setAttribute("editor", "editor");

            return "redirect:/post/EDIT";
        }
        else {
            return "index";
        }


    }
    

    @PostMapping("/post")
    public ResponseEntity<String> postPost(@RequestBody Map<String, Object> request) throws Exception {

        System.out.println("content : " + request.get("content").toString());

        System.out.println("jsonUploadedUrls : " + request.get("jsonUploadedUrls").toString());

        // 임시로 작성자는 IP로..
        PostVo post = new PostVo();

        tmpSetVo(request, post);

        String jsonUploadedUrls = request.get("jsonUploadedUrls").toString();

        postService.post(post, jsonUploadedUrls);
        
        return ResponseEntity.ok("Content received and saved successfully!");
    }


    @PostMapping("/fileupload")
    public ResponseEntity<?> fileupload(@RequestParam("file") MultipartFile file) {
        // 파일 정보 출력
        
        String fileName = file.getOriginalFilename();

        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        String UniqueFileName = UUID.randomUUID().toString() + "." + fileExtension;
        
        System.out.println("Received file: " + fileName);
        System.out.println("UniqueFileName : " + UniqueFileName);
        System.out.println("File size: " + file.getSize() + " bytes");
        System.out.println("Content type: " + file.getContentType());

        // 파일 저장 로직 추가 가능 (예: 로컬 저장소나 클라우드 업로드)
        try {
            // 예시: 파일 저장 경로 
            String uploadDir = "C:/Users/User/Desktop/uploads/";
            java.nio.file.Path path = java.nio.file.Paths.get(uploadDir + UniqueFileName);
            java.nio.file.Files.createDirectories(path.getParent());
            java.nio.file.Files.write(path, file.getBytes());
            UniqueFileName = "uploads/" + UniqueFileName;
            String fileUrl = UniqueFileName;

            System.out.println("fileUrl : " + fileUrl);
            
            return ResponseEntity.status(HttpStatus.OK).body(new FileUploadResponse(fileUrl));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("upload failed.");
        }
    }

    @GetMapping("uploads/{fileName}")
    public ResponseEntity<?> getImage(@PathVariable String fileName) throws IOException  {
        
        String UPLOAD_DIR = "C:/Users/User/Desktop/uploads/";

        // 파일 경로 설정
        File file = new File(UPLOAD_DIR + fileName);

        // 파일이 존재하지 않으면 404 오류 반환
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // 파일을 InputStream으로 읽음
        FileInputStream fileInputStream = new FileInputStream(file);

        // 파일의 MIME 타입을 확인 (여기서는 이미지 파일로 처리)
        // 파일의 MIME 타입을 확장자에 따라 자동으로 추론
        String mimeType = Files.probeContentType(file.toPath());
        
        // MIME 타입을 정확하게 설정하지 못한 경우 기본 MIME 타입 설정 (예: application/octet-stream)
        if (mimeType == null) {
            mimeType = "application/octet-stream";
        }

        // HTTP 응답에 파일을 스트리밍하는 방식으로 전송
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + file.getName())
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .body(new InputStreamResource(fileInputStream));
    }
    
    
    
    

        
    private void tmpSetVo(Map<String, Object> request, PostVo post) {

        // 클라이언트의 IP 주소 얻기
        // String clientIp = request.getRemoteAddr();
        
        // // X-Forwarded-For 헤더가 있으면, 실제 IP 주소를 구할 수 있는 경우
        // String forwardedIps = request.getHeader("X-Forwarded-For");
        // if (forwardedIps != null) {

        //     // 여러 개의 IP가 있을 수 있으므로 첫 번째 IP를 사용
        //     clientIp = forwardedIps.split(",")[0];

        // }

        String title = (String) request.get("title");
        String content = (String) request.get("content");

        post.setAuthor("default");
        post.setTitle(title);
        post.setContent(content);
    }

    // 응답에 포함될 파일 URL을 담기 위한 클래스
    public static class FileUploadResponse {
        private String fileUrl;

        public FileUploadResponse(String fileUrl) {
            this.fileUrl = fileUrl;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
    
}