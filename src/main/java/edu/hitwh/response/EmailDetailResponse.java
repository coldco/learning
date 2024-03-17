package edu.hitwh.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "查看信件详细信息")
public class EmailDetailResponse {
    @Schema(description = "信件id")
    private Integer id;
    @Schema(description = "内容")
    private String content;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "发件人uid")
    private Integer senderUid;
    @Schema(description = "收件人uid")
    private Integer addresseeUid;
    @Schema(description = "是否已读0未读，1已读")
    private Short read;
    @Schema(description = "发件箱内的分组")
    private String groupSender;
    @Schema(description = "收件箱内的分组")
    private String groupAddressee;
    @Schema(description = "是否置顶0否1是")
    private Short top;

}
