package edu.hitwh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hitwh.model.Friends;
import edu.hitwh.model.Group;
import edu.hitwh.model.Relation;
import edu.hitwh.response.FriendPageResponse;
import edu.hitwh.response.FriendRequestPageResponse;
import edu.hitwh.request.FriendQueryRequest;
import edu.hitwh.request.PageQueryRequest;
import edu.hitwh.response.UserResponse;

import java.util.List;

public interface FriendService extends IService<Friends> {
    FriendRequestPageResponse getFriendRequests(PageQueryRequest pageQueryRequest, Integer currentJwt);
    void saveFriendRequest(Integer currentJwt, Integer uid);


    void agreeFriendRequest(Integer uid, Integer currentJwt);

    FriendPageResponse getFriendPage(FriendQueryRequest friendQuery, Integer currentJwt);

    UserResponse getFriendOne(Integer uid);

    void saveFriendRelationRequest(Integer uid, Integer currentJwt, Short type);

    List<Relation> getRelationRequests(Integer currentJwt);

    void agreeRelationRequest(Integer uid, Integer currentJwt, Short type);

    void cancelRelation(Integer uid, Integer currentJwt);

    String getRemark(Integer uid, Integer currentJwt);

    void updateRemark(Integer currentJwt, Integer uid,String remark);

    List<Group> getGroup(Integer currentJwt);

    void saveGroup(String group, Integer currentJwt);

    void addFriendToGroup(Integer groupId, Integer uid, Integer currentJwt);

    void deleteGroup(Integer groupId);

    void blackFriend(Integer uid, Integer currentJwt,Short state);

    void deleteFriend(List<Integer> uids, Integer currentJwt);
}
