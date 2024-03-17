package edu.hitwh.controller;

import edu.hitwh.request.UidLoginRequest;
import edu.hitwh.request.UsernameLoginRequest;
import edu.hitwh.response.ResultResponse;
import edu.hitwh.model.User;
import edu.hitwh.request.UserEnrollRequest;
import edu.hitwh.request.UserQueryRequest;
import edu.hitwh.response.UserPageResponse;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户控制层")
@ApiResponses
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "注册")
    @PostMapping("/enroll")
    public ResultResponse<Integer> enroll(@RequestBody UserEnrollRequest user) {
        int uid;
        try {
            uid = userService.saveNewUser(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResultResponse.success(uid);
    }

    @Operation(summary = "uid登录")
    @PostMapping("/login/uid")
    public ResultResponse<String> loginByUid(@RequestBody UidLoginRequest uidLoginRequest) {
        //密码加密
        uidLoginRequest.setPassword(userService.md5Encrypt(uidLoginRequest.getPassword()));
        boolean exists = userService.lambdaQuery()
                .eq(User::getUid, uidLoginRequest.getUid())
                .eq(User::getPassword, uidLoginRequest.getPassword())
                .exists();
        //新建user封装
        User user= new User();
        user.setUid(uidLoginRequest.getUid());
        user.setUsername(userService.selectUsernameByUid(uidLoginRequest.getUid()));

        return getLoginResult(user, exists);
    }
    @Operation(summary = "用户名登录")
    @PostMapping("/login/username")
    public ResultResponse<String> loginByUsername(@RequestBody UsernameLoginRequest usernameLoginRequest) {
        //密码加密
        usernameLoginRequest.setPassword(userService.md5Encrypt(usernameLoginRequest.getPassword()));
        boolean exists = userService.lambdaQuery()
                .eq(User::getUsername, usernameLoginRequest.getUsername())
                .eq(User::getPassword, usernameLoginRequest.getPassword())
                .exists();
        //新建user封装
        User user= new User();
        user.setUid(userService.selectUidByUsername(usernameLoginRequest.getUsername()));
        return getLoginResult(user, exists);
    }

    /**
     * 获得jwt令牌并返回前端
     * @param user 当前用户信息
     * @param exists 用户是否存在，即用户名和密码正确
     * @return 返回给前端的jwt令牌
     */
    private ResultResponse<String> getLoginResult( User user, boolean exists) {
        if (exists) {
            Map<String,Object> claims = new HashMap<>();
            claims.put("username",user.getUsername());
            claims.put("uid",user.getUid());
            String jwt = JWTUtils.JwtGenerate(claims);
            return ResultResponse.success(jwt);
        } else {
            return ResultResponse.error("用户名或密码错误");
        }
    }


    @Operation(summary = "用户查询")
    @GetMapping("/users")
    public ResultResponse<UserPageResponse> userQuery(@RequestBody UserQueryRequest userQuery){

        try {
            return ResultResponse.success(userService.select(userQuery));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "屏蔽用户")
    @PostMapping("/blackUser")
    public ResultResponse<Object> blackUser(@Parameter(description = "屏蔽对象uid") Integer uid){
        userService.blackUser(JWTUtils.currentJwt,uid,true);
        return ResultResponse.success();
    }

    @DeleteMapping("/unBlackUser")
    public ResultResponse<Object> unBlackUser(@Parameter(description = "屏蔽对象uid") Integer uid){
        userService.blackUser(JWTUtils.currentJwt,uid,false);
        return ResultResponse.success();
    }



}
