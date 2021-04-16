package com.baldwin.configs;

import cn.afterturn.easypoi.handler.impl.ExcelDataHandlerDefaultImpl;
import com.baldwin.entity.WeChatData;

/**
 * @ClassName: WeChatImportHandle
 * @Description: be used to import wechat data 用于处理导入的微信数据
 * @author: Baldwin445
 * @date: 21/4/15 18:26
 */
public class WeChatImportHandle extends ExcelDataHandlerDefaultImpl<WeChatData> {


    /**
     * @param obj 当前对象
     * @param name 当前字段名称
     * @param value 当前值
     * @return
     */
    @Override
    public Object importHandler(WeChatData obj, String name, Object value) {
        String str = String.valueOf(value);
        if(name.equals("金额(元)")) {
            // remove ¥ and ,
            // 移除 ¥ and ,
            value = str.substring(1).replaceAll(",", "");
        }
        if(name.equals("商品"))
            if(str.equals("/")) value = null;

        return value;
    }
}
