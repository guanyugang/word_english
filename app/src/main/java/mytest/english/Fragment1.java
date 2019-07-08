package mytest.english;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Fragment1 extends Fragment {
    private static DBhelper dbHelper;
    private SQLiteDatabase db;
	private View layoutView;
	private ListView listView;
    private static final String EXCEPTION = "exception";
	private ArrayList partlist;
	private Button btn;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment1, null);
        dbHelper=new DBhelper(getActivity());
		db=gDB();
        btn=(Button)layoutView.findViewById(R.id.tj);
		listView = (ListView) layoutView.findViewById(R.id.part_listview);
        final List<String> listdata = new ArrayList<String>();
        Cursor cursor = (Cursor) db.query("table_part",null,null,null,null,null,"part ASC");
        try {

        while(cursor.moveToNext()){
            String part = cursor.getString(cursor.getColumnIndex("part"));
            String partname = cursor.getString(cursor.getColumnIndex("PartName"));

            String string = "   "+"Part"+":"+part+"  "+partname;
            listdata.add(string);
        }
    } catch (SQLiteException e) {
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
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item,listdata);//listdata和str均可
        listView.setAdapter(arrayAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),Chapter.class);
                TextView text=(TextView)view.findViewById(R.id.text);
                text.getText();
                String str = (String) text.getText();
                str=str.substring(str.indexOf(":"));
                str=str.substring(1,2);

                intent.putExtra("part", str);
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
                AlertDialog alertDialog2 = new AlertDialog.Builder(getActivity())
                        .setTitle("删除")
                        .setMessage("是否删除Part:"+str+"？")
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
                                db.delete("table_part","PartName = ?",new String[]{str});

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
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                View view1 = View.inflate(getActivity(), R.layout.dialog_setview, null);
                final EditText et = (EditText) view1.findViewById(R.id.example_et);
                final EditText et2 = (EditText) view1.findViewById(R.id.example_th);

                alertDialog
                        .setTitle("请输入Part号和Part名")
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {//添加"Yes"按钮
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db = gDB();
                                String a = et.getText().toString();
                                String b = et2.getText().toString();
                                if (et.getText().toString().equals("") || et2.getText().toString().equals("")) {
                                    Toast.makeText(getActivity(), "添加失败，请输入例句和翻译", Toast.LENGTH_SHORT).show();
                                } else {


                                        int c = Integer.parseInt(a);


                                        ContentValues values = new ContentValues();

                                        values.put("part", c);
                                        values.put("PartName",b);
                                        db.insert("table_part", null, values);
                                        values.clear();
                                    String string = "   "+"Part"+":"+c+"  "+b;
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
		return layoutView;
	}

    public static SQLiteDatabase gDB(){
        return dbHelper.getWritableDatabase();
    }

}