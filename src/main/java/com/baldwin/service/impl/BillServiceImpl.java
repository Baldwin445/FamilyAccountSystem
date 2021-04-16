package com.baldwin.service.impl;

import com.baldwin.dao.BillMapper;
import com.baldwin.dao.TagMapper;
import com.baldwin.dao.UserMapper;
import com.baldwin.entity.Bill;
import com.baldwin.entity.Tag;
import com.baldwin.entity.User;
import com.baldwin.entity.WeChatData;
import com.baldwin.service.BillService;
import com.baldwin.utils.LogUtil;
import com.baldwin.utils.UserUtil;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.poi.ss.formula.functions.T;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: BillServiceImpl
 * @Description: implement of Bill Service
 * @author: Baldwin445
 * @date: 21/3/31 13:50
 */
@Service
public class BillServiceImpl implements BillService {
    @Resource
    private BillMapper billMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private TagMapper tagMapper;
    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    public int addPayBill(Bill bill) {
        return billMapper.addPayBill(bill);
    }

    @Override
    public int addIncomeBill(Bill bill) {
        return billMapper.addIncomeBill(bill);
    }

    @Override
    public int delBill(int billID) {
        return billMapper.delBill(billID);
    }

    @Override
    public Bill getBillByID(int billID) {
        return billMapper.getBillByID(billID);
    }

    @Override
    public int updateBill(Bill bill) {
        return billMapper.updateBill(bill);
    }

    @Override
    public List<Bill> getBill(int typeid, int userid, int begin, int num) {
        User user = userMapper.getCompleteUser(userid);
        int access = user.getAccess().getAccess();
        int homeID = user.getHouseId();
        List<Bill> bill;

        //user: 仅看自己的账单
        //host/admin: 看全家人的
        if(access == UserUtil.USER_ACCESS_MEMBER)
            bill = billMapper.getSelfBill(typeid, userid, begin, num);
        else if(homeID == 0)
            bill = billMapper.getSelfBill(typeid, userid, begin, num);
        else
            bill = billMapper.getHomeBill(typeid, homeID, begin, num);

        return bill;
    }

    @Override
    public int countBill(int typeid, int userid) {
        User user = userMapper.getCompleteUser(userid);
        int access = user.getAccess().getAccess();
        int homeID = user.getHouseId();

        //user: 仅看自己的账单
        //host/admin: 看全家人的
        if(access == UserUtil.USER_ACCESS_MEMBER)
            return billMapper.countSelfBill(typeid, userid);
        else if(homeID == 0)
            return billMapper.countSelfBill(typeid, userid);
        else
            return billMapper.countHomeBill(typeid, homeID);
    }

    @Override
    public List<Bill> searchBill(int begin, int num, int userid,
                                 String startDate, String endDate,
                                 String name, int tagID, int typeID) {
        User user = userMapper.getCompleteUser(userid);
        int access = user.getAccess().getAccess();
        List<Bill> bills;

        // Classify User and Admin/Host, then consider whether Admin/Host has homeID field
        // 分类用户和管理员户主两类 再考虑管理员户主是否有HomeID字段
        if(access == UserUtil.USER_ACCESS_MEMBER)
            bills = billMapper.searchSelfBill(begin, num, userid,
                    startDate, endDate,
                    name, tagID, typeID);
        else if(user.getHouseId() == 0)
            bills = billMapper.searchSelfBill(begin, num, userid,
                    startDate, endDate,
                    name, tagID, typeID);
        else
            bills = billMapper.searchHomeBill(begin, num, user.getHouseId(),
                    startDate, endDate,
                    name, tagID, typeID);
        return bills;
    }

