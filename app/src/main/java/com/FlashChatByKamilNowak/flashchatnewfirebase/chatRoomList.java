package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class chatRoomList extends AppCompatActivity {

    private ListView mChatRoomView;
    private DatabaseReference mDatabaseReference;
    private ChatRoomAdapter mChatRoomAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mChatRoomView = (ListView)findViewById(R.id.rooms_list_view);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    @Override
    public void onStart()
    {
        super.onStart();
        mChatRoomAdapter=new ChatRoomAdapter(this,mDatabaseReference);
        mChatRoomView.setAdapter(mChatRoomAdapter);
    }
    public void onStop() {
        super.onStop();

        mChatRoomAdapter=new ChatRoomAdapter(this,mDatabaseReference);
        mChatRoomAdapter.cleanup();
    }

}
