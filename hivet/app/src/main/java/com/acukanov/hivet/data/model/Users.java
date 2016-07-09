package com.acukanov.hivet.data.model;


public class Users {
    public int id;
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
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    public int hashCode() {
        int result = id;
        result = 31 * result + userName.hashCode();
        result = 31 * result + (userAvatar != null ? userAvatar.hashCode() : 0);
        return result;
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
