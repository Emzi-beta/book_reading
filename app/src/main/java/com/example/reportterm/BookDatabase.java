

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.reportterm.BookInfo;

import java.util.ArrayList;

public class BookDatabase {

	/**
	 * TAG for debugging
	 */
	public static final String TAG = "BookDatabase";

	/**
	 * Singleton instance
	 */
	private static BookDatabase database;


	/**
	 * database name
	 */
	public static String DATABASE_NAME = "book.db";

	/**
	 * table name for BOOK_INFO
	 */
	public static String TABLE_BOOK_INFO = "BOOK_INFO";

    /**
     * version
     */
	public static int DATABASE_VERSION = 1;


    /**
     * Helper class defined
     */
    private DatabaseHelper dbHelper;

    /**
     * Database object
     */
    private SQLiteDatabase db;


    private Context context;

    /**
     * Constructor
     */
	private BookDatabase(Context context) {
		this.context = context;
	}


	public static BookDatabase getInstance(Context context) {
		if (database == null) {
			database = new BookDatabase(context);
		}

		return database;
	}

	/**
	 * open database
	 *
	 * @return
	 */
    public boolean open() {
    	println("opening database [" + DATABASE_NAME + "].");

    	dbHelper = new DatabaseHelper(context);
    	db = dbHelper.getWritableDatabase();

    	return true;
    }

    /**
     * close database
     */
    public void close() {
    	println("closing database [" + DATABASE_NAME + "].");
    	db.close();
    	database = null;
    }

    /**
     * execute raw query using the input SQL
     * close the cursor after fetching any result
     *
     * @param SQL
     * @return
     */
    public Cursor rawQuery(String SQL) {
		println("\nexecuteQuery called.\n");

		Cursor c1 = null;
		try {
			c1 = db.rawQuery(SQL, null);
			println("cursor count : " + c1.getCount());
		} catch(Exception ex) {
    		Log.e(TAG, "Exception in executeQuery", ex);
    	}

		return c1;
	}

    public boolean execSQL(String SQL) {
		println("\nexecute called.\n");

		try {
			Log.d(TAG, "SQL : " + SQL);
			db.execSQL(SQL);
	    } catch(Exception ex) {
			Log.e(TAG, "Exception in executeQuery", ex);
			return false;
		}

		return true;
	}




    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase _db) {
        	// TABLE_BOOK_INFO
        	println("creating table [" + TABLE_BOOK_INFO + "].");

        	// drop existing table
        	String DROP_SQL = "drop table if exists " + TABLE_BOOK_INFO;
        	try {
        		_db.execSQL(DROP_SQL);
        	} catch(Exception ex) {
        		Log.e(TAG, "Exception in DROP_SQL", ex);
        	}

        	// create table
        	String CREATE_SQL = "create table " + TABLE_BOOK_INFO + "("
		        			+ "  _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
		        			+ "  NAME TEXT, "
		        			+ "  AUTHOR TEXT, "
		        			+ "  CONTENTS TEXT, "
		        			+ "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
		        			+ ")";
            try {
            	_db.execSQL(CREATE_SQL);
            } catch(Exception ex) {
        		Log.e(TAG, "Exception in CREATE_SQL", ex);
        	}

			// insert 5 book records
			insertRecord(_db, "?????????", "?????????", "???????????? ????????????. ?????????????????????, ??????????????????, ????????? ???????????? ???????????? ????????????.");
			insertRecord(_db, "?????????", "?????????", "?????? ????????? ?????????????????????. ???????????? ???????????????. ????????? ??????????????? ???????????????!.");
			insertRecord(_db, "?????????", "?????????", "??????????????? ???????????? ????????? ????????? ???????????? ?????? ?????? ??????????????????. ????????? ?????? ?????????????????????.");
			insertRecord(_db, "???????????????", "?????????", "??????????????? ?????? ????????? ?????????. ?????? ?????? ????????? ???????????????????????????. ???????????????.");
			insertRecord(_db, "?????????", "?????????", "?????? ????????? ????????????. ????????? ?????? ?????????. ??????????????? ?????????!");

		}

        public void onOpen(SQLiteDatabase db) {
        	println("opened database [" + DATABASE_NAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");

        	if (oldVersion < 2) {   // version 1

        	}

        }




		private void insertRecord(SQLiteDatabase _db, String name, String author, String contents) {
			try {
				_db.execSQL( "insert into " + TABLE_BOOK_INFO + "(NAME, AUTHOR, CONTENTS) values ('" + name + "', '" + author + "', '" + contents + "');" );

			} catch(Exception ex) {
				Log.e(TAG, "Exception in executing insert SQL.", ex);
			}
		}





    }

	public void insertRecord(String name, String author, String contents) {
		try {
			db.execSQL( "insert into " + TABLE_BOOK_INFO + "(NAME, AUTHOR, CONTENTS) values ('" + name + "', '" + author + "', '" + contents + "');" );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);
		}
	}

	public void updateRecord(String name, String author, String contents) {
		try {
			db.execSQL( "update " + TABLE_BOOK_INFO + " set NAME = '" + name + "',AUTHOR = '" + author + "',CONTENTS = '" + contents + "' where NAME = '" + name + "';" );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing update SQL.", ex);
		}
	}

	public void deleteRecord(String name) {
		try {
			db.execSQL( "delete from " + TABLE_BOOK_INFO + " where NAME = '" + name + "';" );
		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing update SQL.", ex);
		}
	}
	public BookInfo selectRecord(String name) {
		BookInfo result  = new BookInfo("","",""); //??? ??? ????????? ????????????????????
		try {
			Cursor cursor = db.rawQuery( "select name, author, contents from " + TABLE_BOOK_INFO+" where NAME = '" + name +"'",null);
			cursor.moveToFirst();
			String names = cursor.getString(0) ;
			String authors = cursor.getString(1) ;
			String contentss = cursor.getString(2) ;
			result = new BookInfo(names, authors, contentss);


		} catch(Exception ex) {
			Log.e(TAG, "Exception in executing insert SQL.", ex);


		}
		return result;
	}




	public ArrayList<BookInfo> selectAll(){
    	ArrayList<BookInfo> result = new ArrayList<BookInfo>();

    	try {
    		Cursor cursor = db.rawQuery("select name, author, contents from " + TABLE_BOOK_INFO, null);
    		for (int i =0;i<cursor.getCount();i++){
    			cursor.moveToNext();
    			String name  = cursor.getString(0);
				String author = cursor.getString(1);
				String contents  = cursor.getString(2);

				BookInfo binfo = new BookInfo(name, author, contents);
				result.add(binfo);
			}

		}catch (Exception e){
    		Log.e(TAG,"Exception in execting select SQL.",e);
		}
    	return result;
	}


    private void println(String msg) {
    	Log.d(TAG, msg);
    }
}

































