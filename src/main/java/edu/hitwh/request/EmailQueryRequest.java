package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "邮件模糊查询请求类")
public class EmailQueryRequest extends PageQueryRequest {
    //信件标题
    @Schema(description = "信件标题")
    private String title;
    //信件内容
    @Schema(description = "信件内容")
    private String content;
    //当前用户uid
    @Schema(description = "当前用户uid（不需要传入）")
    private Integer uid;
    //分组名
    @Schema(description = "分组名")
    private String group;
}
