package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友展示信息")
public class FriendResponse {
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "所属组名")
    private String group;
    @Schema(description = "关系")
    private Integer relation;
    @Schema(description = "是否屏蔽0否1是")
    private Short blacked;
}
