package mytest.english;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;

public class word_example extends Activity {
    private static DBhelper dbHelper;
    private SQLiteDatabase db;
    private TextView textView_word;
    private TextView textView_example;
    private static final String EXCEPTION = "exception";

    private  String data;
    private Button btn;
    private Button btn1;
    private ImageView bf;
    private ImageView sc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_example);
        dbHelper=new DBhelper(this);
        db=gDB();
        textView_word = (TextView)findViewById(R.id.word);
        textView_example=(TextView)findViewById(R.id.example) ;
        sc=(ImageView)findViewById(R.id.sc) ;
        Intent intent = getIntent();
        bf=(ImageView) findViewById(R.id.bf);
        data = intent.getStringExtra("Word");
        data=data.substring(2);
        textView_word.setText(data);
        btn=(Button)findViewById(R.id.add_work) ;
        btn1=(Button)findViewById(R.id.dele);
        Cursor cursor = (Cursor) db.query("table_example",null,"Word=?",new String[]{data},null,null,null);
        try {
        while(cursor.moveToNext()){
            String Example = cursor.getString(cursor.getColumnIndex("Example"));
            String ExampleTranslation = cursor.getString(cursor.getColumnIndex("ExampleTranslation"));
            String [] temp = null;
            temp = Example.split("；");

            String [] temp1 = null;
            temp1 = ExampleTranslation.split("；");

            String str = "";
            for(int i=0;i<temp.length;i++){
//                Toast.makeText(this, temp[i], Toast.LENGTH_SHORT).show();
//                Log.v("11111",temp[i] );
                str=str+temp[i]+"\n"+temp1[i]+"\n";
                Log.v("11111",str );
            }
            textView_example.setText(str);
        }
    } catch (
    SQLiteException e) {
        Log.e(TAG, EXCEPTION, e);
    }  catch (Exception e){
        Log.e(TAG, EXCEPTION, e);
    } finally {
        if (cursor != null) {
            cursor.close();
            cursor = null;
        }
        if (db != null) {
            db.close();
        }
    }
        db.close();

        db = dbHelper.getWritableDatabase();
         cursor = (Cursor) db.query("table_word_like",null,"Word=?",new String[]{data},null,null,null);
        if(cursor.getCount()!=0){
            sc.setImageResource(R.drawable.sc_pressed);
        }
        db.close();
    bf.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AssetManager am = getAssets();
            String Filename=data+".mp3";
            Filename = Filename.replace(" ","_");
            //AssetManager am = getResources().getAssets();
            if(Filename.indexOf("/") != -1)
            {
                Filename=Filename.substring(Filename.indexOf("/"));
                Filename=Filename.substring(1);
            }
            AssetFileDescriptor afd = null;
            Log.v("11111",Filename );
            try {
                afd = am.openFd(Filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDescriptor fd = afd.getFileDescriptor();
            MediaPlayer mp2 = new MediaPlayer();
            mp2.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mp2.setDataSource(fd, afd.getStartOffset(), afd.getLength());

                mp2.prepare();
                mp2.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    });
    sc.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            sc.setImageResource(R.drawable.sc_pressed);
            db = dbHelper.getWritableDatabase();
            Cursor cursor = (Cursor) db.query("table_word_like", null, "Word=?", new String[]{data}, null, null, null);
            if (cursor.getCount() != 0) {
                sc.setImageResource(R.drawable.sc);
                db.delete("table_word_like", "Word=?", new String[]{data});
                Toast.makeText(word_example.this, "取消收藏", Toast.LENGTH_SHORT).show();
            } else {
                db.close();
                try {
                    ContentValues values = new ContentValues();


                    values.put("Word", textView_word.getText().toString());

                    String WordTranslation = null;
                    db = dbHelper.getWritableDatabase();
                    cursor = (Cursor) db.query("table_word", null, "Word=?", new String[]{textView_word.getText().toString()}, null, null, null);
                    try {
                        while (cursor.moveToNext()) {
                            WordTranslation = cursor.getString(cursor.getColumnIndex("WordTranslation"));
                        }
                    } catch (
                            SQLiteException e) {
                        Log.e(TAG, EXCEPTION, e);
                    } catch (Exception e) {
                        Log.e(TAG, EXCEPTION, e);
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                            cursor = null;
                        }

                    }

                    values.put("WordTranslation", WordTranslation);
                    db.insert("table_word_like", null, values);
                    db.close();
                    values.clear();
                    Toast.makeText(word_example.this, "收藏成功", Toast.LENGTH_SHORT).show();
                } catch (SQLiteException e) {
                    Log.e(TAG, EXCEPTION, e);
                } catch (Exception e) {
                    Log.e(TAG, EXCEPTION, e);
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        }
    });


    btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(word_example.this);
            View view1 = View.inflate(word_example.this, R.layout.dialog_setview, null);
            final EditText et = (EditText) view1.findViewById(R.id.example_et);
            final EditText et2 = (EditText) view1.findViewById(R.id.example_th);

            alertDialog
                    .setTitle("请输入例句和翻译")
                    .setIcon(R.mipmap.ic_launcher)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            db = gDB();
                            String a = et.getText().toString();
                            String b = et2.getText().toString();
                            if (et.getText().toString().equals("") || et2.getText().toString().equals("")) {
                                Toast.makeText(word_example.this, "添加失败，请输入例句和翻译", Toast.LENGTH_SHORT).show();
                            } else {
                                String Example = "";
                                String ExampleTranslation = "";
                                Cursor cursor = (Cursor) db.query("table_example", null, "Word=?", new String[]{data}, null, null, null);
                                try {
                                    while (cursor.moveToNext()) {
                                        Example = cursor.getString(cursor.getColumnIndex("Example"));
                                        ExampleTranslation = cursor.getString(cursor.getColumnIndex("ExampleTranslation"));
                                    }
                                    ContentValues values = new ContentValues();
                                    Example = Example + "；" + a;
                                    ExampleTranslation = ExampleTranslation + "；" + b;
                                    values.put("Example", Example);
                                    values.put("ExampleTranslation", ExampleTranslation);
                                    values.put("Word", data);
                                    String[] temp = null;
                                    temp = Example.split("；");
                                    db.delete("table_example", "Word = ?", new String[]{data});
                                    String[] temp1 = null;
                                    temp1 = ExampleTranslation.split("；");

                                    String str = "";
                                    for (int c = 0; c < temp.length; c++) {
//                Toast.makeText(this, temp[i], Toast.LENGTH_SHORT).show();
//                Log.v("11111",temp[i] );
                                        str = str + temp[c] + "\n" + temp1[c] + "\n";
                                        Log.v("11111", str);
                                    }
                                    textView_example.setText(str);
                                    db.insert("table_example", null, values);
                                    values.clear();
                                } catch (
                                        SQLiteException e) {
                                    Log.e(TAG, EXCEPTION, e);
                                } catch (Exception e) {
                                    Log.e(TAG, EXCEPTION, e);
                                } finally {
                                    if (cursor != null) {
                                        cursor.close();
                                        cursor = null;
                                    }
                                    if (db != null) {
                                        db.close();
                                    }
                                }
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })

                    .setView(view1)
                    .create();
            final AlertDialog show = alertDialog.show();


        }
    });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_example.setText("");
                db=gDB();
                db.delete("table_example", "Word = ?", new String[]{data});
            }
        });
    }
    public static SQLiteDatabase gDB(){
        return dbHelper.getWritableDatabase();
    }
}
