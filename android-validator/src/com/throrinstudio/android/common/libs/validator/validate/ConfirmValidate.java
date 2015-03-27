
package com.throrinstudio.android.common.libs.validator.validate;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;

import com.throrinstudio.android.common.libs.validator.AbstractValidate;
import com.throrinstudio.android.common.libs.validator.R;

public class ConfirmValidate extends AbstractValidate {

    private static final int CONFIRM_ERROR_MESSAGE = R.string.validator_confirm;
    private TextView mFirstField;
    private TextView mSecondField;
    private Context mContext;
    private final int mErrorMessage;

    public ConfirmValidate(TextView field1, TextView field2) {
        mFirstField = field1;
        mSecondField = field2;
        mContext = mSecondField.getContext();
        mErrorMessage = CONFIRM_ERROR_MESSAGE;
    }

    public ConfirmValidate(TextView field1, TextView field2, int errorMessageResource ) {
        mFirstField = field1;
        mSecondField = field2;
        mContext = mSecondField.getContext();
        mErrorMessage = errorMessageResource;
    }

    @Override
    public boolean isValid() {
        final String firstFieldTxt = mFirstField.getText().toString();
        final String secondFieldTxt = mSecondField.getText().toString();
        if (isNotEmpty(firstFieldTxt) && firstFieldTxt.equals(secondFieldTxt)) {
            mFirstField.setError(null);
            mSecondField.setError(null);
            return true;
        } else {
            mFirstField.setError(mContext.getString(mErrorMessage));
            mSecondField.setError(mContext.getString(mErrorMessage));
            return false;
        }
    }

    @Override
    public String errorMessage() {
        return mContext.getString(mErrorMessage);
    }

    private boolean isNotEmpty(String text) {
        return !TextUtils.isEmpty(text);
    }

}
