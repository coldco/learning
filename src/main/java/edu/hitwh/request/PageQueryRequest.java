package edu.hitwh.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "分页查询实体")
public class PageQueryRequest {
    @Schema(description = "页码")
    private long pageNo = 1L;
    @Schema(description = "每页数据量")
    private long pageSize = 10L;
}
