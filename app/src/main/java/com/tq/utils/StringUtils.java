package com.tq.utils;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class StringUtils {
    /**
     * 提取城市或者是县
     * @param city
     * @param district
     * @return
     */
    public static  String extractLocation(final String city,final String district){
        return district.contains("县")?district.substring(0,district.length()-1):city.substring(0,city.length()-1);
    }
}
