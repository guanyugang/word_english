package mytest.english;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class Chapter extends Activity {
    private static DBhelper dbHelper;
    private static final String EXCEPTION = "exception";
    private SQLiteDatabase db;
    private ListView listView;
    private SQLiteDatabase mDatabase;
    private Button btn;
    private String data[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter);
        dbHelper=new DBhelper(this);
        db=gDB();
        btn=(Button)findViewById(R.id.tj);
        listView = (ListView)findViewById(R.id.ch_listview);
        Intent intent = getIntent();
        final String data = intent.getStringExtra("part");
        final List<String> listdata = new ArrayList<String>();
        Cursor cursor = (Cursor) db.query("table_chapter",null,"Part=?",new String[]{data},null,null,"Chapter ASC ");
        try{
        while(cursor.moveToNext()){
            String Chapter = cursor.getString(cursor.getColumnIndex("Chapter"));
            String ChapterName = cursor.getString(cursor.getColumnIndex("ChapterName"));

            String string = "   "+"Chapter"+":"+Chapter+"  "+ChapterName;
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
                Intent intent = new Intent(Chapter.this,word.class);
                TextView text=(TextView)view.findViewById(R.id.text);
                text.getText();
                String str = (String) text.getText();
                str=str.substring(str.indexOf(":"));
                str=str.substring(1,2);
                intent.putExtra("part", data);
                intent.putExtra("chapter", str);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, final View view, int i, long l) {
                final TextView text=(TextView)view.findViewById(R.id.text);
                String str = (String) text.getText();
                str=str.substring(str.indexOf(":"));
                str=str.substring(str.indexOf(" "));
                str=str.substring(2);
                Log.v("111",str );
                AlertDialog alertDialog2 = new AlertDialog.Builder(Chapter.this)
                        .setTitle("删除")
                        .setMessage("是否删除Chapter:"+str+"？")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String str = (String) text.getText();
                                str=str.substring(str.indexOf(":"));
                                str=str.substring(str.indexOf(" "));
                                str=str.substring(2);
                                Log.v("111",str );

                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                db.delete("table_chapter","ChapterName = ?",new String[]{str});

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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chapter.this);
                View view1 = View.inflate(Chapter.this, R.layout.dialog_setview, null);
                final EditText et = (EditText) view1.findViewById(R.id.example_et);
                final EditText et2 = (EditText) view1.findViewById(R.id.example_th);

                alertDialog
                        .setTitle("请输入Chapter号和Chapter名")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = gDB();
                                String a = et.getText().toString();
                                String b = et2.getText().toString();
                                if (et.getText().toString().equals("") || et2.getText().toString().equals("")) {
                                    Toast.makeText(Chapter.this, "添加失败，请输入例句和翻译", Toast.LENGTH_SHORT).show();
                                } else {


                                    int c = Integer.parseInt(a);


                                    ContentValues values = new ContentValues();

                                    values.put("Chapter", c);
                                    values.put("ChapterName",b);
                                    values.put("Part", data);
                                    db.insert("table_chapter", null, values);
                                    values.clear();
                                    String string = "   "+"Chapter"+":"+c+"  "+b;
                                    listdata.add(string);
                                    arrayAdapter.notifyDataSetChanged();

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
    }
    public static SQLiteDatabase gDB(){
        return dbHelper.getWritableDatabase();
    }
}
