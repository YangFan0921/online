package com.graduation.service;

import com.graduation.model.Tag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
public interface ITagService extends IService<Tag> {

    List<Tag> getTags();

    //获得所有标签的Map,key是标签名称,value是标签对象
    Map<String,Tag> getTagMap();

}
