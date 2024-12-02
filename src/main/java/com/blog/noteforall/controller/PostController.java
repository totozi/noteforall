package com.blog.noteforall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.blog.noteforall.service.PostService;
import com.blog.noteforall.vo.PostVo;

import jakarta.servlet.http.HttpServletRequest;

@RequestMapping("/post")
@Controller
public class PostController {

    // PostService를 자동 주입
    @Autowired
    private PostService postService;

    @GetMapping("GET")
    public String getPost(Model model, HttpServletRequest request) throws Exception {
        
        int no = Integer.parseInt(request.getParameter("no"));

        
        PostVo vo = postService.getPost(no);

        System.out.println(vo.toString());

        model.addAttribute("vo", vo);

        
        return "post";
    }
    

    @GetMapping("/EDIT")
    public String getEditor(Model model) {

        System.out.println("getEditor");
        

        return "editor"; // src/main/resources/templates/post.html
    }

    @PostMapping("/POST")
    public ResponseEntity<String> postPost(HttpServletRequest request) throws Exception {

        System.out.println("content : " + request.getParameter("content"));

        // 임시로 작성자는 IP로..
        PostVo post = new PostVo();
        tmpSetVo(request, post);

        postService.post(post);
        
        return ResponseEntity.ok("Content received and saved successfully!");
    }
        
    private void tmpSetVo(HttpServletRequest request, PostVo post) {

        // 클라이언트의 IP 주소 얻기
        String clientIp = request.getRemoteAddr();
        
        // X-Forwarded-For 헤더가 있으면, 실제 IP 주소를 구할 수 있는 경우
        String forwardedIps = request.getHeader("X-Forwarded-For");
        if (forwardedIps != null) {

            // 여러 개의 IP가 있을 수 있으므로 첫 번째 IP를 사용
            clientIp = forwardedIps.split(",")[0];

        }

        String title = request.getParameter("title");
        String content = request.getParameter("content");

        post.setAuthor(clientIp);
        post.setContent(clientIp);
        post.setTitle(title);
        post.setContent(content);
    }
    
}