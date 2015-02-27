package com.appbonus.android.model.api;

import com.appbonus.android.model.Question;

import java.io.Serializable;
import java.util.List;

public class QuestionsWrapper implements Serializable {
    protected List<Question> questions;

    public List<Question> getQuestions() {
        return questions;
    }
}
