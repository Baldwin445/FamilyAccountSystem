import com.baldwin.BaldwinApplication;
import com.baldwin.dao.UserMapper;
import com.baldwin.service.TagService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * @ClassName: JsonTest
 * @Description: test json
 * @author: Baldwin445
 * @date: 21/4/23 1:33
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = BaldwinApplication.class)
public class JsonTest {
    @Resource
    private TagService tagService;
    @Resource
    private UserMapper userMapper;

    @Test
    @ResponseBody
    public ModelAndView testJson(){
        JSONArray json = new JSONArray();
        JSONObject object = new JSONObject();
        object.put("1","2");
        json.add(object);
        object.put("nihao","hello");
        json.add(object);

        ModelAndView mav = new ModelAndView();
        return mav;

    }
}
