package com.example.mywords;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class NoteActivity extends AppCompatActivity {
    private TextView title,time,accounttext,content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        title = (TextView)findViewById(R.id.Title1);
        time = (TextView)findViewById(R.id.Time);
        accounttext = (TextView)findViewById(R.id.accountText);
        content = (TextView)findViewById(R.id.Content1);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);        //定位Id
        String account = intent.getStringExtra("account");
        NotePad notePad = LitePal.find(NotePad.class,id);
//        notePad.setAccountName(account);
        title.setText("标题 :"+notePad.getTitle());
        time.setText("时间 :"+intent.getStringExtra("mainTime"));

//        List<NotePad> notePadList = LitePal.findAll(NotePad.class);
//        NotePad notePadzero = notePadList.get(0);
//        notePad.setAccountName(notePadzero.getAccountName());


    }


    }
