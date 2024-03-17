package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户注册请求类")
public class UserEnrollRequest {
    @Schema(description = "用户名",required = true)
    private String username;
    @Schema(description = "密码",required = true)
    private String password;
}
