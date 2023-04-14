package com.graduation.service.impl;

import com.graduation.mapper.TagMapper;
import com.graduation.model.Tag;
import com.graduation.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author graduation.com
 * @since 2023-03-02
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    //成员变量充当缓存
    private List<Tag> tags =  new CopyOnWriteArrayList<>();

    private Map<String,Tag> tagMap=new ConcurrentHashMap<>();

    @Resource
    private RedisTemplate<String,List<Tag>> redisTemplate;

    @Override
    public List<Tag> getTags() {
//        if (tags.isEmpty()){
//            synchronized (tags){
//                if (tags.isEmpty()){
//                    tags.addAll(list());
//                    //循环遍历所有标签
//                    for(Tag t: tags){
//                        //以标签名称为key,标签对象为value保存在tagMap中
//                        tagMap.put(t.getName(),t);
//                    }
//                }
//            }
//        }
        List<Tag> tags = redisTemplate.opsForValue().get("tags");
        if (tags == null || tags.isEmpty()){
            System.out.println("连接数据库新增Redis中内容");
            tags = list();
            redisTemplate.opsForValue().set("tags",tags);
        }
        return tags;
    }

    @Override
    public Map<String, Tag> getTagMap() {
//        if(tagMap.isEmpty()){
//            getTags();
//        }
//        return tagMap;
        Map<String,Tag> map = new HashMap<>();
        for (Tag tag : getTags()){
            map.put(tag.getName(),tag);
        }
        return map;
    }
}
