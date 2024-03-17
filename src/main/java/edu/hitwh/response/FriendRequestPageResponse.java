package edu.hitwh.response;

import edu.hitwh.model.FriendRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "好友请求分页展示")
public class FriendRequestPageResponse extends PageResponse {
    @Schema(description = "分页结果")
    private List<FriendRequest> result;
}
