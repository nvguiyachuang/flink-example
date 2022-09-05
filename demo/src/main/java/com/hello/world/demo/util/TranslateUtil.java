package com.hello.world.demo.util;

import cn.hutool.http.HttpUtil;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * APP ID：20220901001328178
 * 密钥：ol8p7RNCJc7aLb3ve6nf
 * https://api.fanyi.baidu.com/api/trans/vip/translate?q=apple&from=en&to=zh&appid=20220901001328178&salt=hello&sign=d923fed3ff0de93fda7f30eee56a0108
 */
public class TranslateUtil {
    private static final String appid = "20220901001328178";
    private static final String securityKey = "ol8p7RNCJc7aLb3ve6nf";
    private static final String TRANS_API_HOST = "https://fanyi-api.baidu.com/api/trans/vip/translate";

    public static void main(String[] args) throws UnsupportedEncodingException {
        String result = getTranslateResult("你好的", "zh", "en");
        System.out.println(result);
    }

    // 发送查询
    public static String getTranslateResult(String query, String from, String to) {
        Map<String, Object> params = new HashMap<>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);
        params.put("appid", appid);
        // 随机数
        String salt = "hello";
        params.put("salt", salt);
        // 签名
        String src = appid + query + salt + securityKey; // 加密前的原文
        String value = MD5.md5(src);
        params.put("sign", value);
        System.out.println(value);
        return HttpUtil.get(TRANS_API_HOST, params);
    }
}
