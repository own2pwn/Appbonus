package com.appbonus.android.loaders;

import android.content.Context;

import com.activeandroid.sebbia.query.Select;
import com.appbonus.android.model.Question;
import com.dolphin.loader.AbstractLoader;

import java.util.List;

public class FaqLoader extends AbstractLoader<List<Question>> {

    public FaqLoader(Context context) {
        super(context);
    }

    @Override
    protected List<Question> backgroundLoading() throws Throwable {
        return new Select().from(Question.class).execute();
    }
}
