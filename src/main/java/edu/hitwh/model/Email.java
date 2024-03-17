package edu.hitwh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "信件")
@TableName("tb_email")
public class Email {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String content;
    private String title;
    private Integer senderUid;
    private Integer addresseeUid;
    private Short read;
    private Short deletedSender;
    private Short deletedAddressee;
    private String groupSender;
    private String groupAddressee;
    private LocalDateTime sendTime;
    private Short inboxTop;
    private Short outboxTop;
    private Short visibility;
}
