package com.acukanov.hivet.ui.chat;


import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.model.Messages;
import com.acukanov.hivet.data.model.Users;
import com.acukanov.hivet.utils.LogUtils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatAdapter.class);
    private static final int VIEW_TYPE_LEFT_SIDE_MESSAGE = 0;
    private static final int VIEW_TYPE_RIGHT_SIDE_MESSAGE = 1;
    private Activity mActivity;
    private ArrayList<Messages> mMessages;
    private ArrayList<Users> mUsers;
    private int mUserId;

    public ChatAdapter(Activity activity, int userId) {
        mUserId = userId;
        mActivity = activity;
        mMessages = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_LEFT_SIDE_MESSAGE:
                return new LeftMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_left_side, parent, false));
            case VIEW_TYPE_RIGHT_SIDE_MESSAGE:
                return new RightMessageHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_message_right_side, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeftMessageHolder) {
            LeftMessageHolder viewHolder = (LeftMessageHolder) holder;
            //Messages message = mMessages.get(position);


        } else if (holder instanceof RightMessageHolder) {
            RightMessageHolder viewHolder = (RightMessageHolder) holder;

        }
    }

    public ArrayList<Messages> getMessages() {
        return mMessages;
    }

    public void setMessages(ArrayList<Messages> messages) {
        mMessages = messages;
    }

    public ArrayList<Users> getUsers() {
        return mUsers;
    }

    public void setUsers(ArrayList<Users> mUsers) {
        this.mUsers = mUsers;
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMessages.get(position).userId == mUserId) {
            return VIEW_TYPE_RIGHT_SIDE_MESSAGE;
        } else {
            return VIEW_TYPE_LEFT_SIDE_MESSAGE;
        }
    }

    public class LeftMessageHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_user_avatar) CircleImageView userAvatar;
        @InjectView(R.id.text_user_name) TextView userName;
        @InjectView(R.id.text_date_time) TextView dateTime;
        @InjectView(R.id.text_message) TextView message;

        public LeftMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }

    public class RightMessageHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.image_user_avatar) CircleImageView userAvatar;
        @InjectView(R.id.text_user_name) TextView userName;
        @InjectView(R.id.text_date_time) TextView dateTime;
        @InjectView(R.id.text_message) TextView message;

        public RightMessageHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }
    }
}
