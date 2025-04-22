package com.chanlee.crpc.v2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 博客类
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog implements Serializable {
     /*
     * 博客id
     */
    private Integer id;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 博客作者
     */
    private String userId;
}
