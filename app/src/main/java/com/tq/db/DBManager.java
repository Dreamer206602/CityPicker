package com.tq.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.tq.model.City;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by boobooL on 2016/4/6 0006
 * Created 邮箱 ：boobooMX@163.com
 */
public class DBManager {
    public static final String ASSETS_NAME = "china_cities.db";
    public static final String DB_NAME = "china_cities.db";
    public static final String TABLE_NAME = "city";
    public static final String NAME = "name";
    public static final String PINYIN = "pinyin";
    public static final int BUFFER_SIZE = 1024;
    private String DB_PATH;
    private Context mContext;

    public DBManager(Context mContext) {
        this.mContext = mContext;
        DB_PATH = File.separator + "data" +
                Environment.getDataDirectory().getAbsolutePath() + File.separator + mContext.getPackageName() + File.separator + "databases" + File.separator;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(ASSETS_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取所有的城市
     *
     * @return
     */
    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new City(name, pinyin);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }


    /**
     * 通过名字或者拼音搜索
     *
     * @param keyword
     * @return
     */
    public List<City> searchCity(final String keyword) {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME +
                " where name like \"%" + keyword +
                "%\" or pinyin like \"%" + keyword + "%\"", null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String pinyin = cursor.getString(cursor.getColumnIndex(PINYIN));
            city = new City(name, pinyin);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    /**
     * a-z排序
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
