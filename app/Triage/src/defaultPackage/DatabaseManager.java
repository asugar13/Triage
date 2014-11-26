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
 * Manages creating, reading, and writing to EmergencyRoomDatabase.db.
 */
public class DatabaseManager {
	/** Name of the database file.*/
	private static final String DATABASE_NAME = "EmergencyRoomDatabase.db";
	/** File path to the databasefile.*/
	private static final String DATABASE_PATH = "/data/data/com.example.triage/databases/";
	/** DatabaseHelper for this datbase than manages the opening of SQLite files.*/
	private DatabaseHelper dbHelper;
	/**SQLitedatabase which will be read from and modified.*/
	private SQLiteDatabase mDatabase;
	/**context of the android application.*/
	private Context context;
	
	/**
	 * Instantiates the DatabaseManager.
	 * @param context of the application.
	 */
	public DatabaseManager(Context context){
		this.context = context;
	}
	
	/**
	 * Manages opening creation, and upgrading of SQLite databases.
	 * Creates a new database if does not exist.
	 */
	private static class DatabaseHelper extends SQLiteOpenHelper{
		/**
		 * Constructs a new databaseHelper,
		 * creating or opening an existing database of databaseName.
		 * @param context used to open or create the database.
		 */
		public DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, 1);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		/**
		 * Not implemented Creation handled by SQLiteOpenHelper constructor.
		 */
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
		}
		/**
		 * Not implemented no upgrading of database necessary at this time.
		 */
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}
	
	}
	/**
	 * Checks if the SQLite database exists.
	 * @return whether the SQLite database exists.
	 */
	public boolean databaseExists(){
		File file = new File(DATABASE_PATH);
		return file.exists();
	}
	
	/**
	 * Return if a specified tableName exists in the database.
	 * @param tableName name of the table to check for.
	 * @return whether the specified tableName exists.
	 */
	public boolean tableExists(String tableName){
		//Solution from: http://stackoverflow.com/questions/3058909/
		// how-does-one-check-if-a-table-exists-in-an-android-sqlite-database/7863401#7863401
		Cursor c = mDatabase.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name= '" + tableName + "';",null);
		if(c != null){
			if(c.getCount() >0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Opens the database for reading/writing.
	 */
	public void open(){
		mDatabase = new DatabaseHelper(context).getWritableDatabase();

	}
	
	/**
	 * Creates table in database with specified columns and column data types.
	 * @param tableName name of table to create.
	 * @param columns list of columns for the table.
	 * @param types types of data for the columns in the table.
	 */
	public void createTable(String tableName,String[] columns,String[] types){
		String sqlStatement = "CREATE TABLE IF NOT EXISTS ";
		sqlStatement = sqlStatement + tableName + "(";
		for(int i =0;i < columns.length;i++){
			sqlStatement = sqlStatement + columns[i] + " " + types[i] + ",";
		}
		sqlStatement = sqlStatement.substring(0,sqlStatement.length() - 1) +  ");";
		
		mDatabase.execSQL(sqlStatement);
	}

	/**
	 * Close the SQLiteDatabase.
	 */
	public void close(){
		dbHelper.close();
	}
	
	/**
	 * Modifies the row specified by whereClause in the given table.
	 * @param table table to modify.
	 * @param newValues ContentValues new values to put in row.
	 * @param whereClause specifies which row to replace.
	 */
	public void modifyRow(String table,ContentValues newValues,String whereClause){
		mDatabase.update(table, newValues, whereClause, null);
	}
	/**
	 * Adds a row to the specified table.
	 * @param newValues ContentValues new values to add to table.
	 * @param tableName table to add values to.
	 */
	public void addRow(ContentValues newValues,String tableName){
		 mDatabase.insert(tableName,null,newValues);
	}

	/**
	 * Return if a specified row exists in a given table.
	 * @param tableName table to check.
	 * @param sqlWhere sqlwhere statement to check for the row.
	 * @return  boolean Whether the specified row exists.
	 */
	public boolean rowExists(String tableName,String sqlWhere){
		Cursor c = mDatabase.query(tableName,null,sqlWhere,null,null,null,null);
		if(c.getCount() !=0){
			return true;
		}
		return false;
	}

	
	/**
	 * Return a cursor object with all data from a given table.
	 * @param tableName the name of the table to query.
	 * @return c a cursor with all table information.
	 */
	public Cursor getAllRows(String tableName){
		Cursor c = mDatabase.query(tableName,  null, null,null,null,null,null);
		return c;
	}
	
	/**
	 * Gets the data in a specific row of a table.
	 * @param tableName specifies the name of the table to query.
	 * @param sqlWhere specifies what the row should be returned.
	 * @param columns The columns requested.
	 * @return Cursor mapping to all the data in the specified row.
	 */
	public Cursor getRow(String tableName,String sqlWhere,String[] columns){
		return mDatabase.query(tableName, columns,sqlWhere, null,null,null,null);
	}
}
