package com.graduation.mapper;

import com.graduation.model.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.graduation.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Repository
public interface NoticeMapper extends BaseMapper<Notice> {

    @Update("update notice set read_status=1 where reply_user_id=#{reply_user_id}")
    Integer updateNoticeReadStatus(@Param("reply_user_id")Integer reply_user_id);

    @Select("select * from notice where reply_user_id=#{reply_user_id} and read_status=0 order by createtime desc limit 0,10")
    List<Notice> getTop10Notice(@Param("reply_user_id") Integer reply_user_id);


}
