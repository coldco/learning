package edu.hitwh.response;

import edu.hitwh.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "用户分页展示")
public class UserPageResponse extends PageResponse {
    @Schema(description = "分页结果")
    private List<UserResponse> result;
}
