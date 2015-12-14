package com.android.contentprovider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Description: #TODO
 *
 * @author zzp(zhao_zepeng@hotmail.com)
 * @since 2015-12-11
 */
public class StudentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.android.StudentProvider";

    public static final Uri STUDENT_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/student");
    public static final Uri GRADE_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/grade");

    public static final int STUDENT_URI_CODE = 0;
    public static final int GRADE_URI_CODE = 1;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, "student", STUDENT_URI_CODE);
        sUriMatcher.addURI(AUTHORITY, "grade", GRADE_URI_CODE);
    }

    private SQLiteDatabase mDb;

    @Override
    public boolean onCreate() {
        mDb = new DbOpenHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] columns, String selection,
                        String[] selectionArgs, String sortOrder) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        return mDb.query(table, columns, selection, selectionArgs, null, null, sortOrder, null);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        mDb.insert(table, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int count = mDb.delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String table = getTableName(uri);
        if (table == null) {
            throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int row = mDb.update(table, values, selection, selectionArgs);
        if (row > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    private String getTableName(Uri uri) {
        String tableName = null;
        switch (sUriMatcher.match(uri)) {
            case STUDENT_URI_CODE:
                tableName = DbOpenHelper.STUDENT_TABLE_NAME;
                break;
            case GRADE_URI_CODE:
                tableName = DbOpenHelper.GRADE_TABLE_NAME;
                break;
            default:break;
        }

        return tableName;
    }

    public class DbOpenHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "student_provider.db";
        public static final String STUDENT_TABLE_NAME = "student";
        public static final String GRADE_TABLE_NAME = "grade";

        public DbOpenHelper(Context context) {
            super(context, DB_NAME, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            String sql;
            sql = "create table if not exists "+ STUDENT_TABLE_NAME + " (";
            sql += "id integer not null primary key , ";
            sql += "name varchar(40) not null default 'unknown', ";
            sql += "gender varchar(10) not null default 'male',";
            sql += "weight float not null default '60'";
            sql += ")";
            db.execSQL(sql);
            sql = "create table if not exists "+GRADE_TABLE_NAME+ " (";
            sql += "id integer not null primary key autoincrement, ";
            sql += "chinese float not null default '0', ";
            sql += "math float not null default '0', ";
            sql += "english float not null default '0'";
            sql += ")";
            db.execSQL(sql);
            db.setTransactionSuccessful();
            db.endTransaction();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }
}
