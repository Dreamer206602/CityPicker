package com.tq.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class PinyinUtils {
    /**
     * 获取拼音的首字母(大写)
     * @param pinyin
     * @return
     */
    public static String getFirstLetter(final String pinyin){
        if(TextUtils.isDigitsOnly(pinyin))return "定位";
        String c=pinyin.substring(0,1);
        Pattern pattern=Pattern.compile("^[A-Za-z]+$");
        if(pattern.matcher(c).matches()){
            return c.toUpperCase();
        }else if(c.equals("0")){
            return "定位";
        }else if(c.equals("1")){
            return "热门";
        }
        return  "定位";
    }
}
