package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "发送信件请求类")
public class SendEmailRequest {
    @Schema(description = "发件人uid（不需要传递）",required = false)
    private Integer senderUid;
    @Schema(description = "收件人uid",required = true)
    private Integer addresseeUid;
    @Schema(description = "标题",required = true)
    private String title;
    @Schema(description = "内容",required = true)
    private String content;

}
