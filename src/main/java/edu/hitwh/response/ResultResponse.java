package edu.hitwh.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "统一响应结果")
public class ResultResponse<T> {
    @Schema(description = "响应码，1 代表成功; 0 代表失败")
    private Integer code;
    @Schema(description = "响应信息 描述字符串")
    private String msg;
    @Schema(description = "返回的数据")
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    //增删改 成功响应
    public static<T> ResultResponse<T> success(){
        return new ResultResponse<>(1,"success",null);
    }
    //查询 成功响应
    public static <T>ResultResponse<T> success(T data){
        return new <T>ResultResponse<T>(1,"success",data);
    }
    //失败响应
    public static <T>ResultResponse<T> error(String msg){
        return new ResultResponse<>(0,msg,null);
    }
}
