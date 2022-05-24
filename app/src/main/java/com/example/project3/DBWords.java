package com.example.project3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

public class DBWords {

    private String DB_NAME = InfoToClasses.DB_NAME;
    private String DB_PATH = "";
    private final String DB_ABS_PATH = InfoToClasses.DB_ABS_PATH;
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "dict";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_RU_WORD = "ru_word";
    private static final String COLUMN_EN_WORD = "en_word";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_CNT = "cnt";

    private static final int NUM_COLUMN_ID = 0;
    private static final int NUM_COLUMN_RU_WORD = 1;
    private static final int NUM_COLUMN_EN_WORD = 2;
    private static final int NUM_COLUMN_DESCRIPTION = 3;
    private static final int NUM_COLUMN_DATE = 4;
    private static final int NUM_COLUMN_CNT = 5;

    private final SQLiteDatabase mDataBase;

    public DBWords(Context context) {
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getReadableDatabase();
    }

    public void deleteAll() {
        mDataBase.delete(TABLE_NAME, null, null);
    }

    public void delete(long id) {
        mDataBase.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Words select(long id) {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        String ru_word = mCursor.getString(NUM_COLUMN_RU_WORD);
        String en_word = mCursor.getString(NUM_COLUMN_EN_WORD);
        String description = mCursor.getString(NUM_COLUMN_DESCRIPTION);
        String date = mCursor.getString(NUM_COLUMN_DATE);
        int cnt = mCursor.getInt(NUM_COLUMN_CNT);
        return new Words(id, ru_word, en_word, description, date, cnt);
    }

    public int SizeOfDB() {
        Cursor mCursor = mDataBase.query(TABLE_NAME, null, null, null, null, null, null);
        int len = 0;
        mCursor.moveToFirst();
        if (!mCursor.isAfterLast()) {
            do {
                len += 1;
            } while (mCursor.moveToNext());
        }
        return len;
    }

    private class OpenHelper extends SQLiteOpenHelper {
        private final static String DEBUG_TAG = "DBWords";
        private final File dbFile;
        private final Context mContext;

        OpenHelper(Context context) {
            super(context, DB_NAME, null, DATABASE_VERSION);
            if (android.os.Build.VERSION.SDK_INT >= 17)
                DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            else DB_PATH = "/data/user/0/" + context.getPackageName() + "/databases/";
            this.mContext = context;
            copyDataBase();
            if (DB_ABS_PATH.equals("")) dbFile = context.getDatabasePath(DB_NAME);
            else dbFile = new File(DB_ABS_PATH);
        }

        private boolean checkDataBase() {
            File dbF = new File(DB_PATH + DB_NAME);
            return dbF.exists();
        }

        private void copyDataBase() {
            try {
                InputStream mInput = mContext.getAssets().open(DB_NAME);
            } catch (Exception e) {
                System.out.println("AAAA");
                e.printStackTrace();
                System.out.println("AAAA");
            }
            if (!checkDataBase()) {
                try {
                    copyDBFile();
                } catch (IOException mIOException) {
                    mIOException.printStackTrace();
                    //throw new Error("ErrorCopyingDataBase");
                }
            }
        }

        private void copyDBFile() throws IOException {
            InputStream mInput = mContext.getAssets().open(DB_NAME);
            (new File(DB_PATH)).mkdirs();
            OutputStream mOutput = new FileOutputStream(DB_PATH + DB_NAME);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) mOutput.write(mBuffer, 0, mLength);
            mOutput.flush();
            mOutput.close();
            mInput.close();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }

        public synchronized SQLiteDatabase getReadableDatabase() {
            Log.d(DEBUG_TAG, dbFile.getAbsolutePath());
            SQLiteDatabase db;
            db = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            return db;
        }
    }

}