    //TODO: 仅记录了收入支出 中性账单没有导入
    @Override
    public List<WeChatData> importWeChatData(int userid, List<WeChatData> wcData) {
        List<String> payTagStrList, incomeTagStrList, dbPayTagStrList, dbIncomeTagStrList;
        // List To Map by TypeID
        // 按typeID分类list
        Map<Integer, List<WeChatData>> listMap = wcData.stream().collect(Collectors.groupingBy(WeChatData::getTypeid));

        // DISTINCT Pay or Income tags
        // 分类去重tag
        payTagStrList = listMap.get(1).stream().map(WeChatData::getTagName).distinct().collect(Collectors.toList());
        incomeTagStrList = listMap.get(2).stream().map(WeChatData::getTagName).distinct().collect(Collectors.toList());
        List<Tag> payTags = tagMapper.getDefaultTag();
        payTags.addAll(tagMapper.getUserTag(userid));
        List<Tag> incomeTags = tagMapper.getDefaultIncomeTag();
        incomeTags.addAll(tagMapper.getUserIncomeTag(userid));
        dbPayTagStrList = payTags.stream().map(Tag::getTagName).distinct().collect(Collectors.toList());
        dbIncomeTagStrList = incomeTags.stream().map(Tag::getTagName).distinct().collect(Collectors.toList());
        payTagStrList.removeAll(dbPayTagStrList);
        incomeTagStrList.removeAll(dbIncomeTagStrList);

        // import the tags which don't exist in db
        // 导入数据库中没有的Tags
        List<Tag> importTag = new ArrayList<>();
        if(payTagStrList.size() != 0 || incomeTagStrList.size() != 0){
            importTag.addAll(packTagParam(userid, 1, payTagStrList));
            importTag.addAll(packTagParam(userid, 2, incomeTagStrList));
            tagMapper.insertUserImportTag(importTag);

            // refresh the List<Tag>
            // 刷新tag列表
            payTags = tagMapper.getDefaultTag();
            payTags.addAll(tagMapper.getUserTag(userid));
            incomeTags = tagMapper.getDefaultIncomeTag();
            incomeTags.addAll(tagMapper.getUserIncomeTag(userid));
        }

        // construct List<WeChatData> TagID/collectionID/userID
        // 构造WeChatData列表
        // collectID format by Acct + DataSize + time
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String collectID = userMapper.getUserByID(userid).getAcct()
                        + sdf.format(new Date())
                        + String.format("%05d", wcData.size());
        Map<String, List<WeChatData>> payBill =
                listMap.get(1).stream().collect(Collectors.groupingBy(WeChatData::getTagName));
        Map<String, List<WeChatData>> incomeBill =
                listMap.get(2).stream().collect(Collectors.groupingBy(WeChatData::getTagName));
        List<WeChatData> allData = packWeChatParam(userid, collectID, payTags, payBill);
        allData.addAll(packWeChatParam(userid, collectID,incomeTags,incomeBill));
        System.out.println("导入数据数量： "+allData.size());

        // update the DateBase
        // 更新数据库相关信息
        billMapper.updateBillCollection(collectID, userid, allData.size(), "WeChat");
        insertBigData(allData, "WeChat");

        // get Bill inserted just now
        // 获取刚刚插入的账单数据
        return allData;
    }

    @Override
    public int countSearchBill(int userid, String startDate, String endDate, String name, int tagID, int typeID) {
        User user = userMapper.getCompleteUser(userid);
        int access = user.getAccess().getAccess();

        // Classify User and Admin/Host, then consider whether Admin/Host has homeID field
        // 分类用户和管理员户主两类 再考虑管理员户主是否有HomeID字段
        if(access == UserUtil.USER_ACCESS_MEMBER)
            return billMapper.countSearchSelfBill(userid,
                    startDate, endDate,
                    name, tagID, typeID);
        else if(user.getHouseId() == 0)
            return billMapper.countSearchSelfBill(userid,
                    startDate, endDate,
                    name, tagID, typeID);
        else
            return billMapper.countSearchHomeBill(user.getHouseId(),
                    startDate, endDate,
                    name, tagID, typeID);
    }

    /**
     * Use to set the Tag Param
     * @param userid 用户id
     * @param typeid 支付类型
     * @param tagName tag名称
     * @return
     */
    private List<Tag> packTagParam(int userid, int typeid, List<String> tagName){
        List<Tag> tags = new ArrayList<>();
        tagName.forEach(tag -> {
            Tag t = new Tag();
            t.setTagName(tag);
            t.setTypeid(typeid);
            t.setUserid(userid);
            tags.add(t);
        });
        return tags;
    }

    /**
     * Use to set the Bill Param
     * @param userid 用户id
     * @param collectID 集合表单编号
     * @param tags 标签列表
     * @param datas 数据列表
     * @return
     */
    private List<WeChatData> packWeChatParam(int userid, String collectID,
                                             List<Tag> tags, Map<String, List<WeChatData>> datas){
        List<WeChatData> temp = new ArrayList<>();
        tags.forEach(tag -> {
            if(datas.get(tag.getTagName()) != null){
                datas.get(tag.getTagName()).forEach(bill -> {
                    bill.setUserID(userid);
                    bill.setTagID(tag.getTagid());
                    bill.setCollectID(collectID);
                    temp.add(bill);
                });
            }
        });
        return temp;
    }


    /**
     *
     * @param datas 数据列表
     * @param platform 平台信息：WeChat
     */
    private void insertBigData(List<WeChatData> datas, String platform){
        //如果自动提交设置为true,将无法控制提交的条数，改为最后统一提交，可能导致内存溢出
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);
        //不自动提交
        if(platform.equals("WeChat")){
            // Wechat Data Module 微信数据处理模块
            try {
                BillMapper billMapper = session.getMapper(BillMapper.class);
                for(int i=0; i<datas.size(); i++){
                    billMapper.insertWeChatData(datas.get(i));
                    if(i%500 == 0 || i == datas.size() - 1){
                        //手动每500条提交一次，提交后无法回滚
                        session.commit();
                        //清理缓存，防止溢出
                        session.clearCache();
                    }
                }
            }catch (Exception e) {
                //没有提交的数据可以回滚
                session.rollback();
            } finally {
                session.close();
            }
        }
    }
}
