package com.blog.noteforall.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.blog.noteforall.vo.PostVo;

@Mapper
public interface PostDao {

    void post(PostVo post);

    PostVo getPost(int no);

    List<PostVo> getList();
    
}
