package com.appbonus.android.ui.fragments.profile.settings.faq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.sebbia.query.Select;
import com.appbonus.android.R;
import com.appbonus.android.model.Question;
import com.commonsware.cwac.anddown.AndDown;
import com.dolphin.ui.fragment.SimpleFragment;

public class FaqAnswerFragment extends SimpleFragment {
    protected TextView question;
    protected TextView answer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.faq_answer_layout, null);
        question = (TextView) view.findViewById(R.id.question);
        answer = (TextView) view.findViewById(R.id.answer);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();

        long questionId = bundle.getLong("question", -1L);
        Question object;
        if (questionId != -1L) {
            object = new Select().from(Question.class).where(Question.ID + "=?", questionId).executeSingle();
        } else object = new Select().from(Question.class).where(Question.ABOUT_RULES + "=?", true).executeSingle();

        if (object != null) {
            question.setText(object.getText());
            answer.setText(Html.fromHtml(new AndDown().markdownToHtml(object.getAnswer())));
        }

        setTitle(R.string.faq);
        setDrawerIndicatorEnabled(false);
    }
}
