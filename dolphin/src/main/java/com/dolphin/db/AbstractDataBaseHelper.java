package com.dolphin.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractDataBaseHelper extends SQLiteOpenHelper {
    private String dbPath = "";
    private SQLiteDatabase mDataBase;
    protected final Context mContext;

    public AbstractDataBaseHelper(Context context, String baseName) {
        super(context, baseName, null, 1);// 1? its Database Version
        this.dbPath = String.format("/data/data/%s/databases/", context.getPackageName());
        this.mContext = context;
    }

    public void createDataBase() {
        createDataBase(false);
    }

    public boolean createDataBase(boolean force) {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist || force) {
            this.getReadableDatabase();
            this.close();
            try {
                copyDataBase();
                Log.e("DataBaseHelper", "createDatabase database created");
                return true;
            } catch (IOException ignored) {
            }
        }
        return false;
    }

    //Check that the database exists here: /data/data/your package/databases/Da Name
    private boolean checkDataBase() {
        File dbFile = new File(dbPath + getDatabaseName());
        return dbFile.exists();
    }

    //Copy the database from assets
    private void copyDataBase() throws IOException {
        InputStream mInput = mContext.getAssets().open(getDatabaseName());
        String outFileName = dbPath + getDatabaseName();
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer)) > 0) {
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    //Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        String mPath = dbPath + getDatabaseName();
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        return true;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Cursor select(String[] columnNames, String selection, String[] selectionArgs, String order) {
        return getReadableDatabase().query(getTableName(), columnNames, selection,
                selectionArgs, null, null, order);
    }

    protected abstract String getTableName();
    public abstract String[] getColumnNames();
    public abstract Uri getAuthorityUri();
}