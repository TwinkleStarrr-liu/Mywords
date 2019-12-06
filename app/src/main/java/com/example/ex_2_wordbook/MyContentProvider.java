package com.example.ex_2_wordbook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    public static final String AUTHORITY = "com.example.vocabulary.provider";
    private static UriMatcher uriMatcher;
    private MyDH dbhelper;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "WordsBook", BOOK_DIR);
        uriMatcher.addURI(AUTHORITY, "WordsBook/#", BOOK_ITEM);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        dbhelper = new MyDH(getContext(), "Ven.db", null, 2);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                //查询words表中所有数据
                cursor = db.query("WordsBook", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case BOOK_ITEM:
                //查询words表中单条数据
                String words_word = uri.getPathSegments().get(1);
                cursor = db.query("WordsBook", projection, "word=?", new String[]{words_word}, null, null, sortOrder);
                break;
            default:
                break;
        }
        return cursor;
    }


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        int deleteRows=0;
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                deleteRows=db.delete("WordsBook",selection,selectionArgs);
                break;
            case BOOK_ITEM:
                String id =uri.getPathSegments().get(1);
                deleteRows=db.delete("WordsBook","word=?",new String[]{id});
                break;
            default:break;
        }
        return deleteRows;
    }



    @Override
    public Uri insert(Uri uri, ContentValues values) {
        //添加数据
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Uri uriReturn = null;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
            case BOOK_ITEM:
                long i = db.insert("WordsBook", null, values);
                String id = values.getAsString("WordsBook");
                uriReturn = Uri.parse("content://" + AUTHORITY + "/WordsBook/" + id);
            default:
                break;
        }
        return uriReturn;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        int updateRows = 0;
        switch (uriMatcher.match(uri)) {
            case BOOK_DIR:
                updateRows = db.update("WordsBook", values, selection, selectionArgs);
                break;
            case BOOK_ITEM:
                String id = uri.getPathSegments().get(1);
                updateRows = db.update("WordsBook", values, "word=?", new String[]{id});
                break;
        }
        return updateRows;
    }




    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd.com.example.VocabularyBook.provider.WordsBook";
            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd.com.example.VocabularyBbook.provider.WordsBook";
        }
        return null;
    }
}

