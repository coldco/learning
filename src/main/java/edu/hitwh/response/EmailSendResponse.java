package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 本类用于处理发送信件时同时返回提示信息和生成的信件的主键id
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "信件发送后反馈信息")
public class EmailSendResponse {
    @Schema(description = "反馈信息")
    private String message;
    @Schema(description = "信件id")
    private Integer id;
}
