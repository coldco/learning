package edu.hitwh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hitwh.model.Email;
import edu.hitwh.response.EmailPageResponse;
import edu.hitwh.request.EmailQueryRequest;
import edu.hitwh.response.EmailDetailResponse;

import java.util.List;

public interface MailboxService extends IService<Email>  {
    EmailPageResponse getEmail(Integer currentJwt, Short box, EmailQueryRequest query);

    EmailDetailResponse readEmail(Integer id, Short box);

    void deleteEmail(Short box, List<Integer> ids);

    void deleteGroup(Short box, String group);

    void topEmail(Integer id, short i,Short box);




}
