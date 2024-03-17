package edu.hitwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hitwh.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    //屏蔽用户
    @Insert("insert into blacked(user_uid,object_uid) values (#{currentJwt},#{uid})")
    void insertBlackObject(Integer currentJwt, Integer uid);
    //取消屏蔽
    @Delete("delete from blacked where user_uid = #{currentJwt} and object_uid = #{uid}")
    void deleteBlackObject(Integer currentJwt, Integer uid);
    //判断对方是否屏蔽用户
    @Select("select id from blacked where user_uid = #{addresseeUid} and object_uid = #{senderUid}")
    Integer selectBlack(Integer addresseeUid, Integer senderUid);
    //获取所有屏蔽了用户的人的uid
    @Select("select blacked.user_uid from blacked where object_uid = #{uid}")
    List<Integer> selectWhoBlackUser(Integer uid);

}
