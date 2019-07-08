package mytest.english;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class word extends Activity {
    private static DBhelper dbHelper;
    private SQLiteDatabase db;
    private Button btn;
    private ListView listView;
    private static final String EXCEPTION = "exception";
    private SQLiteDatabase mDatabase;
    private int a;
    private int b;
    private List<String> listdata;
    private EditText et_word;
    private EditText et_wordtn;
    private String data[];
    @SuppressLint({"WrongViewCast", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        dbHelper=new DBhelper(this);
        db=gDB();
        listView = (ListView)findViewById(R.id.word_lt);
        btn=(Button)findViewById(R.id.add);

        et_word=(EditText)findViewById(R.id.word);
        et_wordtn=(EditText)findViewById(R.id.wordtranslationl);
        et_word.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
               et_word.setFocusable(true);
               et_word.setFocusableInTouchMode(true);
                return false;
            }

        });
        et_wordtn.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_wordtn.setFocusable(true);
                et_wordtn.setFocusableInTouchMode(true);
                return false;
            }

        });
        Intent intent = getIntent();
        final String data = intent.getStringExtra("chapter");
        final String data1 = intent.getStringExtra("part");
        Log.v("22222",data+data1 );
         listdata = new ArrayList<String>();
        Cursor cursor = (Cursor) db.query("table_word",null,"Part=? and Chapter=?",new String[]{data1,data},null,null,"Word ASC");
        try{
        while(cursor.moveToNext()){
            String Word = cursor.getString(cursor.getColumnIndex("Word"));
            String WordTranslation = cursor.getString(cursor.getColumnIndex("WordTranslation"));

            String string = "  "+Word+":"+WordTranslation;
            listdata.add(string);
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
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,listdata);//listdata和str均可
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(word.this,word_example.class);
                TextView text=(TextView)view.findViewById(R.id.text);
                text.getText();
                String str = (String) text.getText();
                str=str.substring(str.indexOf(" "));
                str=str.substring(0, str.indexOf(":"));
                //Toast.makeText(word.this, str, Toast.LENGTH_SHORT).show();

                intent.putExtra("Word", str);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final TextView text=(TextView)view.findViewById(R.id.text);
                String str = (String) text.getText();
                str=str.substring(str.indexOf(" "));
                str=str.substring(2, str.indexOf(":"));
                AlertDialog alertDialog2 = new AlertDialog.Builder(word.this)
                        .setTitle("删除")
                        .setMessage("是否删除"+str+"？")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String str = (String) text.getText();
                                str=str.substring(str.indexOf(" "));
                                str=str.substring(2, str.indexOf(":"));

                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("table_word","Word = ?",new String[]{str});
                                db.delete("table_example", "Word = ?",new String[]{str} );
                                db.close();
                                listdata.remove(text.getText());
                                arrayAdapter.notifyDataSetChanged();

                            }
                        })

                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alertDialog2.show();

                return true;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_word.getText().toString().equals("")||et_wordtn.getText().toString().equals("")){
                    Toast.makeText(word.this, "添加失败，请输入单词和翻译", Toast.LENGTH_SHORT).show();
                }
                else {
                    String c = et_word.getText().toString();
                    String d = et_wordtn.getText().toString();
                    try {
                        a = Integer.parseInt(data1);
                        b = Integer.parseInt(data);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    saveInfoToDataBase(a, b, c, d);
                    String string = "  " + c + ":" + d;
                    listdata.add(string);
                    arrayAdapter.notifyDataSetChanged();
                    et_word.setText("");
                    et_wordtn.setText("");
                }
            }
        });

    }
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    private void saveInfoToDataBase(int Part, int Chapter, String Word, String WordTranslation) {
        if (dbHelper == null) {
            return;
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put("Part", Part);
            values.put("Chapter", Chapter);
            values.put("Word", Word);
            values.put("WordTranslation", WordTranslation);

            db.insert("table_word", null, values);
            Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            values.clear();
        } catch (SQLiteException e) {
            Log.e(TAG, EXCEPTION, e);
        } catch (Exception e){
            Log.e(TAG, EXCEPTION, e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
    public static SQLiteDatabase gDB(){
        return dbHelper.getWritableDatabase();
    }
}
