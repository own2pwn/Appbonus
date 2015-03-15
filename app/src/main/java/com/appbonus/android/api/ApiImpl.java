package com.appbonus.android.api;

import android.content.Context;
import android.text.TextUtils;

import com.appbonus.android.R;
import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.api.model.ConfirmPhoneRequest;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.api.model.OfferRequest;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.api.model.RegisterRequest;
import com.appbonus.android.api.model.ResetPasswordRequest;
import com.appbonus.android.api.model.UserRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.api.model.WithdrawalRequest;
import com.appbonus.android.model.api.BalanceWrapper;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.DoneOffersWrapper;
import com.appbonus.android.model.api.HistoryWrapper;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.model.api.OfferWrapper;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.appbonus.android.model.api.SettingsWrapper;
import com.appbonus.android.model.api.SimpleResult;
import com.appbonus.android.model.api.UserWrapper;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.dolphin.api.CommonApi;
import com.dolphin.json.JsonHandler;
import com.dolphin.net.exception.FormException;
import com.dolphin.net.methods.HttpMethod;
import com.dynamixsoftware.ErrorAgent;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.http.HttpHeaders;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class ApiImpl extends CommonApi implements Api {
    protected HttpMethod.ErrorHandler errorHandler;

    public ApiImpl(Context context) {
        super(context);
        this.errorHandler = new ApiErrorHandler(this);
    }

    @Override
    public LoginWrapper registration(RegisterRequest request) throws Throwable {
        return doPost(request, RegisterRequest.class, LoginWrapper.class, SUFX_SIGNUP);
    }

    @Override
    public LoginWrapper login(LoginRequest loginRequest) throws Throwable {
        return doPost(loginRequest, LoginRequest.class, LoginWrapper.class, SUFX_SIGNIN);
    }

    @Override
    public SimpleResult resetPassword(ResetPasswordRequest request) throws Throwable {
        return doPost(request, ResetPasswordRequest.class, SimpleResult.class, SUFX_RESET_PASSWORD);
    }

    @Override
    public UserWrapper readProfile() throws Throwable {
        return doGet(null, null, UserWrapper.class, SUFX_MY);
    }

    @Override
    public HistoryWrapper readHistory(PagingRequest request) throws Throwable {
        return doGet(request, PagingRequest.class, HistoryWrapper.class, SUFX_MY, SUFX_HISTORY);
    }

    @Override
    public ReferralsHistoryWrapper readReferralsHistory(PagingRequest request) throws Throwable {
        return doGet(request, PagingRequest.class, ReferralsHistoryWrapper.class, SUFX_MY, SUFX_REFERRALS_HISTORY);
    }

    @Override
    public BalanceWrapper readBalance() throws Throwable {
        return doGet(BalanceWrapper.class, SUFX_MY, SUFX_BALANCE);
    }

    @Override
    public DataWrapper registerDevice(DeviceRequest request) throws Throwable {
        return doPost(request, DeviceRequest.class, DataWrapper.class, SUFX_MY, SUFX_REGISTER_DEVICE);
    }

    @Override
    public DataWrapper unregisterDevice(DeviceRequest request) throws Throwable {
        return doPost(request, DeviceRequest.class, DataWrapper.class, SUFX_MY, SUFX_UNREGISTER_DEVICE);
    }

    @Override
    public DataWrapper makeWithdrawal(WithdrawalRequest request) throws Throwable {
        return doPost(request, WithdrawalRequest.class, DataWrapper.class, SUFX_MY, SUFX_WITHDRAWAL);
    }

    @Override
    public UserWrapper writeProfile(UserRequest request) throws Throwable {
        return doPost(request, UserRequest.class, UserWrapper.class, SUFX_MY);
    }

    @Override
    public UserWrapper changePassword(ChangePasswordRequest request) throws Throwable {
        return doPost(request, ChangePasswordRequest.class, UserWrapper.class, SUFX_MY, SUFX_UPDATE_PASSWORD);
    }

    @Override
    public DataWrapper confirmPhone(ConfirmPhoneRequest request) throws Throwable {
        return doPost(request, ConfirmPhoneRequest.class, DataWrapper.class, SUFX_MY, SUFX_CONFIRM_PHONE);
    }

    @Override
    public LoginWrapper vkRegister(VkLoginRequest request) throws Throwable {
        return doPost(request, VkLoginRequest.class, LoginWrapper.class, SUFX_VK_AUTH);
    }

    @Override
    public LoginWrapper vkLogin(VkLoginRequest request) throws Throwable {
        return vkRegister(request);
    }

    @Override
    public SimpleResult vkExit() throws Throwable {
        return doDelete(SimpleResult.class, SUFX_VK_AUTH);
    }

    @Override
    public OffersWrapper getOffers(PagingRequest request) throws Throwable {
        return doGet(request, PagingRequest.class, OffersWrapper.class, SUFX_OFFERS);
    }

    @Override
    public OfferWrapper showOffer(OfferRequest request) throws Throwable {
        return doGet(null, null, OfferWrapper.class, SUFX_OFFERS, String.valueOf(request.getId()));
    }

    @Override
    public QuestionsWrapper getFaq() throws Throwable {
        return doGet(QuestionsWrapper.class, SUFX_FAQ);
    }

    @Override
    public ReferralsDetailsWrapper readReferralsDetails() throws Throwable {
        return doGet(ReferralsDetailsWrapper.class, SUFX_MY, SUFX_REFERRALS_DETAILS);
    }

    @Override
    public DataWrapper requestConfirmation() throws Throwable {
        return doPost(DataWrapper.class, SUFX_MY, SUFX_REQUEST_CONFIRMATION);
    }

    @Override
    public DoneOffersWrapper readDoneIds() throws Throwable {
        return doGet(DoneOffersWrapper.class, SUFX_OFFERS, SUFX_DONE);
    }

    @Override
    public SettingsWrapper getSettings() throws Throwable {
        return doGet(SettingsWrapper.class, SUFX_SETTINGS);
    }

    protected void preparation(HttpMethod httpMethod) {
        addDefaultHeader(httpMethod);
        addDefaultErrorHandler(httpMethod);
    }

    private void addDefaultErrorHandler(HttpMethod httpMethod) {
        httpMethod.setErrorHandler(errorHandler);
    }

    private void addDefaultHeader(HttpMethod httpMethod) {
        httpMethod.addHeader(HttpHeaders.USER_AGENT, HEADER_USER_AGENT_VALUE);
        String token = Storage.load(context, Config.TOKEN);
        if (!TextUtils.isEmpty(token)) {
            httpMethod.addHeader(HEADER_AUTH_TOKEN, token);
        }
    }

    @Override
    protected String host() {
        return HOST_URI;
    }

    @Override
    protected String[] apiParameters() {
        return new String[] {API_SUFX, API_VERSION};
    }

    @Override
    protected void report(String tag, String message) {
        ErrorAgent.reportError(new Throwable(message), tag);
    }

    class ApiErrorHandler implements HttpMethod.ErrorHandler {
        private static final String ERROR_PARAMETER = "error";
        private static final String INFO_PARAMETER = "info";
        private static final String ERRORS_PARAMETER = "errors";
        private static final String SUCCESS_PARAMETER = "success";

        protected CommonApi api;

        public ApiErrorHandler(CommonApi api) {
            this.api = api;
        }

        @Override
        public FormException handle(String error) {
            FormException exception = new FormException();
            try {
                JSONObject object = new JSONObject(error);
                if (object.has(ERROR_PARAMETER)) {
                    exception.message = object.getString(ERROR_PARAMETER);
                } else if (object.has(INFO_PARAMETER)) {
                    exception.message = object.getString(INFO_PARAMETER);
                } else if (object.has(ERRORS_PARAMETER)) {
                    JSONObject errors = object.getJSONObject(ERRORS_PARAMETER);
                    Iterator<String> keys = errors.keys();
                    if (keys.hasNext()) {
                        String next = keys.next();
                        Object o = errors.get(next);
                        if (o instanceof JSONArray) {
                            exception.form = WordUtils.capitalize(next);
                            exception.message = ((JSONArray) o).get(0).toString();
                        } else exception.form = WordUtils.capitalize(errors.optString(next));
                    }
                } else if (object.has(SUCCESS_PARAMETER)) {
                    boolean aBoolean = object.getBoolean(SUCCESS_PARAMETER);
                    if (!aBoolean) {
                        exception.message =  api.getString(R.string.failed);
                    } else exception.message =  api.getString(R.string.success);
                }
            } catch (JSONException ignored) {
            }
            return exception;
        }
    }

    @Override
    protected <T> JsonHandler<T> createJsonHandler(Class<T> tClass) {
        return new JsonHandler<T>(tClass) {
            @Override
            public GsonBuilder modify(GsonBuilder builder) {
                GsonBuilder gsonBuilder = super.modify(builder);
                gsonBuilder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {
                    @Override
                    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(new SimpleDateFormat("yyyy-MM-dd").format(src));
                    }
                });
                gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
                    @Override
                    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        String date = json.getAsString();
                        if (StringUtils.isNoneBlank(date)) {
                            try {
                                return new SimpleDateFormat("yyyy-MM-dd").parse(date);
                            } catch (ParseException e) {
                                return null;
                            }
                        }
                        return null;
                    }
                });
                return gsonBuilder;
            }
        };
    }
}
