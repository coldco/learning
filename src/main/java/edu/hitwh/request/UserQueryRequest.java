package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "用户模糊查询请求类")
public class UserQueryRequest extends PageQueryRequest {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "用户uid")
    private Integer uid;
}
