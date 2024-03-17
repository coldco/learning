package edu.hitwh.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import edu.hitwh.model.Cron;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CronMapper extends BaseMapper<Cron> {
}
