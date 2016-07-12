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
            ArrayList<Users> user_id = new ArrayList<Users>();
            // String query = "SELECT * FROM user_id INNER JOIN messages ON user_id.id = messages.user_id;
            String query = "SELECT * FROM " + DatabaseTables.TABLE_USERS
                    + " INNER JOIN " + DatabaseTables.TABLE_MESSAGES
                    + " ON " + DatabaseTables.UsersTable.COLUMN_ID
                    +  " = " + DatabaseTables.MessagesTable.COLUMN_USER_ID;
            Cursor cursor = mDb.query(query);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.UsersTable.parseCursor(cursor).toString());
                user_id.add(DatabaseTables.UsersTable.parseCursor(cursor));
                subscriber.onNext(user_id);
            }
            cursor.close();
            subscriber.onCompleted();
        });
    }*/

    public Observable<ArrayList<Messages>> findAllMessages() {
        return Observable.create(subscriber -> {
            ArrayList<Messages> messages = new ArrayList<Messages>();
            /*String query = "SELECT m.message, u.user_name"
                    + " FROM messages m"
                    + " LEFT OUTER JOIN users u ON m.user_id = u." + DatabaseTables.UsersTable.COLUMN_ID;*/
            String query = "SELECT * FROM " + DatabaseTables.TABLE_MESSAGES;
            Cursor cursor = mDb.query(query);
            cursor.moveToFirst();
            while (cursor.moveToNext()) {
                messages.add(DatabaseTables.MessagesTable.parseCursor(cursor));
                subscriber.onNext(messages);
            }
            cursor.close();
            subscriber.onCompleted();
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

    public Observable<ArrayList<Messages>> findAllMessagesTest() {
        return Observable.create(subscriber -> {
            ArrayList<Messages> messages = new ArrayList<Messages>();
            String query =  "SELECT * FROM " + DatabaseTables.TABLE_MESSAGES;
            Cursor cursor = mDb.query(query);
            while (cursor.moveToNext()) {
                LogUtils.error(LOG_TAG, DatabaseTables.MessagesTable.parseCursor(cursor).toString());
                messages.add(DatabaseTables.MessagesTable.parseCursor(cursor));
                subscriber.onNext(messages);
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
            // TODO: Test data, delete
            /*final AtomicInteger queries = new AtomicInteger();
            Observable<SqlBrite.Query> u = mDb.createQuery(DatabaseTables.TABLE_USERS, "SELECT * FROM " + DatabaseTables.TABLE_USERS);
            Subscription s = u.subscribe(query -> {
                queries.getAndIncrement();
            });
            LogUtils.error(LOG_TAG, "Queries = " + queries.get());*/
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

    public Observable<Void> createMessage(Users users, Messages messages) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.insert(DatabaseTables.TABLE_MESSAGES, DatabaseTables.MessagesTable.createMessage(users, messages));
                transaction.markSuccessful();
                subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates message = UserId " + users.id + "\nUserName " + users.userName
                        + "\nMessageId " + messages.id + "\nText message " + messages.message
                        + "\nDate sent" + messages.dateTime + "\nMessage userId " + messages.userId);
            } finally {
                transaction.end();
            }
        });
    }

    public Observable<Messages> createMessage(Messages messages) {
        return Observable.create(subscriber -> {
            BriteDatabase.Transaction transaction = mDb.newTransaction();
            try {
                mDb.insert(DatabaseTables.TABLE_MESSAGES, DatabaseTables.MessagesTable.createMessage(messages));
                transaction.markSuccessful();
                subscriber.onCompleted();
                LogUtils.debug(LOG_TAG, "Creates message = "
                        + "\nMessageId " + messages.id + "\nText message " + messages.message
                        + "\nDate sent" + messages.dateTime + "\nMessage userId " + messages.userId);
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
