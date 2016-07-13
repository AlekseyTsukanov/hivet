package com.acukanov.hivet.data.database;


import android.content.ContentValues;
import android.database.Cursor;

import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.data.database.model.Users;
import com.acukanov.hivet.utils.DateUtils;

public class DatabaseTables {
    public static final String TABLE_USERS = "users";
    public static final String TABLE_MESSAGES = "messages";

    public DatabaseTables() {};

    public static final class UsersTable {
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_USER_NAME = "user_name";
        public static final String COLUMN_USER_AVATAR = "user_avatar";

        // create table user_id (id int auto_increment primary key, user_name message not null, user_avatar message);
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_USER_NAME + " TEXT NOT NULL,"
                +  COLUMN_USER_AVATAR + " TEXT"
                + ");";

        // insert into user_id values (1, "bot", "");
        public static final String CREATE_BOT_USER =
                "INSERT INTO " + TABLE_USERS + " VALUES ("
                + "1, \"Bot\", \"\");";

        public static ContentValues createUser(Users users) {
            ContentValues values = new ContentValues(2);
            values.put(COLUMN_USER_NAME, users.userName);
            values.put(COLUMN_USER_AVATAR, users.userAvatar);
            return values;
        }

        public static ContentValues updateUserAvatarByUserId(Users users) {
            ContentValues values = new ContentValues(1);
            values.put(COLUMN_USER_AVATAR, users.userAvatar);
            return values;
        }


        public static ContentValues toContentValues(Users users) {
            ContentValues values = new ContentValues(3);
            values.put(COLUMN_ID, users.id);
            values.put(COLUMN_USER_NAME, users.userName);
            values.put(COLUMN_USER_AVATAR, users.userAvatar);
            return values;
        }

        public static Users parseCursor(Cursor cursor) {
            Users users = new Users();
            users.id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            users.userName = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            users.userAvatar = cursor.getString(cursor.getColumnIndex(COLUMN_USER_AVATAR));
            return users;
        }
    }

    public static final class MessagesTable {
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_MESSAGE = "message";
        public static final String COLUMN_DATE = "date_time";
        public static final String COLUMN_USER_ID = "user_id";

        // create table messages (id int auto_increment primary key, message message, dateTime int not null, user_id int not null,
        // foreign key (user_id) references user_id(id));
        public static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_MESSAGES + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
                + COLUMN_MESSAGE + " TEXT,"
                //+ COLUMN_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_USER_ID + " INT NOT NULL,"
                + " FOREIGN KEY (" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(_id)"
                + ");";


        public static ContentValues createMessage(Users users, Messages messages) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE, messages.id);
            values.put(COLUMN_DATE, DateUtils.getDateTime());
            values.put(COLUMN_USER_ID, users.id);
            return values;
        }

        public static ContentValues createMessage(Messages messages) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_MESSAGE, messages.message);
            values.put(COLUMN_DATE, DateUtils.getDateTime());
            values.put(COLUMN_USER_ID, messages.userId);
            return values;
        }

        public static ContentValues toContentValues(Messages messages) {
            ContentValues values = new ContentValues(3);
            //values.put(COLUMN_ID, messages.id);
            values.put(COLUMN_MESSAGE, messages.message);
            values.put(COLUMN_DATE, messages.dateTime);
            values.put(COLUMN_USER_ID, messages.userId);
            return values;
        }

        public static Messages parseCursor(Cursor cursor, Cursor usersCursor) {
            Messages messages = new Messages();
            messages.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            messages.message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
            messages.dateTime = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            messages.userId = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID));
            return messages;
        }

        public static Messages parseCursor(Cursor cursor) {
            Messages messages = new Messages();
            messages.id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID));
            messages.message = cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE));
            messages.dateTime = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            messages.userId = cursor.getLong(cursor.getColumnIndex(COLUMN_USER_ID));
            return messages;
        }
    }

}
