package mytest.english;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class Fragment2 extends Fragment {
	private static final String EXCEPTION = "exception";
	private static DBhelper dbHelper;
	private SQLiteDatabase db;
	private View layoutView;
	private EditText et;
	private ListView listView1;
	private Button btn;
	private List<String> listdata1;
	private ArrayAdapter<String> arrayAdapter1;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment2, null);
		et = (EditText)layoutView.findViewById(R.id.word_edit);
		btn = (Button) layoutView.findViewById(R.id.search);

		listView1 = (ListView)layoutView.findViewById(R.id.word_listview);
		listdata1 = new ArrayList<String>();
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				arrayAdapter1.clear();
				String data;
				dbHelper=new DBhelper(getActivity());
				db=gDB();
				data=et.getText().toString();

				Cursor cursor = (Cursor) db.query("table_word",null,"Word LIKE ?",new String[]{"%"+data+"%"},null,null,null);
				if(cursor.getCount()==0){
					cursor=(Cursor) db.query("table_word",null,"WordTranslation LIKE ?",new String[]{"%"+data+"%"},null,null,null);
					if(cursor.getCount()==0){
						cursor=(Cursor) db.query("table_chapter",null,"ChapterName LIKE ?",new String[]{"%"+data+"%"},null,null,null);
						while(cursor.moveToNext()){
							String chapter = cursor.getString(cursor.getColumnIndex("Chapter"));
							String part = cursor.getString(cursor.getColumnIndex("Part"));
							cursor=(Cursor) db.query("table_word",null,"Chapter = ? and Part=?",new String[]{chapter,part},null,null,null);

							while(cursor.moveToNext()){
								String Word = cursor.getString(cursor.getColumnIndex("Word"));
								String WordTranslation = cursor.getString(cursor.getColumnIndex("WordTranslation"));

								String string = "  "+Word+":"+WordTranslation;
								//Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
								listdata1.add(string);
							}
							Toast.makeText(getActivity(), "搜索成功", Toast.LENGTH_SHORT).show();

						}


					}
				}
					try{
						while(cursor.moveToNext()){
							String Word = cursor.getString(cursor.getColumnIndex("Word"));
							String WordTranslation = cursor.getString(cursor.getColumnIndex("WordTranslation"));

							String string = "  "+Word+":"+WordTranslation;
							//Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
							listdata1.add(string);
						}
						Toast.makeText(getActivity(), "搜索成功", Toast.LENGTH_SHORT).show();
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


				arrayAdapter1.notifyDataSetChanged();
			}
		});
		arrayAdapter1 = new ArrayAdapter<String>(getActivity(),R.layout.list_item,listdata1);//listdata和str均可
		arrayAdapter1.notifyDataSetChanged();
		listView1.setAdapter(arrayAdapter1);
		listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Intent intent = new Intent(getActivity(),word_example.class);
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
		return layoutView;
	}
	public static SQLiteDatabase gDB(){
		return dbHelper.getWritableDatabase();
	}
}
