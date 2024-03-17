package edu.hitwh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hitwh.model.User;
import edu.hitwh.request.UserEnrollRequest;
import edu.hitwh.request.UserQueryRequest;
import edu.hitwh.response.UserPageResponse;

import java.util.List;

public interface UserService extends IService<User> {
    String md5Encrypt(String password);

    int uidGenerate();

    String selectUsernameByUid(Integer uid);

    Integer selectUidByUsername(String username);

    UserPageResponse select(UserQueryRequest userQuery);

    int saveNewUser(UserEnrollRequest user);

    void newMailGroup(Short box, String group);

    void deleteMailGroup(Short box, String group);

    List<String> getMailGroup(Short box);

    void blackUser(Integer currentJwt, Integer uid, boolean flag);

    boolean isBlacked(Integer addresseeUid,Integer senderUid);
}
