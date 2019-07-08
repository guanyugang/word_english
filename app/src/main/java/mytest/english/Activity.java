package mytest.english;

import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;


public class Activity extends FragmentActivity {
    private Thread newThread; //声明一个子线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_);
        init();
        newThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    Intent intent =new Intent(Activity.this,MainActivity.class);
                    //启动
                    startActivity(intent);
                    finish();
                 } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }

            }
        });

        newThread.start();

    }
    protected void init() {
        DBhelper dbHelper = new DBhelper(Activity.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int amount = 0;
        Cursor c = db.rawQuery("select * from table_part", null);
        amount = c.getCount();
        if (amount == 0) {
            ContentValues values = new ContentValues();
            values.put(DBhelper.PART, 1);
            values.put(DBhelper.PartName, "生活常识与社交篇");
            db.insert(DBhelper.TABLE_NAME, null, values);
            values.clear();
            values.put(DBhelper.PART, 2);
            values.put(DBhelper.PartName, "旅游篇");
            db.insert(DBhelper.TABLE_NAME, null, values);
            values.clear();
            values.put(DBhelper.PART, 3);
            values.put(DBhelper.PartName, "职场篇");
            db.insert(DBhelper.TABLE_NAME, null, values);
            values.clear();
            values.put(DBhelper.PART, 4);
            values.put(DBhelper.PartName, "学术篇");
            db.insert(DBhelper.TABLE_NAME, null, values);
            values.clear();
            values.put(DBhelper.PART, 5);
            values.put(DBhelper.PartName, "自然人文篇");
            db.insert(DBhelper.TABLE_NAME, null, values);
        }
        int amount1 =0;
        Cursor d = db.rawQuery("select * from table_chapter", null);
        amount1 = d.getCount();
        if (amount1 == 0) {
            ContentValues values = new ContentValues();
            values.put("Chapter", 1);
            values.put("ChapterName", "居家生活");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 2);
            values.put("ChapterName", "个人情况");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 3);
            values.put("ChapterName", "个人健康");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 4);
            values.put("ChapterName", "日常活动");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 5);
            values.put("ChapterName", "就医");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 6);
            values.put("ChapterName", "文艺爱好");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 7);
            values.put("ChapterName", "休闲爱好");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 8);
            values.put("ChapterName", "户外活动");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 9);
            values.put("ChapterName", "体育活动");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 10);
            values.put("ChapterName", "情绪感受");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 11);
            values.put("ChapterName", "人际关系");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 12);
            values.put("ChapterName", "会话技巧");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 13);
            values.put("ChapterName", "交际场合");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 14);
            values.put("ChapterName", "度量衡");
            values.put("Part", 1);
            db.insert("table_chapter", null, values);
            values.clear();

            values.put("Chapter", 1);
            values.put("ChapterName", "行前事宜");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 2);
            values.put("ChapterName", "上飞机前");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 3);
            values.put("ChapterName", "在飞机上");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 4);
            values.put("ChapterName", "下飞机后");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 5);
            values.put("ChapterName", "酒店住宿");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 6);
            values.put("ChapterName", "出行方式");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 7);
            values.put("ChapterName", "自驾游");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 8);
            values.put("ChapterName", "交通");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 9);
            values.put("ChapterName", "饮食");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 10);
            values.put("ChapterName", "观光");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 11);
            values.put("ChapterName", "购物·穿戴");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 12);
            values.put("ChapterName", "商场购物");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 13);
            values.put("ChapterName", "购物·礼物");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 14);
            values.put("ChapterName", "旅途中的麻烦");
            values.put("Part", 2);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 1);
            values.put("ChapterName", "个人背景");
            values.put("Part", 3);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 2);
            values.put("ChapterName", "求职面试");
            values.put("Part", 3);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 3);
            values.put("ChapterName", "公司构成");
            values.put("Part", 3);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 4);
            values.put("ChapterName", "脑力劳动");
            values.put("Part", 3);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 5);
            values.put("ChapterName", "生产劳动");
            values.put("Part", 3);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 1);
            values.put("ChapterName", "学科类型");
            values.put("Part", 4);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 2);
            values.put("ChapterName", "学术领域");
            values.put("Part", 4);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 3);
            values.put("ChapterName", "学校教育");
            values.put("Part", 4);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 4);
            values.put("ChapterName", "学校生活");
            values.put("Part", 4);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 1);
            values.put("ChapterName", "地球");
            values.put("Part", 5);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 2);
            values.put("ChapterName", "生物");
            values.put("Part", 5);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 3);
            values.put("ChapterName", "自然灾害");
            values.put("Part", 5);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 4);
            values.put("ChapterName", "自然物质");
            values.put("Part", 5);
            db.insert("table_chapter", null, values);
            values.clear();
            values.put("Chapter", 5);
            values.put("ChapterName", "节日");
            values.put("Part", 5);
            db.insert("table_chapter", null, values);
            values.clear();

        }
    }
    @Override
    protected void onDestroy() {
        finish();
        super.onDestroy();
    }
}
