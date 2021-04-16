package com.baldwin.service;

import com.baldwin.entity.Tag;

import java.util.List;

public interface TagService {
    List<Tag> getDefaultTag();

    List<Tag> getUserTag(int userid);

    List<Tag> getDefaultIncomeTag();

    List<Tag> getUserIncomeTag(int userid);

    int tagNameToID(int userid, String tagName, int type);

    List<Tag> getUserAllPayTag(int userid);

    List<Tag> getUserAllIncomeTag(int userid);
}
