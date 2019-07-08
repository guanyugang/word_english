package mytest.english;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sf.excelutils.ExcelUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import static android.content.ContentValues.TAG;

public class Fragment3 extends Fragment {
	/**Sheet表, Excel表中的底部的表名*/
	private WritableSheet mWritableSheet;
	/**Excel工作簿*/
	private WritableWorkbook mWritableWorkbook;
	private static final String EXCEPTION  = "exception";
	private View layoutView;
	private Button btn;
	private Button btn1;
	private File file;
	private List<String> s;
	private EditText et;
	private DBhelper dbHelper;
	private String table_w[]={"Part","Chapter","Word","WordTranslation","Example","ExampleTranslation"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		layoutView = inflater.inflate(R.layout.fragment3, null);
		btn=(Button)layoutView.findViewById(R.id.dr);
		btn1=(Button)layoutView.findViewById(R.id.dc);
		et=(EditText)layoutView.findViewById(R.id.et1) ;
		s=new ArrayList<String>();
		dbHelper = new DBhelper(getActivity());
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				readExcelToDB(getActivity(),et.getText().toString());
			}
		});
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				try {
					initData();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (WriteException e) {
					e.printStackTrace();
				}
			}
		});
		return layoutView;
	}


	/**
	 * 读取excel数据到数据库里
	 * @param context
	 */
	private void readExcelToDB(Context context,String name) {
		try {
			InputStream is = context.getAssets().open(name+".xls");
			Workbook book = Workbook.getWorkbook(is);
			book.getNumberOfSheets();
			// 获得第一个工作表对象
			Sheet sheet = book.getSheet(0);
			int Rows = sheet.getRows();
			ExtraWord info = null;
			for (int i = 1; i < Rows; ++i) {
				String Part = (sheet.getCell(0, i)).getContents();
				String Chapter = (sheet.getCell(1, i)).getContents();
				String Word = (sheet.getCell(2, i)).getContents();
				String WordTranslation = (sheet.getCell(3, i)).getContents();
				String Example = (sheet.getCell(4, i)).getContents();
				String ExampleTranslation = (sheet.getCell(5, i)).getContents();
				int a=0;
				int b=0;
				try{
				 a = Integer.parseInt(Part);
				 b = Integer.parseInt(Chapter);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
				info = new ExtraWord(a, b, Word, WordTranslation, Example, ExampleTranslation);
				saveInfoToDataBase(info);
			}
			book.close();

		} catch (Exception e) {

			Log.e(TAG, EXCEPTION, e);
		}
	}
	/**
	 * 保存该条数据到数据库
	 * @param info excel中的某条数据
	 */
	private void saveInfoToDataBase(ExtraWord info) {
		if (dbHelper == null) {
			return;
		}
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		try {
			ContentValues values = new ContentValues();
			Cursor cursor = (Cursor) db.query("table_word",null,"Word = ?",new String[]{info.getWord()},null,null,null);
			if(cursor.getCount()==0) {
				values.put("Part", info.getPart());
				values.put("Chapter", info.getChapter());
				values.put("Word", info.getWord());
				values.put("WordTranslation", info.getWordTranslation());

				db.insert("table_word", null, values);
				values.clear();
				values.put("Example", info.getExample());
				values.put("ExampleTranslation", info.getExampleTranslation());
				values.put("Word", info.getWord());
				db.insert("table_example", null, values);
				values.clear();

			}

		} catch (SQLiteException e) {
			Log.e(TAG, EXCEPTION, e);
		} catch (Exception e){
			Log.e(TAG, EXCEPTION, e);
		} finally {
			if (db != null) {
				db.close();
			}
		}
		Toast.makeText(getActivity(), "导入成功", Toast.LENGTH_SHORT).show();
	}
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		String dir = sdDir.toString();
		return dir;

	}
	public void initData() throws IOException, WriteException {
		//Log.e(TAG, "initData: --------------------------------");
		file = new File(getSDPath() + "/My");
		makeDir(file);
		String filename = file.toString() + "/word.xls";

		OutputStream os = new FileOutputStream(filename);





		try {
			if (mWritableWorkbook == null) {
				// 创建Excel工作簿
				mWritableWorkbook = Workbook.createWorkbook(os);
			}
			if (mWritableSheet == null) {
				// 创建一个标签页
				mWritableSheet =  mWritableWorkbook.createSheet("word", 0);
			}

			for(int i = 0;i<table_w.length;i++){
				Label label = new Label(i, 0, table_w[i]);
				mWritableSheet.addCell(label);
			}
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			try {
				String iad[]={""};

				Cursor cursor = (Cursor) db.query("table_word",null,"Word LIKE ?", new String[]{"%"},null,null,null);
				int x=1;

				while(cursor.moveToNext()) {
					String Part = cursor.getString(cursor.getColumnIndex("Part"));
					String Chapter = cursor.getString(cursor.getColumnIndex("Chapter"));
					String Word = cursor.getString(cursor.getColumnIndex("Word"));
					String WordTranslation = cursor.getString(cursor.getColumnIndex("WordTranslation"));
					s.add(Word);


					iad= new String[]{Part, Chapter, Word, WordTranslation};
					for(int i = 0;i<4;i++){
						Label label = new Label(i, x, iad[i]);
						mWritableSheet.addCell(label);
					}
					x++;
				}
				db.close();
				cursor.close();
				db = dbHelper.getWritableDatabase();
				x=1;
				for(int d=0;d<s.size();d++){
					cursor = (Cursor) db.query("table_example",null,"Word = ?",new String[]{s.get(d)},null,null,null);
					while (cursor.moveToNext()) {

						String Example = cursor.getString(cursor.getColumnIndex("Example"));
						String ExampleTranslation = cursor.getString(cursor.getColumnIndex("ExampleTranslation"));
						iad= new String[]{Example, ExampleTranslation};
						int k=0;
						for(int i = 4;i<6;i++){

							Label label = new Label(i, x, iad[k]);
							mWritableSheet.addCell(label);
							k++;
						}
						x++;
					}

					cursor.close();
				}
				Toast.makeText(getActivity(),"储存到："+filename, Toast.LENGTH_SHORT).show();


			}
			catch (SQLiteException e) {
				Log.e(TAG, EXCEPTION, e);
			} catch (Exception e){
				Log.e(TAG, EXCEPTION, e);
			} finally {
			if (db != null) {
				db.close();
			}
		}
			//最后，写入，关闭
		mWritableWorkbook.write();
		mWritableWorkbook.close();
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdirs();
	}
	/**
	 * 添加字符串
	 * @param col 列号
	 * @param row 行号
	 * @param text 文本
	 * @throws WriteException
	 */



}
