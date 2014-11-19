package com.example.movietrailerapp;

import android.annotation.SuppressLint;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.Log;
import android.widget.Toast;

public class DBAdapter {
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TITLE = "title";
	public static final String KEY_RATING = "rating";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_IMAGENAME = "imageName";
	public static final String KEY_TRAILERNAME = "trailerName";
	public static final String TAG = "DBAdapter";
	
	public static final String DATABASE_NAME = "Movie Trailers";
	public static final String DATABASE_TABLE = "movie";
	public static final int DATABASE_VERSION = 2;
	
	public static final String DATABASE_CREATE = "create table movie (id integer primary key autoincrement, "
			+ "title text not null, rating integer, description text not null, imageName text not null, trailerName text not null);";

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public DBAdapter(Context ctx){
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context context)
		{
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
		}
		
		public void onCreate(SQLiteDatabase db)
		{
			try{
				db.execSQL(DATABASE_CREATE);
			}catch(SQLException e){
				e.printStackTrace();
			}		
		}//end method onCreate
		
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			
			Log.w(TAG, "Updrading data from version " + oldVersion + "  to "
				+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS movie");
			onCreate(db);
		}
	}
		
		//open the database
		public DBAdapter open() throws SQLException{
			
			db = DBHelper.getWritableDatabase();
			return this;
			
		}
		
		//close the database
		public void close(){
			DBHelper.close();
		}
		
		//insert a contact
		public long insertMovie(String title, String description, String imageName, String trailerName){
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_TITLE, title);
			initialValues.put(KEY_DESCRIPTION, description);
			initialValues.put(KEY_IMAGENAME, imageName);
			initialValues.put(KEY_TRAILERNAME, trailerName);
			return db.insert(DATABASE_TABLE, null, initialValues);
		}
		
		//delete contact
		public boolean delelteContact(long rowId){
			return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0 ;
		}
		
		//retrieve all contacts
		public Cursor getAllContacts(){
			return db.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_RATING,KEY_DESCRIPTION, KEY_IMAGENAME, KEY_TRAILERNAME}, null,null,null,null,null);
		}
		
		//retrieve an individual contact
		@SuppressLint("NewApi")
		public Cursor getContact(long rowId)throws SQLException{
			
			Cursor mCursor = db.query(true, DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_RATING,KEY_DESCRIPTION, KEY_IMAGENAME, KEY_TRAILERNAME}, KEY_ROWID + "=" + rowId, null, null, null, null, null, null);
			if(mCursor != null){
				mCursor.moveToFirst();
			}
			return mCursor;
		}
		
		//update a contact
		public boolean updateContact(long rowId, String title, int rating, String description, String imageName, String trailerName){
			ContentValues args = new ContentValues();
			args.put(KEY_TITLE, title);
			args.put(KEY_RATING, rating);
			args.put(KEY_DESCRIPTION, description);
			args.put(KEY_IMAGENAME, imageName);
			args.put(KEY_TRAILERNAME, trailerName);
			return db.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
		}
		
	
	
	
}
