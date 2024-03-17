package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "账号登录")
public class UidLoginRequest {
    @Schema(description = "账号")
    private Integer uid;
    @Schema(description = "密码")
    private String password;
}
