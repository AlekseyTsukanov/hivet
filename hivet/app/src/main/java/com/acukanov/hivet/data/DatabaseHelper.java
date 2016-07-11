package com.acukanov.hivet.data;


import android.database.Cursor;

import com.acukanov.hivet.data.model.Messages;
import com.acukanov.hivet.data.model.Users;
import com.acukanov.hivet.utils.LogUtils;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;

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
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.UsersTable.parseCursor(cursor).toString());
                subscriber.onNext(DatabaseTables.UsersTable.parseCursor(cursor));
            }
            cursor.close();
        });
    }

    public ArrayList<Users> findUsersData() {
        ArrayList<Users> users = new ArrayList<>();
        String query = "SELECT * FROM " + DatabaseTables.TABLE_USERS
                + " INNER JOIN " + DatabaseTables.TABLE_MESSAGES
                + " ON " + DatabaseTables.UsersTable.COLUMN_ID
                +  " = " + DatabaseTables.MessagesTable.COLUMN_USER_ID;
        Cursor cursor = mDb.query(query);
        while (cursor.moveToNext()) {
            users.add(DatabaseTables.UsersTable.parseCursor(cursor));
        }
        return users;
    }

    /*public Observable<ArrayList<Users>> findUsersDataTest() {
        return Observable.create(subscriber -> {
            ArrayList<Users> users = new ArrayList<Users>();
            // String query = "SELECT * FROM users INNER JOIN messages ON users.id = messages.user_id;
            String query = "SELECT * FROM " + DatabaseTables.TABLE_USERS
                    + " INNER JOIN " + DatabaseTables.TABLE_MESSAGES
                    + " ON " + DatabaseTables.UsersTable.COLUMN_ID
                    +  " = " + DatabaseTables.MessagesTable.COLUMN_USER_ID;
            Cursor cursor = mDb.query(query);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.UsersTable.parseCursor(cursor).toString());
                users.add(DatabaseTables.UsersTable.parseCursor(cursor));
                subscriber.onNext(users);
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }*/

    public Observable<ArrayList<Messages>> findUserAndMessageData() {
        return Observable.create(subscriber -> {
            ArrayList<Messages> messages = new ArrayList<Messages>();
            String query = "SELECT m.message, u.user_name"
                    + " FROM messages m"
                    + " INNER JOIN users u ON m.user_id = u." + DatabaseTables.UsersTable.COLUMN_ID;
            /*String query = "SELECT messages.message, messages.date_time, users.user_name"
                    + " FROM messages"
                    + " INNER JOIN users ON messages.user_id = users._id";*/
            /*String query = "SELECT messages.message, messages.date_time, users.user_name, users.user_avatar"
                    + " FROM messages"
                    + " INNER JOIN users ON users._id = messages.user_id";*/
            /*String query = "SELECT " + DatabaseTables.MessagesTable.COLUMN_MESSAGE + ", " + DatabaseTables.MessagesTable.COLUMN_DATE
                    + ", " + DatabaseTables.UsersTable.COLUMN_USER_NAME + ", " + DatabaseTables.UsersTable.COLUMN_USER_AVATAR
                    + " FROM " + DatabaseTables.TABLE_MESSAGES
                    + " INNER JOIN " + DatabaseTables.TABLE_USERS
                    + " ON " + DatabaseTables.UsersTable.COLUMN_ID + " = " + DatabaseTables.MessagesTable.COLUMN_USER_ID;*/
            Cursor cursor = mDb.query(query);
            cursor.moveToFirst();
            //LogUtils.error(LOG_TAG, DatabaseTables.MessagesTable.parseCursor(cursor).toString());
            while (cursor.moveToNext()) {
                //LogUtils.error(LOG_TAG, DatabaseTables.MessagesTable.parseCursor(cursor).toString());
                // TODO: Returns only mesages obejct fields, need somehow to return also and users data!
                messages.add(DatabaseTables.MessagesTable.parseCursor(cursor));
                subscriber.onNext(messages);
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }

    /*public Observable<Users> findUserData() {
        return Observable.create(subscriber -> {
            ArrayList<Users> users = new ArrayList<Users>();
            // String query = "SELECT * FROM users INNER JOIN messages ON users.id = messages.user_id;
            String query = "SELECT * FROM " + DatabaseTables.TABLE_USERS
                    + " INNER JOIN " + DatabaseTables.TABLE_MESSAGES
                    + " ON " + DatabaseTables.UsersTable.COLUMN_ID
                    +  " = " + DatabaseTables.MessagesTable.COLUMN_USER_ID;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.UsersTable.parseCursor(cursor).toString());
                users.add(DatabaseTables.UsersTable.parseCursor(cursor));
                subscriber.onNext(DatabaseTables.UsersTable.parseCursor(cursor));
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }*/

    public Observable<Messages> findAllMessages() {
        return Observable.create(subscriber -> {
            String query = "SELECT * FROM " + DatabaseTables.TABLE_MESSAGES;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.MessagesTable.parseCursor(cursor).toString());
                subscriber.onNext(DatabaseTables.MessagesTable.parseCursor(cursor));
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
