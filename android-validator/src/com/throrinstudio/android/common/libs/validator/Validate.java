package com.throrinstudio.android.common.libs.validator;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Validate extends AbstractValidate {

    private List<AbstractValidator> mValidators = new ArrayList<AbstractValidator>();
    private TextView mSourceView;
    private String errorMessage;

    public Validate(TextView sourceView) {
        mSourceView = sourceView;
    }

    /**
     * Add a new validator for fields attached
     *
     * @param validator {@link AbstractValidator} : The validator to attach
     */
    public void addValidator(AbstractValidator validator) {
        mValidators.add(validator);
    }

    public boolean isValid() {
        for (AbstractValidator validator : mValidators) {
            try {
                if (!validator.isValid(mSourceView.getText().toString())) {
                    String message = validator.getMessage();
                    mSourceView.setError(message);
                    errorMessage = message;
                    return false;
                }
            } catch (ValidatorException e) {
                e.printStackTrace();
                mSourceView.setError(e.getMessage());
                return false;
            }
        }

        return true;
    }

    public TextView getSource() {
        return mSourceView;
    }

    @Override
    public String errorMessage() {
        return errorMessage;
    }
}
