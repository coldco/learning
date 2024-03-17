package edu.hitwh.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* 好友
* */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("friends")
public class Friends {
    private Integer userUid;
    private Integer friendUid;
    private String remark;
    private String relation;
    private String username;
    @TableField(exist = false)
    private String group;
    private Integer groupId;
    private Short deleted;
    private Short blacked;
    private Integer id;

}
