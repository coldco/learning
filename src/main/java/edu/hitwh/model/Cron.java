package edu.hitwh.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("cron")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cron {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private Integer emailId;
    private LocalDateTime executeTime;
    private LocalDate startTime;
    private LocalDate deadTime;
}
