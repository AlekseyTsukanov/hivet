package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.ui.base.IView;

import java.util.ArrayList;

public interface IChatView extends IView {
    void onMessagesLoaded(ArrayList<Messages> messagesList);
    void onMessageAdded(Messages message);
    void showProgress(boolean show);
    void showEmptyMessage();
}
