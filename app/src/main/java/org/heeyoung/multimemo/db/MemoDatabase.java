package org.heeyoung.multimemo.db;

import org.heeyoung.multimemo.BasicInfo;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * �޸� �����ͺ��̽�
 */
public class MemoDatabase {

	public static final String TAG = "MemoDatabase";

	/**
	 * �̱��� �ν��Ͻ�
	 */
	private static MemoDatabase database;

	/**
	 * table name for MEMO
	 */
	public static String TABLE_MEMO = "MEMO";

	/**
	 * table name for PHOTO
	 */
	public static String TABLE_PHOTO = "PHOTO";


    /**
     * version
     */
	public static int DATABASE_VERSION = 2;


    /**
     * Helper class defined
     */
    private DatabaseHelper dbHelper;

    /**
     * SQLiteDatabase �ν��Ͻ�
     */
    private SQLiteDatabase db;

    /**
     * ���ؽ�Ʈ ��ü
     */
    private Context context;

    /**
     * ������
     */
	private MemoDatabase(Context context) {
		this.context = context;
	}

	/**
	 * �ν��Ͻ� ��������
	 */
	public static MemoDatabase getInstance(Context context) {
		if (database == null) {
			database = new MemoDatabase(context);
		}

		return database;
	}

	/**
	 * �����ͺ��̽� ����
	 */
    public boolean open() {
    	println("opening database [" + BasicInfo.DATABASE_NAME + "].");

    	dbHelper = new DatabaseHelper(context);
    	db = dbHelper.getWritableDatabase();

    	return true;
    }

    /**
     * �����ͺ��̽� �ݱ�
     */
    public void close() {
    	println("closing database [" + BasicInfo.DATABASE_NAME + "].");
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
			Log.e("asdf", SQL);
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



	/**
	 * Database Helper inner class
	 */
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
        	println("creating database [" + BasicInfo.DATABASE_NAME + "].");

        	// TABLE_MEMO
        	println("creating table [" + TABLE_MEMO + "].");

        	// drop existing table
        	String DROP_SQL = "drop table if exists " + TABLE_MEMO;
        	try {
        		db.execSQL(DROP_SQL);
        	} catch(Exception ex) {
        		Log.e(TAG, "Exception in DROP_SQL", ex);
        	}

        	// create table
			String CREATE_SQL = "create table " + TABLE_MEMO + "("
					+ " _id INTEGER NOT NULL PRIMARY KEY, "
					+ " INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
					+ " CONTENT_TEXT TEXT DEFAULT 'asdf', "
					+ " ID_PHOTO INTEGER, "
					+ " CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
					+ ");";
            try {

            	db.execSQL(CREATE_SQL);
				Log.e("asdf","dd2");
            } catch(Exception ex) {
				Log.e(TAG, CREATE_SQL);
        		Log.e(TAG, "Exception in CREATE_SQL", ex);
        	}

            // TABLE_PHOTO
        	println("creating table [" + TABLE_PHOTO + "].");

        	// drop existing table
        	DROP_SQL = "drop table if exists " + TABLE_PHOTO;

        	try {
        		db.execSQL(DROP_SQL);
				Log.e("asdf","dddd2");
        	} catch(Exception ex) {
				Log.e("asdf","dddd");
        		Log.e(TAG, "Exception in DROP_SQL", ex);
        	}

        	// create table
        	CREATE_SQL = "create table " + TABLE_PHOTO + "("
		        			+ "  _id INTEGER  NOT NULL PRIMARY KEY , "
		        			+ "  URI TEXT, "
		        			+ "  CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP "
		        			+ ")";
            try {
            	db.execSQL(CREATE_SQL);
				Log.e("asdf","dddd2");
            } catch(Exception ex) {
				Log.e("asdf","dddd");
        		Log.e(TAG, "Exception in CREATE_SQL", ex);
        	}

            // create index
        	String CREATE_INDEX_SQL = "create index " + TABLE_PHOTO + "_IDX ON " + TABLE_PHOTO + "("
		        			+ "URI"
		        			+ ")";
            try {
				Log.e("asdf","d");
            	db.execSQL(CREATE_INDEX_SQL);
            } catch(Exception ex) {
				Log.e("asdf","d");
        		Log.e(TAG, "Exception in CREATE_INDEX_SQL", ex);
        	}


        }

        public void onOpen(SQLiteDatabase db)
        {
        	println("opened database [" + BasicInfo.DATABASE_NAME + "].");

        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion)
        {
        	println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");



        }
    }

    private void println(String msg) {
    	Log.d(TAG, msg);
    }


}