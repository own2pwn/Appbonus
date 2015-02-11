package com.appbonus.android.ui.fragments.profile.settings.faq;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.model.Question;
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
        Bundle bundle = takeArguments();

        Question object = (Question) bundle.getSerializable("question");
        question.setText(object.getText());
        answer.setText(object.getAnswer());

        setTitle(R.string.faq);
        setDrawerIndicatorEnabled(false);
    }
}
