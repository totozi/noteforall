package com.blog.noteforall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.noteforall.dao.PostDao;
import com.blog.noteforall.vo.PostVo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PostService {

    @Autowired 
    PostDao postDao;

    public void post(PostVo vo, String jsonUploadedUrls) {
        postDao.post(vo);
        System.out.println("keyproperty : " + vo.getNo());

        ObjectMapper objectMapper = new ObjectMapper(); 
        
        try {
            // JSON 문자열을 List로 변환
            List<String> uploadedUrls = objectMapper.readValue(jsonUploadedUrls, new TypeReference<List<String>>() {});
            System.out.println("Uploaded URLs as List: " + uploadedUrls);

            // 개별 요소 접근
            for (String url : uploadedUrls) {
                vo.setUrl(url);
                postDao.insertUrlAndPostNo(vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public PostVo getPost(int no) {
        return postDao.getPost(no);
    }

    public List<PostVo> getList() {
        
        return postDao.getList();
    }

    public boolean isAdmin(String password) {
        
        int isAdmin = 0;

        isAdmin = postDao.isAdmin(password);

        if (isAdmin == 1) {
            return true;
        }
        else {
            return false;
        }
    }
    
}
