package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.data.database.model.Users;
import com.acukanov.hivet.ui.base.IView;

import java.util.ArrayList;

public interface IChatView extends IView {
    void showProgress(boolean show);
    void showEmptyMessage();
    void showChatUsers(ArrayList<Users> users);
    void showChatMessages(ArrayList<Messages> messages);
}
