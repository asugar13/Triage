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
		//open();
	}
	
	/**
	 * Manages opening creation, and upgrading of SQLite databases
	 *
	 */
	//NOT sure if needed
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
	 * Checks if the SQLite database exists
	 * @return whether the SQLite database exists
	 */
	public boolean databaseExists(){
		File file = new File(databasePath);
		return file.exists();
	}
	
	/**
	 * Opens the database for reading/writing
	 */
	public void open(){
		File file = new File(databasePath);
		
		if(!file.exists()){
			//Create new database with specified tables
			Log.d("HERE","HERE");
			mDatabase = new DatabaseHelper(context).getWritableDatabase();
			mDatabase.execSQL("CREATE TABLE IF NOT EXISTS patient_records(health_card_number TEXT,"
					+ "name TEXT,date_of_birth TEXT,seen_by_doctor TEXT,vitals TEXT);");

		}else{
			mDatabase = new DatabaseHelper(context).getWritableDatabase();
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
