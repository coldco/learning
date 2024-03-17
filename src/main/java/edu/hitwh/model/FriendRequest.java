package edu.hitwh.model;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "好友请求信息")
@TableName("friend_request")
public class FriendRequest {
    @Schema(description = "被请求者uid")
    private Integer requestedUid;
    @Schema(description = "请求者uid")
    private Integer requesterUid;
    @Schema(description = "状态0不同意1同意")
    private short state = (short)0;
    @Schema(description = "发起时间")
    private LocalDateTime createTime;
    @Schema(description = "发送者用户名")
    private String username;
}
