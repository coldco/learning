package edu.hitwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hitwh.model.Email;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailboxMapper extends BaseMapper<Email> {
}
