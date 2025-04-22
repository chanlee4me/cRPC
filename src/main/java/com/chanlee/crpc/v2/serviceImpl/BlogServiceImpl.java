package com.chanlee.crpc.v2.serviceImpl;

import com.chanlee.crpc.v2.entity.Blog;
import com.chanlee.crpc.v2.service.BlogService;

import java.util.UUID;

/**
 * 博客服务实现类
 */
public class BlogServiceImpl implements BlogService {

    public Blog getBlogById(int id) {
        Blog blog = Blog.builder()
                .id(id)
                .title("我的博客")
                .userId(UUID.randomUUID().toString())
                .build();
        System.out.println("客户端查询了"+id+"博客");
        return blog;
    }
}
