package com.chanlee.crpc.v2.service;

import com.chanlee.crpc.v2.entity.Blog;

/**
 * 博客服务
 */
public interface BlogService {

    /**
     * 根据id获取博客
     * @param id
     * @return
     */
    Blog getBlogById(int id);
}
