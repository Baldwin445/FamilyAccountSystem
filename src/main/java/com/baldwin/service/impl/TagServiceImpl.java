package com.baldwin.service.impl;

import com.baldwin.dao.TagMapper;
import com.baldwin.entity.Tag;
import com.baldwin.service.TagService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: TagServiceImpl
 * @Description: implement tag service
 * @author: Baldwin445
 * @date: 21/3/29 16:41
 */
@Service
public class TagServiceImpl implements TagService {
    @Resource
    private TagMapper mapper;

    @Override
    public List<Tag> getDefaultTag() {
        return mapper.getDefaultTag();
    }

    @Override
    public List<Tag> getUserTag(int userid) {
        return mapper.getUserTag(userid);
    }

    @Override
    public List<Tag> getDefaultIncomeTag() {
        return mapper.getDefaultIncomeTag();
    }

    @Override
    public List<Tag> getUserIncomeTag(int userid) {
        return mapper.getUserIncomeTag(userid);
    }

    /**
    * @return: -2：no result, -1: can't get tag_id, >=1：correct
    * @param userid: user id
    * @param tagName:
    * @param typeid: Pay or Income
    *
    * PS: need to search for userid=0, other method already search this situation
    * */
    @Override
    public int tagNameToID(int userid, String tagName, int typeid) {
        List<Tag> tags = mapper.tagNameToID(userid, tagName);
        tags.addAll(mapper.tagNameToID(0,tagName));
        if(tags.size() == 0) return -2;
        if(tags.size() == 1)
            return tags.get(0).getTagid();

        //loop when the size of tags >= 2
        //当出现多个tag时查找属于 支出/收入对应类型
        for (Tag t: tags)
            if(t.getTypeid() == typeid)
                return t.getTagid();

        return -1; //return -1 when loop didn't get the id
    }

    @Override
    public List<Tag> getUserAllPayTag(int userid) {
        List<Tag> payTags = mapper.getDefaultTag();
        payTags.addAll(mapper.getUserTag(userid));
        return payTags;
    }

    @Override
    public List<Tag> getUserAllIncomeTag(int userid) {
        List<Tag> incomeTags = mapper.getDefaultIncomeTag();
        incomeTags.addAll(mapper.getUserIncomeTag(userid));
        return incomeTags;
    }
}
