package com.acukanov.hivet.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.acukanov.hivet.injection.annotations.ApplicationContext;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String LOG_TAG = LogUtils.makeLogTag(DatabaseOpenHelper.class);
    public static final String DATABASE_NAME = "hive_t_database.db";
    public static final int VERSION = 1;

    @Inject
    public DatabaseOpenHelper(@ApplicationContext Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.beginTransaction();
        try {
            sqLiteDatabase.execSQL(DatabaseTables.UsersTable.CREATE_TABLE);
            LogUtils.debug(LOG_TAG, DatabaseTables.UsersTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(DatabaseTables.MessagesTable.CREATE_TABLE);
            LogUtils.debug(LOG_TAG, DatabaseTables.MessagesTable.CREATE_TABLE);
            sqLiteDatabase.execSQL(DatabaseTables.UsersTable.CREATE_BOT_USER);
            LogUtils.debug(LOG_TAG, DatabaseTables.UsersTable.CREATE_BOT_USER);
            // TODO: test data, remove
            sqLiteDatabase.execSQL(DatabaseTables.MessagesTable.CREATE_FIRST_MESSAGE);
            LogUtils.debug(LOG_TAG, DatabaseTables.MessagesTable.CREATE_FIRST_MESSAGE);
            sqLiteDatabase.execSQL(DatabaseTables.MessagesTable.CREATE_SECOND_MESSAGE);
            LogUtils.debug(LOG_TAG, DatabaseTables.MessagesTable.CREATE_SECOND_MESSAGE);
            sqLiteDatabase.setTransactionSuccessful();
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }
}
