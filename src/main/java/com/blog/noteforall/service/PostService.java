package com.blog.noteforall.service;

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
    
}
