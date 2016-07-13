package com.acukanov.hivet.data.database.model;


public class Users {
    public long id;
    public String userName;
    public String userAvatar;

    public Users() {

    }

    public Users(int id, String userName, String userAvatar) {
        this.id = id;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }

    // region getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }
    // endregion


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Users users = (Users) o;

        if (id != users.id) return false;
        if (!userName.equals(users.userName)) return false;
        return userAvatar != null ? userAvatar.equals(users.userAvatar) : users.userAvatar == null;

    }

    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }
}
