package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "好友模糊查询请求类")
public class FriendQueryRequest extends UserQueryRequest {
    //当前用户uid
    @Schema(description = "当前用户uid(不需要传递)")
    private Integer userUid;
    //好友备注
    @Schema(description = "好友备注")
    private String remark;
    //分组id
    @Schema(description = "分组id")
    private Integer groupId;

}
