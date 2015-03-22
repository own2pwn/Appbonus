package com.throrinstudio.android.common.libs.validator.validator;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import com.throrinstudio.android.common.libs.validator.AbstractValidator;
import com.throrinstudio.android.common.libs.validator.R;
import com.throrinstudio.android.common.libs.validator.ValidatorException;

import java.util.regex.Pattern;

/**
 * Validator to check if Phone number is correct.
 * Created by throrin19 on 13/06/13.
 */
public class PhoneValidator extends AbstractValidator {

    private static Pattern PHONE_PATTERN = Patterns.PHONE;
    private static final int DEFAULT_ERROR_MESSAGE_RESOURCE = R.string.validator_phone;

    public PhoneValidator(Context c) {
        super(c, DEFAULT_ERROR_MESSAGE_RESOURCE);
    }

    public PhoneValidator(Context c, int errorMessageRes) {
        this(c, errorMessageRes, null);
    }

    public PhoneValidator(Context c, int errorMessageRes, String customPattern) {
        super(c, errorMessageRes);
        if (!TextUtils.isEmpty(customPattern)) {
            PHONE_PATTERN = Pattern.compile(customPattern);
        }
    }

    @Override
    public boolean isValid(String phone) throws ValidatorException {
        return TextUtils.isEmpty(phone) || PHONE_PATTERN.matcher(phone).matches();
    }
}
