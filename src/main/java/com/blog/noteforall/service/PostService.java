package com.blog.noteforall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.blog.noteforall.dao.PostDao;
import com.blog.noteforall.vo.PostVo;

@Service
public class PostService {

    @Autowired 
    PostDao postDao;

    public void post(PostVo post) {
        postDao.post(post);
    }

    public PostVo getPost(int no) {
        return postDao.getPost(no);
    }

    public List<PostVo> getList() {
        
        return postDao.getList();
    }
    
}
