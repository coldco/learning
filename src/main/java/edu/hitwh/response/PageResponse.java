package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "分页实体")
public class PageResponse {
    @Schema(description = "总数据量")
    private Long total;
}
