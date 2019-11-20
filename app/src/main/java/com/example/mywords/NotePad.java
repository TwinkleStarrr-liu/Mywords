package com.example.mywords;

import org.litepal.crud.LitePalSupport;

public class NotePad extends LitePalSupport {
    private int id;                       //id
    private String title;               //标题
    private String content;             //内容
    private String accountName;     //用户名
    private String time;            //时间

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }



    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
