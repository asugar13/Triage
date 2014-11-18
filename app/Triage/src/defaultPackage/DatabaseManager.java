package defaultPackage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
/**
 * Manages reading,writing, and copying of hospital database 
 *
 */
public class DatabaseManager {
	//SingleTON???
	private static DatabaseManager instance = null;
	/** Name of the database file*/
	private static final String databaseName = "EmergencyRoomDatabase.db";
	/** File path to the databasefile*/
	private static final String databasePath = "/data/data/com.example.triage/databases/";
	/** DatabaseHelper for this datbase than manages the opening of SQLite files*/
	private DatabaseHelper dbHelper;
	/**SQLitedatabase which will be read from and modified */
	private SQLiteDatabase mDatabase;
	/**context of the android application*/
	private Context context;
	//
	public DatabaseManager(Context context){
		this.context = context;
		//dbHelper = new DatabaseHelper(context);
		open();
	}
	
	/**
	 * Manages opening creation, and upgrading of SQLite databases
	 *
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{

		public DatabaseHelper(Context context) {
			super(context, databaseName, null, 1);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Not necessary for this app
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//not necessary for this app
			
		}
	}
	/**
	 * Opens the database for reading/writing
	 */
	public void open(){
		File file = new File(databasePath);
		if(!file.exists()){
			file.mkdirs();
			AssetManager assetManager = context.getResources().getAssets();
			try{
				Log.d("ASSETS","COPYING DB FROM ASSETS -> internal");
				InputStream is = assetManager.open("" + databaseName);
				OutputStream os = new FileOutputStream(databasePath + databaseName);
				copyDatabase(is,os);
			}catch (IOException io){
				io.printStackTrace();
			}
		}

		mDatabase = new DatabaseHelper(context).getWritableDatabase();
	}
	
	/**
	 * Copies the database from the given input stream to the given output stream
	 * @param is: predefined database located in assets folder
	 * @param os: location to copy database too
	 */
	public void copyDatabase(InputStream is, OutputStream os){
		byte[] buffer = new byte[1024];
		int length;
		try{
			while((length = is.read(buffer))>0){
				os.write(buffer,0,length);
				
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	/**
	 * Close the SQLiteDatabase
	 */
	public void close(){
		dbHelper.close();
	}
	

	public void modifyRow(String table,ContentValues newValues,String whereClause){
		mDatabase.update(table, newValues, whereClause, null);
	}

	public boolean addRow(ContentValues newValues,String tableName){
		return (mDatabase.insert(tableName,null,newValues) != -1);
	}

	/**
	 * Return if a row specified by a sql Where statement exists
	 * @param tableName
	 * @param sqlWhere
	 * @return
	 */
	public boolean rowExists(String tableName,String sqlWhere){
		Cursor c = mDatabase.query(tableName,null,sqlWhere,null,null,null,null);
		if(c.getCount() !=0){
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Return a cursor object with all data from a given table
	 * @param tableName
	 * @return
	 */
	public Cursor getAllRows(String tableName){
		Cursor c = mDatabase.query(tableName,  null, null,null,null,null,null);
		return c;
	}
	
	public Cursor getRow(String tableName,String sqlWhere,String[] columns){
		return mDatabase.query(tableName, columns,sqlWhere, null,null,null,null);
	}
	
	
	
	
	

}
