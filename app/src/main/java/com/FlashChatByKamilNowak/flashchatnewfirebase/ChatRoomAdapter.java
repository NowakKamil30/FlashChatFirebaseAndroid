package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChatRoomAdapter extends BaseAdapter {
    private DatabaseReference mDatabase;
    private Activity mActivity;
    private ArrayList<DataSnapshot> mDataSnapshots;
    private ChildEventListener mChildEventListener=new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            mDataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
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
    public ChatRoomAdapter(Activity activity,DatabaseReference ref)
    {
        mActivity=activity;
        mDataSnapshots=new ArrayList<>();
        mDatabase=ref.child("rooms");
        mDatabase.addChildEventListener(mChildEventListener);
    }
    static class ViewHolder
    {
        TextView name;
        LinearLayout.LayoutParams layoutParams;
    }
    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantChatRoom getItem(int position) {
        DataSnapshot snapshot=mDataSnapshots.get(position);
        return snapshot.getValue(InstantChatRoom.class);
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
        convertView = inflater.inflate(R.layout.room_list_row,parent,false);
        final ViewHolder holder= new ViewHolder();
        holder.name=(TextView)convertView.findViewById(R.id.chat_name);
        holder.layoutParams =(LinearLayout.LayoutParams)holder.name.getLayoutParams();
        convertView.setTag(holder);
    }

    final InstantChatRoom room=getItem(position);
    final ViewHolder holder=(ViewHolder)convertView.getTag();

    String name =room.getName();
        holder.name.setText(name);


        return convertView;
    }
    public void cleanup()
    {
        mDatabase.removeEventListener(mChildEventListener);
    }
}
