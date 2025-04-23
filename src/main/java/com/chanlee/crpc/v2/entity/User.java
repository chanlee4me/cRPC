package com.chanlee.crpc.v2.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
/**
 * 用户类
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements Serializable {
    /**
     * 用户id
     */
    Integer id;

    /**
     * 用户真实姓名
     */
    String realName;

    /**
     * 用户年龄
     */
    Integer age;
}
