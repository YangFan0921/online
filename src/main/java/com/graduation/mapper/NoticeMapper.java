package com.graduation.mapper;

import com.graduation.model.Notice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

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
}
