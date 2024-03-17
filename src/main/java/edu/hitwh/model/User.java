package edu.hitwh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "用户实体")
@TableName("tb_user")
public class User {
    private String username;
    @TableId(type = IdType.NONE)
    @Schema(pattern = "账号")
    private int uid;
    private String password;
    private String inboxGroup;
    private String outboxGroup;

}
