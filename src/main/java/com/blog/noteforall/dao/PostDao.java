package com.blog.noteforall.dao;

import org.apache.ibatis.annotations.Mapper;

import com.blog.noteforall.vo.PostVo;

@Mapper
public interface PostDao {

    void post(PostVo post);

    PostVo getPost(int no);
    
}
