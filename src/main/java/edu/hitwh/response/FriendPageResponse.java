package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "好友列表分页展示")
public class FriendPageResponse extends PageResponse {
    @Schema(description = "分页结果")
    private List<FriendResponse> result;
}
