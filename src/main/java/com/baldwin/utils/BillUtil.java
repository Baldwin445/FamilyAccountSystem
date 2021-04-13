package com.baldwin.utils;

import com.baldwin.entity.Bill;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * @ClassName: BillUtil
 * @Description: some tool function for bill
 * @author: Baldwin445
 * @date: 21/4/13 15:53
 */
public class BillUtil {

    public static String billModelToJSON(List<Bill> bill){
        JSONArray jsonArray = new JSONArray();
        for(Bill b: bill){
            JSONObject json;
            json =JSONObject.fromObject(b);
            json.putAll(JSONObject.fromObject(b.getUser()));
            json.putAll(JSONObject.fromObject(b.getRoleInfo()));
            json.remove("user");
            json.remove("roleInfo");
            jsonArray.add(json);
        }
        return jsonArray.toString();
    }
}
