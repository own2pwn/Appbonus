package com.appbonus.android.model;

import android.provider.BaseColumns;

import com.activeandroid.sebbia.Model;
import com.activeandroid.sebbia.annotation.Column;
import com.activeandroid.sebbia.annotation.Table;

import java.io.Serializable;

@Table(name = Question.TABLE_NAME, id = BaseColumns._ID)
public class Question extends Model implements Serializable {
    public static final String TABLE_NAME = "question";

    public static final String ID = "id";
    @Column(name = ID)
    public Long id;

    public static final String TEXT = "_text";
    @Column(name = TEXT)
    public String text;

    public static final String ANSWER = "answer";
    @Column(name = ANSWER)
    public String answer;

    public static final String ABOUT_REFERRALS = "about_referrals";
    @Column(name = ABOUT_REFERRALS)
    public Boolean aboutReferrals;

    public String getText() {
        return text;
    }

    public String getAnswer() {
        return answer;
    }

    public Boolean getAboutReferrals() {
        return aboutReferrals;
    }
}
