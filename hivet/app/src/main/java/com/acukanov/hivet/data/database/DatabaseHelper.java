package com.acukanov.hivet.data.database;


import android.database.Cursor;

import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.data.database.model.Users;
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
                subscriber.onNext(DatabaseTables.UsersTable.parseCursor(cursor));
            }
            cursor.close();
        });
    }

    public Observable<Users> findUserById(long id) {
        return Observable.create(subscriber -> {
            String query = "SELECT * FROM users WHERE users._id = " + id;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                subscriber.onNext(DatabaseTables.UsersTable.parseCursor(cursor));
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }

    public Observable<Messages> getMessages() {
        return Observable.create(subscriber -> {
            Messages msg = new Messages();
            String query =  "SELECT * FROM " + DatabaseTables.TABLE_MESSAGES;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.MessagesTable.parseCursor(cursor).toString());
                msg = (DatabaseTables.MessagesTable.parseCursor(cursor));
                subscriber.onNext(msg);
            }
            cursor.close();
            subscriber.onCompleted();
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

    public Observable<Long> createUser(Users users) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                long id = mDb.insert(DatabaseTables.TABLE_USERS, DatabaseTables.UsersTable.createUser(users));
                transaction.markSuccessful();
                subscriber.onNext(id);
            } finally {
                transaction.end();
                LogUtils.debug(LOG_TAG, "Creates user = " + users.getId() + "\n" + users.getUserName());
            }
        });
    }

    public Observable<Long> createMessage(Messages message) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                long id = mDb.insert(DatabaseTables.TABLE_MESSAGES, DatabaseTables.MessagesTable.createMessage(message));
                transaction.markSuccessful();
                subscriber.onNext(id);
                //subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates message = "
                        + "\nMessageId " + message.id + "\nText message " + message.message
                        + "\nDate sent" + message.dateTime + "\nMessage userId " + message.userId);
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<Messages> findMessageById(long messageId) {
        return Observable.create(subscriber -> {
            Messages msg = new Messages();
            String query = "SELECT * FROM messages WHERE messages._id = " + messageId;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                msg = DatabaseTables.MessagesTable.parseCursor(cursor);
                subscriber.onNext(DatabaseTables.MessagesTable.parseCursor(cursor));
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }

    public Observable<Void> updateUserAvatarByUserId(long userId, String userAvatar) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.update(
                        DatabaseTables.TABLE_USERS,
                        DatabaseTables.UsersTable.updateUserAvatar(userAvatar),
                        DatabaseTables.UsersTable.COLUMN_ID + "=?",
                        String.valueOf(userId)
                );
                transaction.markSuccessful();
                subscriber.onCompleted();
            } finally {
                transaction.end();
            }
        });
    }
}
