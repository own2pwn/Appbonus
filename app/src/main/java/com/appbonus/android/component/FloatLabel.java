package com.appbonus.android.component;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class FloatLabel extends com.iangclifton.android.floatlabel.FloatLabel {
    protected boolean locked = false;

    public FloatLabel(Context context) {
        super(context);
    }

    public FloatLabel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FloatLabel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setText(String s) {
        getEditText().setText(s);
    }

    public String getText() {
        return getEditText().getText().toString();
    }

    public void setInputType(int type) {
        getEditText().setInputType(type);
    }

    public void lock() {
        EditText editText = getEditText();
        setEditable(editText, false);
    }

    public void unlock() {
        EditText editText = getEditText();
        setEditable(editText, true);
    }

    private void setEditable(EditText editText, boolean b) {
        editText.setClickable(b);
        editText.setFocusable(b);
        editText.setFocusableInTouchMode(b);
        editText.setCursorVisible(b);
        locked = !b;
    }

    public boolean isLocked() {
        return locked;
    }

    public void clear() {
        EditText editText = getEditText();
        editText.setText("");
    }
}
