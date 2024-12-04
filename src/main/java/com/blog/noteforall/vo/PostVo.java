package com.blog.noteforall.vo;

import lombok.Data;

@Data
public class PostVo {

    private int no;                    // INDEX NUMBER
    private String author;             // AUTHOR
    private String title;              // TITLE
    private String content;            // CONTENT IN HTML
    private String createdDate;        // FIRST CREATED DATE
    private String createdTime;        // FIRST CREATED TIME
    private String lastChgDate;        // LAST CHANGED DATE
    private String lastChgTime;        // LAST CHANGED TIME
    private char status;               // STATUS VALUE FOR DELETION
    private String url;                // File UUID 매핑용
}
