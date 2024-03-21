package com.yupi.springbootinit.model.dto.user;

import java.io.Serializable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户创建请求
 *
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;
//
//    /**
//     * 用户头像
//     */
//    private MultipartFile avatar;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}