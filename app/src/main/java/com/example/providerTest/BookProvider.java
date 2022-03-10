package com.example.providerTest;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


public class BookProvider extends ContentProvider {
    public BookProvider() {
    }
    private static final String TAG = "qiruimin_BookProvider";
    private static final String AUTHORITY = "com.ipctest.book";
    public static final String EXTRA_RESULT_CODE = "result_code";
    private static final String METHOD_TEST1 = "method_test1";
    public static final Uri BOOK_CONTENT_URI =  Uri.parse("content://"+AUTHORITY+"/book");
    public static final Uri USER_CONTENT_URI =  Uri.parse("content://"+AUTHORITY+"/user");
    public static final int BOOK_URI_CODE = 0;
    public static final int USER_URI_CODE = 1;
    private static final UriMatcher sUrimatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sUrimatcher.addURI(AUTHORITY,"book",BOOK_URI_CODE);
        sUrimatcher.addURI(AUTHORITY,"user",USER_URI_CODE);
    }
    private Context mContext;
    private SQLiteDatabase mDb;
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.e(TAG, "delete: " );
        // Implement this to handle requests to delete one or more rows.
        String table = getTableName(uri);
        if(table ==null){
            Log.e(TAG, "Unsupported Uri ");
        }
        int count = mDb.delete(table,selection,selectionArgs);
        if(count>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return count;
    }

    @Override
    public String getType(Uri uri) {
        Log.e(TAG, "getType: " );
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.e(TAG, "insert: " );
        // TODO: Implement this to handle requests to insert a new row.
        String table = getTableName(uri);
        if(table ==null){
            Log.e(TAG, "Unsupported Uri ");
        }
        mDb.insert(table,null,values);
        mContext.getContentResolver().notifyChange(uri,null);
        return uri;
    }

    @Override
    public boolean onCreate() {
        Log.i(TAG, "onCreate,current thread:"+Thread.currentThread().getName());
        mContext = getContext();
        initProviderData();
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        Log.i(TAG, "query, current thread:"+Thread.currentThread().getName());
        String table = getTableName(uri);
        if(table ==null){
            Log.e(TAG, "Unsupported Uri ");
        }
        return mDb.query(table,projection,selection,selectionArgs,null,null,sortOrder,null);

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        Log.e(TAG, "update: " );
        // TODO: Implement this to handle requests to update one or more rows.
        String table = getTableName(uri);
        if(table ==null){
            Log.e(TAG, "Unsupported Uri ");
        }
        int row = mDb.update(table,values,selection,selectionArgs);
        if(row>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return row;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Log.e(TAG, "call: ");
        Bundle reply = new Bundle();
        if (extras == null) {
            reply.putInt(EXTRA_RESULT_CODE, 0);
            return reply;
        }
        switch (method) {
            case METHOD_TEST1:{
//                handlemethod1(extras);
                Log.e(TAG, "call: METHOD_TEST1" );
                reply.putInt(EXTRA_RESULT_CODE, 1);
                break;
            }
            default: {
                Log.e(TAG, "call: default" );
                reply.putInt(EXTRA_RESULT_CODE, 0);
            }
        }
        return reply;
    }
    private void initProviderData(){
        mDb = new DbOpenHelper(mContext).getWritableDatabase();
        mDb.execSQL("delete from "+DbOpenHelper.BOOK_TABLE_NAME);
        mDb.execSQL("delete from "+DbOpenHelper.USER_TABLE_NAME);
        mDb.execSQL("insert into book values(3,'android ');");
        mDb.execSQL("insert into book values(4,'Ios ');");
        mDb.execSQL("insert into book values(5,'C++ ');");
        mDb.execSQL("insert into user values(1,'android ',1);");
        mDb.execSQL("insert into user values(2,'jasmine ',0);");
    }
    private String getTableName(Uri uri){
        String tableName = null;
        switch (sUrimatcher.match(uri)){
            case BOOK_URI_CODE:
                tableName = DbOpenHelper.BOOK_TABLE_NAME;
                break;
            case USER_URI_CODE:
                tableName = DbOpenHelper.USER_TABLE_NAME;
                break;
            default:break;
        }
        return tableName;
    }
}