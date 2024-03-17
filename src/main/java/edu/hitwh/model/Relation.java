package edu.hitwh.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "好友关系信息")
public class Relation {
    @Schema(description = "被请求者uid")
    private Integer requestedUid;
    @Schema(description = "请求者uid")
    private Integer requesterUid;
    @Schema(description = "类型1基友2闺蜜3情侣")
    private Short type;
    @Schema(description = "状态0不同意1同意")
    private Short state;
    @Schema(description = "发送者用户名")
    private String username;
}
