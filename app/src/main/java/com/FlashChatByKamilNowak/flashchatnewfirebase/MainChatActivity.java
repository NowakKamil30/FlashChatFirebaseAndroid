package com.FlashChatByKamilNowak.flashchatnewfirebase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mChatListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


            setupDisplayName();
            Log.d("Flash Chat", mDisplayName);
            mDatabaseReference = FirebaseDatabase.getInstance().getReference();


        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.rooms_list_view);

    mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    sendMessage();
                    return true;

                }
            });


        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

private void setupDisplayName()
{
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    mDisplayName = user.getDisplayName();
}

    private void sendMessage() {
        Log.d("FlashChat", "send message");

        String input=mInputText.getText().toString();
        if(!input.isEmpty())
        {
            InstantMessage chat=new InstantMessage(input,mDisplayName);
            mDatabaseReference.child("rooms").child("messager").push().setValue(chat);
            mInputText.setText("");
        }

    }


    @Override
    public void onStart()
    {
        super.onStart();
        mChatListAdapter=new ChatListAdapter(this,mDatabaseReference,mDisplayName);
        mChatListView.setAdapter(mChatListAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

       mChatListAdapter=new ChatListAdapter(this,mDatabaseReference,mDisplayName);
       mChatListAdapter.cleanup();
    }

}
