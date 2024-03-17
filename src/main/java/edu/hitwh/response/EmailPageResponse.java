package edu.hitwh.response;

import edu.hitwh.model.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "信件分页展示")
public class EmailPageResponse extends PageResponse {
    @Schema(description = "信件集合")
    private List<EmailSimpleResponse> result;
}
