package com.example.mywords;



import android.app.ActivityManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

public class EditNote extends AppCompatActivity {
    public static final int CHOOSE_PHOTO = 2;

    private EditText title, content;
    private Button btn_save, btn_cancel, btn_picture;
    private ActivityManager activityManager;

    private int noteId;
    private String accountname;
    private String time;

    private NotePad notePad;
    private boolean EDIT = false;                   //用来判断是编辑还是删除

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        btn_save = (Button) findViewById(R.id.save);
        btn_cancel = (Button) findViewById(R.id.cancel);

        title = (EditText) findViewById(R.id.noteTitle1);
        content = (EditText) findViewById(R.id.notecontent1);

        Intent intent = getIntent();                                            //nodeId   main传过来的
        noteId = intent.getIntExtra("id", 0);
        accountname = intent.getStringExtra("mainAccount");

        time = intent.getStringExtra("mainTime");

        if (noteId != 0) {
            EDIT = true;
        } else EDIT = false;

        if (EDIT) {                              //如果是长按编辑打开，则显示原有内容
            NotePad notePad = LitePal.find(NotePad.class, noteId);
            title.setText(notePad.getTitle());
            content.setText(notePad.getContent());


        }
        btn_save.setOnClickListener(new View.OnClickListener() {            //保存按钮
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {          //取消按钮
            @Override
            public void onClick(View v) {
                AlertDialog.Builder adb = new AlertDialog.Builder(EditNote.this);
                adb.setTitle("提示");
                adb.setMessage("确定不保存吗");
                adb.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                adb.setNegativeButton("取消", null);
                adb.show();
            }
        });
    }


    public void saveNote() {                                         //保存函数
        String t = title.getText().toString().trim();
        String c = content.getText().toString().trim();

        String im = "[local]1[/local]";
        c = c.replace(im,"");                           //去掉  [local]1[local]
        System.out.println("截取后的content = "+c);

        if (t.equals("") || c.equals("")) {
            Toast.makeText(EditNote.this, "标题内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            if (EDIT) {                          //update
                notePad = LitePal.find(NotePad.class, noteId);
                NotePad notePad = new NotePad();
                notePad.setTitle(t);
                notePad.setContent(c);
                notePad.update(noteId);
            } else {                         //add
                NotePad notePad = new NotePad();
                notePad.setTitle(t);
                notePad.setContent(c);
                notePad.setTime(time);
                if (accountname != null) {
                    notePad.setAccountName(accountname);
                }
                notePad.save();

            }
        }
        Intent intent2 = new Intent(EditNote.this, MainActivity.class);
        startActivity(intent2);
        EditNote.this.finish();
    }
}
