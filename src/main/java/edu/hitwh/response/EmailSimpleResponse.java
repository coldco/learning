package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页展示信件")
public class EmailSimpleResponse {
    @Schema(description = "信件id")
    private Integer id;
    @Schema(description = "标题")
    private String title;
    @Schema(description = "发件人uid")
    private Integer senderUid;
    @Schema(description = "收件人uid")
    private Integer addresseeUid;
    @Schema(description = "是否已读0否1是")
    private Short read;
    @Schema(description = "收件箱所属分组")
    private String groupSender;
    @Schema(description = "发件箱所属分组")
    private String groupAddressee;
    @Schema(description = "收件箱内是否置顶0否1是")
    private Short inboxTop;
    @Schema(description = "发件箱内是否置顶0否1是")
    private Short outboxTop;
}
