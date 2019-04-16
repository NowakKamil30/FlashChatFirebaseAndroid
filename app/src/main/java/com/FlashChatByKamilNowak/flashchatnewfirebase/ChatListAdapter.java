package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {
    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName;
    private ArrayList<DataSnapshot> mDataSnapshots;
    private ChildEventListener mChildEventListener= new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            //Intent intent=new Intent(mActivity, MainChatActivity.class);
            //PendingIntent pendingIntent= PendingIntent.getActivity(mActivity,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            //MyNotification myNotification=new MyNotification("new message","test","new message",R.drawable.circle_shape,pendingIntent,mActivity);
            //myNotification.createNotification();
            //myNotification.sendNotification();
            mDataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
            Log.d("Flash Chat","hello?");

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public ChatListAdapter(Activity activity,DatabaseReference ref, String name)
    {
        mActivity=activity;
        mDisplayName=name;
        mDatabaseReference=ref.child("rooms").child("messager");
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDataSnapshots= new ArrayList<>();
    }
    static class ViewHolder
    {
        TextView authorName;
        TextView body;
        LinearLayout.LayoutParams layoutParams;
    }
    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        DataSnapshot snapshot=mDataSnapshots.get(position);
        return snapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row,parent,false);
            final ViewHolder holder= new ViewHolder();
            holder.authorName=(TextView)convertView.findViewById(R.id.author);
            holder.body=(TextView)convertView.findViewById(R.id.message);
            holder.layoutParams =(LinearLayout.LayoutParams)holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final InstantMessage message=getItem(position);
        final ViewHolder holder=(ViewHolder) convertView.getTag();

        boolean isMe=(message.getAuthor().equals(mDisplayName));
        setChatRowAppearance(isMe,holder);
        String author= message.getAuthor();
        holder.authorName.setText(author);
        String msg=message.getMessage();
        holder.body.setText(msg);


        return convertView;
    }
    private void setChatRowAppearance(boolean isItMe, ViewHolder holder)
    {
        if(isItMe)
        {
            holder.layoutParams.gravity= Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);

        }else
        {
            holder.layoutParams.gravity=Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.layoutParams);
        holder.body.setLayoutParams(holder.layoutParams);
    }

    public void cleanup()
    {
        mDatabaseReference.removeEventListener(mChildEventListener);
    }
}
