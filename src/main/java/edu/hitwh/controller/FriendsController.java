package edu.hitwh.controller;


import edu.hitwh.model.Group;
import edu.hitwh.model.Relation;
import edu.hitwh.response.*;
import edu.hitwh.request.FriendQueryRequest;
import edu.hitwh.request.PageQueryRequest;
import edu.hitwh.service.FriendService;
import edu.hitwh.service.UserService;
import edu.hitwh.utils.JWTUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friend")
@Tag(name = "好友控制层")
@RequiredArgsConstructor
public class FriendsController {
    private final FriendService friendService;
    private final UserService userService;

    @Operation(summary = "发送好友申请")
    @PostMapping("/sendRequest")
    public ResultResponse<Object> sendFriendRequest(@Parameter(description = "请求好友的uid") Integer uid){
        friendService.saveFriendRequest(JWTUtils.currentJwt,uid);
        return ResultResponse.success();
    }

    @Operation(summary = "查看好友请求")
    @GetMapping("/getRequest")
    public ResultResponse<FriendRequestPageResponse> getFriendRequest(@Parameter @RequestParam  PageQueryRequest pageQueryRequest){
        FriendRequestPageResponse pageResponse = friendService.getFriendRequests(pageQueryRequest,JWTUtils.currentJwt);
        return ResultResponse.success(pageResponse);
    }

    @Operation(summary = "同意好友申请")
    @PutMapping("/agreeRequest")
    public ResultResponse<Object> agreeFriendRequest(@Parameter Integer uid){
        friendService.agreeFriendRequest(uid,JWTUtils.currentJwt);
        return ResultResponse.success();
    }

    @Operation(summary = "查找好友")
    @GetMapping()
    public ResultResponse<FriendPageResponse> getFriendPage(@Parameter @RequestParam FriendQueryRequest friendQuery){
        FriendPageResponse pageBean = friendService.getFriendPage(friendQuery,JWTUtils.currentJwt);
        return ResultResponse.success(pageBean);
    }

    @Operation(summary = "查看好友精确信息")
    @GetMapping("/getOneFriend")
    public ResultResponse<UserResponse> getFriendOne(@Parameter(description = "好友uid") Integer uid){
        return ResultResponse.success(friendService.getFriendOne(uid));
    }


    @Operation(summary = "发送好友关系绑定申请")
    @PostMapping("/sendRelationRequest")
    public ResultResponse<Object> sendRelationRequest(@Parameter(description = "好友uid") Integer uid,
                                              @Parameter(description = "关系绑定类型1基友2闺蜜3情侣") Short type){
        friendService.saveFriendRelationRequest(uid,JWTUtils.currentJwt,type);
    return ResultResponse.success();
    }

    @Operation(summary = "查看关系绑定申请")
    @GetMapping("/getRelationRequest")
    public ResultResponse<List<Relation>> getRelationRequest(){
        List<Relation> relations = friendService.getRelationRequests(JWTUtils.currentJwt);
        return ResultResponse.success(relations);
    }


    @Operation(summary = "同意好友关系绑定")
    @PutMapping("/agreeRelationRequest")
    public ResultResponse<Object> agreeRelationRequest(@Parameter(description = "好友uid") Integer uid,
                                               @Parameter(description = "关系绑定类型1基友2闺蜜3情侣") Short type){
        friendService.agreeRelationRequest(uid,JWTUtils.currentJwt,type);
        return ResultResponse.success();
    }

    @Operation(summary = "解除好友关系绑定")
    @PutMapping("/cancel")
    public ResultResponse<Object> cancelRelation(@Parameter(description = "好友uid") Integer uid){
        friendService.cancelRelation(uid,JWTUtils.currentJwt);
        return ResultResponse.success();
    }

    @Operation(summary = "回显好友备注")
    @GetMapping("/Remark")
    public ResultResponse<String> getRemark(@Parameter(description = "好友uid") Integer uid){
        return ResultResponse.success(friendService.getRemark(uid,JWTUtils.currentJwt));
    }

    @Operation(summary = "修改好友备注")
    @PutMapping("/Remark")
    public ResultResponse<Object> updateRemark(@Parameter(description = "备注") @RequestParam String remark,
                                               @Parameter(description = "好友uid") @RequestParam Integer uid){
        friendService.updateRemark(JWTUtils.currentJwt, uid,remark);
        return ResultResponse.success();
    }

    @Operation(summary = "查看分组")
    @GetMapping("/group")
    public ResultResponse<List<Group>> getGroup(){
        List<Group> groupList =  friendService.getGroup(JWTUtils.currentJwt);
        return ResultResponse.success(groupList);
    }

    @Operation(summary = "新增好友分组")
    @PostMapping("/group/{group}")
    public ResultResponse<Object> newGroup(@Parameter(description = "组名") @PathVariable String group){
        friendService.saveGroup(group,JWTUtils.currentJwt);
        return ResultResponse.success();
    }

    @Operation(summary = "删除分组（不包括默认分组）")
    @DeleteMapping("/group")
    public ResultResponse<Object> deleteGroup(@Parameter(description = "分组id") Integer groupId){
        friendService.deleteGroup(groupId);
        return ResultResponse.success();
    }

    @Operation(summary = "添加好友到分组")
    @PutMapping("/group")
    public ResultResponse<Object> addFriendToGroup(@Parameter(description = "分组id") Integer groupId,
                                           @Parameter(description = "好友uid") Integer uid){
        friendService.addFriendToGroup(groupId,uid,JWTUtils.currentJwt);
        return ResultResponse.success();
    }

    @Operation(summary = "查看当前分组的所有好友")
    @GetMapping("/group/getFriend")
    public ResultResponse<FriendPageResponse> getGroupFriend(@Parameter(description = "分组id") Integer groupId,
                                                             @Parameter(description = "每页数据量") Long pageSize,
                                                             @Parameter(description = "页码") Long pageNo){
        FriendQueryRequest friendQuery = new FriendQueryRequest();
        friendQuery.setGroupId(groupId);
        friendQuery.setPageSize(pageSize);
        friendQuery.setPageNo(pageNo);
        FriendPageResponse friendGroupPage = friendService.getFriendPage(friendQuery, JWTUtils.currentJwt);
        return ResultResponse.success(friendGroupPage);
    }

    @Operation(summary = "屏蔽好友")
    @PutMapping("/black")
    public ResultResponse<Object> blackFriend(@Parameter(description = "好友uid") Integer uid){
        //将好友表中的信息更新
        friendService.blackFriend(uid,JWTUtils.currentJwt,(short) 1);
        //新建屏蔽对象
        userService.blackUser(JWTUtils.currentJwt,uid,true);
        return ResultResponse.success();
    }
    @Operation(summary = "取消屏蔽")
    @PutMapping("/unblack")
    public ResultResponse<Object> unblackFriend(@Parameter(description = "好友uid") Integer uid){
        //将好友表中的信息更新
        friendService.blackFriend(uid,JWTUtils.currentJwt,(short)0);
        //删除屏蔽对象
        userService.blackUser(JWTUtils.currentJwt,uid,false);
        return ResultResponse.success();
    }
    @Operation(summary = "（批量）删除好友")
    @DeleteMapping("/{uids}")
    public ResultResponse<Object> deleteFriend(@PathVariable List<Integer> uids){
        friendService.deleteFriend(uids,JWTUtils.currentJwt);
        return ResultResponse.success();
    }

}
