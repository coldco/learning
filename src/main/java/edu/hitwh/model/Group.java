package edu.hitwh.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.annotations.Insert;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分组信息")
public class Group {
    @Schema(description = "分组id")
    private Integer id;
    private Integer uid;
    @Schema(description = "组名")
    private String group;
}
