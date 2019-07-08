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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static mytest.english.word_example.gDB;

public class Fragment4 extends Fragment {
	private static DBhelper dbHelper;
	private SQLiteDatabase db;
	private View layoutView;
	private ListView listView;
	private List<String> listdata;
	private static final String EXCEPTION = "exception";
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment4, null);
		listView=(ListView)layoutView.findViewById(R.id.word_love);



		dbHelper=new DBhelper(getActivity());
		db=gDB();

		listdata = new ArrayList<String>();




		Cursor cursor = (Cursor) db.query("table_word_like",null,"Word LIKE ?",new String[]{"%"},null,null,null);
		try {
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
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(),R.layout.list_item,listdata);//listdata和str均可
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
