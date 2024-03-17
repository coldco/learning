package edu.hitwh.exception;

import edu.hitwh.response.ResultResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理用户名重复异常
     */
    @ExceptionHandler(value = UsernameDuplicateException.class)
    public ResultResponse UsernameDuplicateExceptionHandler(UsernameDuplicateException e){
        return ResultResponse.error("用户名重复");
    }
    /**
     * 处理分组名重复异常
     */
    @ExceptionHandler(value = GroupDuplicateException.class)
    public ResultResponse GroupDuplicateExceptionHandler(GroupDuplicateException e){
        return ResultResponse.error("分组名重复");
    }
    /**
     * 处理删除默认分组（我的好友）异常
     */
    @ExceptionHandler(value = DeleteDefaultGroupException.class)
    public ResultResponse DeleteDefaultGroupExceptionHandler(DeleteDefaultGroupException e){
        return ResultResponse.error("禁止删除默认分组我的好友");
    }

    /**
     * 处理信件标题为空
     */
    @ExceptionHandler(value = TitleNullException.class)
    public ResultResponse TitleNullExceptionHandler(TitleNullException e){
        return ResultResponse.error("信件标题不能为空");
    }
    /**
     * 处理其他可能发生的异常
     */
    @ExceptionHandler(Exception.class)
    public ResultResponse otherExceptionHandler(Exception e){
        e.printStackTrace();
        return ResultResponse.error("操作错误，请联系管理员");
    }
}
