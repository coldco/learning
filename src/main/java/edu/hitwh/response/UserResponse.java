package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页展示的用户信息")
public class UserResponse  {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "uid")
    private Integer uid;
}
