package mytest.english;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "english.db";
    // 数据库表名
    public static final String TABLE_NAME = "table_part";
    // 数据库版本号
    public static final int DB_VERSION = 1;
    public static final String PartName="PartName";
    public static final String PART = "part";
    public DBhelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " +
                TABLE_NAME +
                "(_id integer primary key autoincrement, " +
                PART + " int, " +
                PartName + " varchar(20)"
                + ")";
        db.execSQL(sql);
        String sql1 = "create table " +
                "table_chapter" +
                "(_id integer primary key autoincrement, " +
                "Chapter" + " int, " +
                "ChapterName" + " varchar, " +
                "Part" + " int"
                + ")";
        db.execSQL(sql1);
        String sql2 = "create table " +
                "table_word" +
                "(_id integer primary key autoincrement, " +
                "Word" + " varchar, " +
                "WordTranslation" + " varchar, " +
                "Part" + " int, "+
                "Chapter" + " int"
                + ")";
        db.execSQL(sql2);
        String sql3 = "create table " +
                "table_example" +
                "(_id integer primary key autoincrement, " +
                "Example" + " varchar(100), " +
                "ExampleTranslation" + " varchar(100), " +
                "Word" + " varchar"
                + ")";
        db.execSQL(sql3);
        String sql4 = "create table " +
                "table_word_like" +
                "(_id integer primary key autoincrement, " +
                "Word" + " varchar(100), " +
                "WordTranslation" + " varchar "
                + ")";
        db.execSQL(sql4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
