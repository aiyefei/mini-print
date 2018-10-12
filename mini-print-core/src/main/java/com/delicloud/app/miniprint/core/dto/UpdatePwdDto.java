package com.delicloud.app.miniprint.core.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: dy
 * @Description: 更新密码dto
 * @Date: 2018/9/19 11:55
 */
@Data
public class UpdatePwdDto {

    private String beforePwd;
    private String newPwd;
    private String sureNewPwd;

}
