package com.acukanov.hivet.data;


import android.database.Cursor;

import com.acukanov.hivet.data.model.Messages;
import com.acukanov.hivet.data.model.Users;
import com.acukanov.hivet.utils.LogUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class DatabaseHelper {
    private static final String LOG_TAG = LogUtils.makeLogTag(DatabaseHelper.class);
    private final BriteDatabase mDb;

    @Inject
    public DatabaseHelper(DatabaseOpenHelper databaseOpenHelper) {
        mDb = SqlBrite.create().wrapDatabaseHelper(databaseOpenHelper);
    }

    public BriteDatabase getBriteDatabase() {
        return mDb;
    }

    public Observable<Void> clearTables() {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                Cursor cursor = mDb.query("SELECT name FROM sqlite_manager WHERE type='table'");
                while (cursor.moveToNext()) {
                    mDb.delete(cursor.getString(cursor.getColumnIndex("name")), null);
                    LogUtils.debug(LOG_TAG, "clear table");
                }
                cursor.close();
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<Users> findAllUsers() {
        return Observable.create(subscriber -> {
            String query = "SELECT * FROM " + DatabaseTables.TABLE_USERS;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.UsersTable.parseCursor(cursor).toString());
            }
        });
    }

    public Observable<Users> findMessagesByUserId(int id) {
        return Observable.create(subscriber -> {
            String query = "SELECT messages.message FROM " + DatabaseTables.TABLE_USERS
                    + " INNER JOIN " + DatabaseTables.TABLE_MESSAGES
                    + " ON " + DatabaseTables.TABLE_USERS + "." + DatabaseTables.UsersTable.COLUMN_ID
                    + " = "
                    + DatabaseTables.TABLE_MESSAGES + "." + DatabaseTables.MessagesTable.COLUMN_USER_ID
                    + " WHERE " + DatabaseTables.TABLE_USERS + "." + DatabaseTables.UsersTable.COLUMN_ID + "=" + id;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                subscriber.onNext(DatabaseTables.UsersTable.parseCursor(cursor));
            }
            cursor.close();
            subscriber.onCompleted();
            LogUtils.debug(LOG_TAG, "Finding message by user id = " + id);
        });
    }

    public Observable<Void> createUser(Users users) {
        return Observable.create(subscriber -> {
            // TODO: Test data, delete
            /*final AtomicInteger queries = new AtomicInteger();
            Observable<SqlBrite.Query> u = mDb.createQuery(DatabaseTables.TABLE_USERS, "SELECT * FROM " + DatabaseTables.TABLE_USERS);
            Subscription s = u.subscribe(query -> {
                queries.getAndIncrement();
            });
            LogUtils.error(LOG_TAG, "Queries = " + queries.get());*/
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.insert(DatabaseTables.TABLE_USERS, DatabaseTables.UsersTable.createUser(users));
                //s.unsubscribe();
                transaction.markSuccessful();
                subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates user = " + users.id + "\n" + users.userName);
                //LogUtils.debug(LOG_TAG, "Creates user = " + users.id + "\n" + users.userName + "\nQueries = " + queries.get());
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<Void> createMessage(Users users, Messages messages) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.insert(DatabaseTables.TABLE_MESSAGES, DatabaseTables.MessagesTable.createMessage(users, messages));
                transaction.markSuccessful();
                subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates message = " + users.id + "\n" + users.userName
                        + "\n" + messages.id + "\n" + messages.message + "\n" + messages.dateTime);
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<Void> updateUserAvatar(Users users) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.update(
                        DatabaseTables.TABLE_USERS,
                        DatabaseTables.UsersTable.updateUserAvatarByUserId(users),
                        DatabaseTables.UsersTable.COLUMN_ID + "=?"
                );
                transaction.markSuccessful();
                subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates message = " + users.id + "\n" + users.userName
                        + "\n" + users.userAvatar);
            } finally {
                transaction.end();
            }
        });
    }
}
