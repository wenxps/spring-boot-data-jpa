package com.atguigu.jpa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库返回数据接口
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOnlyIdAndUsername {
    Integer id;
    String username;
}