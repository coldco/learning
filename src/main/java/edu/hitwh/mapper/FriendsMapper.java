package edu.hitwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hitwh.model.FriendRequest;
import edu.hitwh.model.Friends;
import edu.hitwh.model.Group;
import edu.hitwh.model.Relation;
import edu.hitwh.request.FriendQueryRequest;
import edu.hitwh.response.FriendResponse;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendsMapper extends BaseMapper<Friends> {
    /*
     * 创建好友申请
     * */
    @Insert("insert into friendrequest(requested_uid, requester_uid, state,create_time) values (#{requestedUid},#{requesterUid},#{state},#{createTime})")
    void insertFriendRequest(FriendRequest friendRequest);

    /*
     * 查询好友申请
     * */
    @Select("select requester_uid,state,create_time,username from friendrequest , chaoci.tb_user where requested_uid = #{uid} and uid = requester_uid ")
    List<FriendRequest> selectFriendRequests(Integer uid);


    /*
    * 更新好友申请表中的state
    * */
    @Update("update friendrequest set state = 1 where requester_uid = #{requesterUid} and requested_uid = #{requestedUid}")
    void updateFriendRequestState(Integer requesterUid, Integer requestedUid);

    /*
    * 好友列表查询
    * */
    List<FriendResponse> selectFriends(FriendQueryRequest friendQuery);

    /*
    * 判断是否发送过好友请求
    * */
    @Select("select count(*) from friendrequest where (requested_uid = #{uid} and requester_uid = #{uid1}) or" +
            " (requested_uid = #{uid1} and requester_uid = #{uid})")
    Boolean selectRequest(Integer uid, Integer uid1);

    /*
    * 判断是否发送过好友关系绑定申请
    * */
    @Select("select count(*) from friend_relationship where (requested_uid = #{uid} and requester_uid = #{uid1}) or" +
            " (requested_uid = #{uid1} and requester_uid = #{uid})")
    boolean selectRelationRequest(Integer uid, Integer uid1);

    /*
    * 创建关系申请
    * */
    @Insert("insert into friend_relationship(requested_uid, requester_uid, type, state) VALUES (#{requestedUid},#{requesterUid},#{type},#{state})")
    void insertRelationRequest(Relation relation);

    /*
    * 查看关系申请
    * */
    @Select("select requester_uid,state,type,username from friend_relationship left join chaoci.tb_user on requested_uid = #{uid} and uid = requester_uid ")
    List<Relation> selectRelationRequests(Integer uid);

    /*
    * 更新关系绑定表中的state
    * */
    @Update("update friend_relationship set state = #{state} where (requester_uid = #{requesterUid} and requested_uid = #{requestedUid})" +
            " or (requester_uid = #{requestedUid} and requested_uid = #{requesterUid})")
    void updateRelationStateTo(Integer requesterUid, Integer requestedUid,Short state);

    /*
    * 查看分组
    * */
    @Select("select `group`,id from friend_groups where uid = #{currentJwt}")
    List<Group> selectGroup(Integer currentJwt);

    /*
    * 新增分组
    * */
    @Insert("insert into friend_groups(uid, `group`) VALUES (#{uid},#{group})")
    void insertGroup(int uid, String group);

    /*
    * 获取“我的好友主键id”
    * */
    @Select("select friend_groups.id from friend_groups where uid = #{uid} and `group` ='我的好友'")
    int selectDefaultGroupId(Integer uid);

    /*
    * 删除分组
    * */
    @Delete("delete from friend_groups where id = #{groupId}")
    void deleteGroup(Integer groupId);

    /*
    * 删除好友申请
    * */
    void deleteFriendRequest(List<Integer> uids, Integer currentJwt);

    /*
    * 删除好友关系绑定申请
    * */
    void deleteRelationRequest(List<Integer> uids, Integer currentJwt);
}