package com.chanlee.crpc.v0.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
/**
 * 用户类
 */
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
