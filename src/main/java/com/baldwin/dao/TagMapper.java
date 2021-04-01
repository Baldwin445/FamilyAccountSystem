package com.baldwin.dao;

import com.baldwin.entity.Tag;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapper {
    List<Tag> getDefaultTag();

    List<Tag> getUserTag(int userid);

    List<Tag> getDefaultIncomeTag();

    List<Tag> getUserIncomeTag(int userid);

    List<Tag> tagNameToID(int userid, String tagName);
}
