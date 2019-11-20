package com.example.mywords;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends BaseActivity {

    Button btn_add;
    public List<NotePad> notePadList;
    private ListView listView;                  //显示备忘录文件

    private ActivityManager activityManager;
    private static ActivityMananger instance;
    private List<Activity> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activityManager = ActivityManager.getInstance();
        activityManager.AddActivity(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        btn_add=(Button)findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,EditNote.class);
                Intent intent2 = getIntent();
                String Account = intent2.getStringExtra("account");
                String Time = activityManager.getTime();
                if (Account != null)
                    intent.putExtra("mainAccount",Account);
                intent.putExtra("mainTime",Time);

                startActivity(intent);
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {             //进入查看
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {              //短摁 查看
                NotePad notePad = notePadList.get(position);

                Intent intent = new Intent(MainActivity.this,NoteActivity.class);       //传 创建者和  时间   noteID

                String Time = ActivityManager.getTime();

                intent.putExtra("account",notePad.getAccountName());
                intent.putExtra("id",notePad.getId());
                intent.putExtra("mainTime",Time);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {         //长摁删除
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(notePadList.get(position).getTitle());
                final int nodeId = notePadList.get(position).getId();
                adb.setItems(new String[]{"删除", "修改"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:         //删除
                                System.out.println("长按删除时的 noteId "+nodeId);
                                LitePal.delete(NotePad.class,nodeId);
                                notePadList.remove(position);
                                Toast.makeText(MainActivity.this,"数据删除成功",Toast.LENGTH_SHORT).show();

                                init();                                             //刷新页面
                                break;
                            case 1:         //修改
                                Intent intent = new Intent();
                                intent.putExtra("id",nodeId);
                                intent.setClass(MainActivity.this,EditNote.class);
                                startActivity(intent);
                                break;
                            default:
                                break;
                        }
                    }
                });
                adb.show();
                return true;
            }
        });
        System.out.println("这时候启动活动，并刷新界面");
        init();
    }

    public void init(){                                                     //刷新页面
        notePadList = LitePal.findAll(NotePad.class);
        if(notePadList.size() > 0) {
            NotePad notePadzero = notePadList.get(0);
            for (int i = 0; i < notePadList.size(); i++) {
                NotePad notePad = notePadList.get(i);
                notePad.setAccountName(notePadzero.getAccountName());
                int ID = notePad.getId();
                notePad.update(ID);
            }
        }
        NoteAdapter adapter = new NoteAdapter(MainActivity.this,R.layout.item_title,notePadList);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
            adb.setTitle("提醒")
                    .setMessage("确定退出？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityManager.exitAllprogress();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
        return super.onKeyDown(keyCode, event);
    }
    public static ActivityMananger getInstance(){
        if (instance == null){
            instance = new ActivityMananger();
        }
        return instance;
    }
    public void AddActivity(Activity activity){         //添加活动进列表
        if (list == null){
            list = new ArrayList<Activity>();
        }
        if (activity != null){
            list.add(activity);
        }
    }
    public void exitAllprogress(){                  //退出所有程序
        for (int i = 0;i < list.size();i++){
            Activity activity = list.get(i);
            activity.finish();
        }
    }
    public void saveNote(String accountName,String title,String content,String time){
        NotePad note = new NotePad();
        note.setTitle(title);
    }
    public static String getTime(){
        Calendar callForDate = Calendar.getInstance();

        java.text.SimpleDateFormat currentDate = new java.text.SimpleDateFormat("dd-MMMM-yyyy");

        final String saveCurrentDate = currentDate.format(callForDate.getTime());

        return saveCurrentDate;
    }


}
