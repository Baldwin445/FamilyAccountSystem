package com.baldwin.controller;

import com.baldwin.entity.Tag;
import com.baldwin.service.TagService;
import com.baldwin.utils.LogUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName: TagController
 * @Description: Deal with Service & Mapper
 * @author: Baldwin445
 * @date: 21/3/29 16:57
 */
@Controller
public class TagController {
    @Resource
    private TagService tagService;

    @RequestMapping(value="/getPayTags/{userid}/{file}",method = RequestMethod.POST)
    public String getPayTag(@PathVariable String userid,@PathVariable String file, Model m){
        List<Tag> allTags = tagService.getDefaultTag();
        allTags.addAll(tagService.getUserTag(Integer.parseInt(userid)));
//        LogUtil.log(allTags);
        m.addAttribute("allTags", allTags);
        return "/details/"+ file +"::payTagField";
    }

    @RequestMapping(value = "/getIncomeTags/{userid}", method = RequestMethod.POST)
    public String getIncomeTag(@PathVariable String userid, Model m){
        List<Tag> allTags = tagService.getDefaultIncomeTag();
        allTags.addAll(tagService.getUserIncomeTag(Integer.parseInt(userid)));
//        LogUtil.log(allTags);
        m.addAttribute("incomeTags", allTags);
        return "/details/add::incomeTagField";
    }

    @RequestMapping(value = "/getTags/{userid}/{file}/{type}", method = RequestMethod.POST)
    public String getBothTag(@PathVariable String userid,
                             @PathVariable String file,
                             @PathVariable String type,Model m){
        List<Tag> allTags;
        if(type.equals("pay")){
            allTags = tagService.getDefaultIncomeTag();
            allTags.addAll(tagService.getUserIncomeTag(Integer.parseInt(userid)));
        }else {
            allTags = tagService.getDefaultIncomeTag();
            allTags.addAll(tagService.getUserIncomeTag(Integer.parseInt(userid)));
        }
        m.addAttribute("allTags", allTags);
        return "/details/"+ file +"::tagField";
    }



}
