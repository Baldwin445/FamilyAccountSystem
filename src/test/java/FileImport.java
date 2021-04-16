import cn.afterturn.easypoi.csv.CsvImportUtil;
import cn.afterturn.easypoi.csv.entity.CsvImportParams;
import com.baldwin.BaldwinApplication;
import com.baldwin.configs.WeChatImportHandle;
import com.baldwin.dao.UserMapper;
import com.baldwin.entity.Tag;
import com.baldwin.entity.WeChatData;
import com.baldwin.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName: FileImport
 * @Description: test for using import csv file
 * @author: Baldwin445
 * @date: 21/4/15 13:46
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BaldwinApplication.class)
public class FileImport {
    @Resource
    private TagService tagService;
    @Resource
    private UserMapper userMapper;

    @Test
    public void testImport(){
        //read csv data and handle 读取数据并处理输出
        CsvImportParams params = new CsvImportParams();
        WeChatImportHandle handle = new WeChatImportHandle();
        handle.setNeedHandlerFields(new String[]{"金额(元)","商品"});
        params.setTitleRows(16);
        params.setHeadRows(1);
        params.setDataHandler(handle);
        try {
            // read the csv data
            // 读取csv数据文件
            List<WeChatData> datas = CsvImportUtil.importCsv(new FileInputStream("C:/Users/12411/Desktop/微信支付账单(20201006-20210106).csv"),
                    WeChatData.class, params);
            List<String> tag, payTag, incomeTag;

            // DISTINCT all tags
            // 去重所有tag
            tag = datas.stream().map(WeChatData::getTagName).distinct().collect(Collectors.toList());

            // List To Map by TypeID
            // 按typeID分类list
            Map<Integer, List<WeChatData>> listMap = datas.stream().collect(Collectors.groupingBy(WeChatData::getTypeid));
            List<WeChatData> typeID1 = listMap.get(1);
            List<WeChatData> typeID2 = listMap.get(2);

            // DISTINCT tags by Pay or Income
            // 分类去重tag
            payTag = listMap.get(1).stream().map(WeChatData::getTagName).distinct().collect(Collectors.toList());
            incomeTag = listMap.get(2).stream().map(WeChatData::getTagName).distinct().collect(Collectors.toList());

            List<Tag> payTags = tagService.getUserAllPayTag(1);
            tag = payTags.stream().map(Tag::getTagName).distinct().collect(Collectors.toList());
            payTag.removeAll(tag);
            System.out.println(payTag);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCollectID(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String collectID = userMapper.getUserByID(1).getAcct()
                + sdf.format(new Date())
                + String.format("%05d", 445);
        System.out.println(collectID);
    }
}
