package com.appbonus.android.api;

import android.content.Context;

import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.User;
import com.appbonus.android.model.WithdrawalRequest;
import com.appbonus.android.model.api.BalanceWrapper;
import com.appbonus.android.model.api.DataWrapper;
import com.appbonus.android.model.api.HistoryWrapper;
import com.appbonus.android.model.api.LoginWrapper;
import com.appbonus.android.model.api.OfferWrapper;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.model.api.QuestionsWrapper;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.appbonus.android.model.api.SimpleResult;
import com.appbonus.android.model.api.UserWrapper;
import com.dolphin.json.JsonHandler;
import com.dolphin.net.methods.HttpMethod;
import com.dolphin.net.methods.MethodDelete;
import com.dolphin.net.methods.MethodGet;
import com.dolphin.net.methods.MethodPost;

import org.json.JSONObject;

import java.util.HashMap;

public class ApiImpl implements Api {
    private static final String HOST_URI = "http://appbonus-staging.herokuapp.com/";
    private static final String API_SUFX = "api";
    private static final String API_VERSION = "v1";

    protected Context context;
    protected HttpMethod.ErrorHandler errorHandler;

    public ApiImpl(Context context) {
        this.context = context;
        this.errorHandler = new ApiErrorHandler(this);
    }

    public ApiLogger logger = new ApiLogger();

    @Override
    public String getString(int resourceId) {
        return context.getString(resourceId);
    }

    @Override
    public LoginWrapper registration(String email, String password, String country, String phone, String deviceId) throws Throwable {
        JSONObject info = new JSONObject();
        info.put("email", email);
        info.put("password", password);
        info.put("country", country);
        info.put("phone", phone);
        info.put("device_id", deviceId);

        JSONObject object = new JSONObject();
        object.put("user", info);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "signup");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<LoginWrapper> jsonHandler = new JsonHandler<>(LoginWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public LoginWrapper login(LoginRequest loginRequest) throws Throwable {
        return doPost(loginRequest, LoginRequest.class, LoginWrapper.class, SUFX_SIGNIN);
    }

    @Override
    public SimpleResult resetPassword(Context context, String mail) throws Throwable {
        JSONObject info = new JSONObject();
        info.put("email", mail);

        JSONObject object = new JSONObject();
        object.put("user", info);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "reset_password");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<SimpleResult> jsonHandler = new JsonHandler<>(SimpleResult.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public UserWrapper readProfile(Context context, String authToken) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "my");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<UserWrapper> jsonHandler = new JsonHandler<>(UserWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public HistoryWrapper readHistory(Context context, String authToken, Long page) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        params.put("page", String.valueOf(page));
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "my", "history");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<HistoryWrapper> jsonHandler = new JsonHandler<>(HistoryWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public ReferralsHistoryWrapper readReferralsHistory(Context context, String authToken, Long page) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        params.put("page", String.valueOf(page));
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "my", "referrals_history");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<ReferralsHistoryWrapper> jsonHandler = new JsonHandler<>(ReferralsHistoryWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public BalanceWrapper readBalance(Context context, String authToken) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "my", "balance");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<BalanceWrapper> jsonHandler = new JsonHandler<>(BalanceWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public DataWrapper registerDevice(Context context, String authToken, String deviceToken) throws Throwable {
        JSONObject object = new JSONObject();
        object.put("auth_token", authToken);
        object.put("device_token", deviceToken);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "my", "register_device");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<DataWrapper> jsonHandler = new JsonHandler<>(DataWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public DataWrapper unregisterDevice(Context context, String authToken, String deviceToken) throws Throwable {
        JSONObject object = new JSONObject();
        object.put("auth_token", authToken);
        object.put("device_token", deviceToken);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "my", "unregister_device");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<DataWrapper> jsonHandler = new JsonHandler<>(DataWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public DataWrapper makeWithdrawal(Context context, String authToken, WithdrawalRequest request) throws Throwable {
        JSONObject withdrawal = new JSONObject();
        withdrawal.put("request_type", request.getRequestType());
        withdrawal.put("amount", request.getAmount());

        JSONObject object = new JSONObject();
        object.put("auth_token", authToken);
        object.put("withdrawal_request", withdrawal);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "my", "withdrawal");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<DataWrapper> jsonHandler = new JsonHandler<>(DataWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public UserWrapper writeProfile(Context context, String authToken, User user) throws Throwable {
        JSONObject userObj = new JSONObject();
        userObj.put("email", user.getEmail());
        userObj.put("phone", user.getPhone());
        userObj.put("country", user.getCountry());

        JSONObject object = new JSONObject();
        object.put("user", userObj);
        object.put("auth_token", authToken);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "my");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<UserWrapper> jsonHandler = new JsonHandler<>(UserWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public DataWrapper confirmPhone(Context context, String authToken) throws Throwable {
        JSONObject object = new JSONObject();
        object.put("auth_token", authToken);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "my", "confirm_phone");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<DataWrapper> jsonHandler = new JsonHandler<>(DataWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public LoginWrapper vkRegister(Context context, String mail, String vkToken) throws Throwable {
        JSONObject info = new JSONObject();
        info.put("email", mail);
        info.put("vk_token", vkToken);

        JSONObject object = new JSONObject();
        object.put("user", info);

        HttpMethod method = new MethodPost(HOST_URI, object, API_SUFX, API_VERSION, "vk_auth");
        preparation(method);

        logger.start();
        String answer = method.perform(context);
        logger.end("vkRegister");

        JsonHandler<LoginWrapper> jsonHandler = new JsonHandler<>(LoginWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public LoginWrapper vkLogin(Context context, String vkToken) throws Throwable {
        return vkRegister(context, null, vkToken);
    }

    @Override
    public SimpleResult vkExit(Context context) throws Throwable {
        HttpMethod method = new MethodDelete(HOST_URI, null, API_SUFX, API_VERSION, "vk_auth");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<SimpleResult> jsonHandler = new JsonHandler<>(SimpleResult.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public OffersWrapper getOffers(Context context, String authToken, Long page) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        params.put("page", String.valueOf(page));
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "offers");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<OffersWrapper> jsonHandler = new JsonHandler<>(OffersWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public OfferWrapper showOffer(Context context, String authToken, Offer offer) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "offers", String.valueOf(offer.getId()));
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<OfferWrapper> jsonHandler = new JsonHandler<>(OfferWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public QuestionsWrapper getFaq(Context context, String authToken) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "faq");
        preparation(method);

        String answer = method.perform(context);

        JsonHandler<QuestionsWrapper> jsonHandler = new JsonHandler<>(QuestionsWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    @Override
    public ReferralsDetailsWrapper readReferralsDetails(Context context, String authToken) throws Throwable {
        HashMap<String, String> params = new HashMap<>();
        params.put("auth_token", authToken);
        HttpMethod method = new MethodGet(HOST_URI, params, API_SUFX, API_VERSION, "my", "referrals_details");
        preparation(method);

        logger.start();
        String answer = method.perform(context);
        logger.end("readReferralsDetails");

        JsonHandler<ReferralsDetailsWrapper> jsonHandler = new JsonHandler<>(ReferralsDetailsWrapper.class);
        return jsonHandler.fromJsonString(answer);
    }

    private HttpMethod preparation(HttpMethod httpMethod) {
        addDefaultHeader(httpMethod);
        addDefaultErrorHandler(httpMethod);
        return httpMethod;
    }

    private void addDefaultErrorHandler(HttpMethod httpMethod) {
        httpMethod.setErrorHandler(errorHandler);
    }

    private void addDefaultHeader(HttpMethod httpMethod) {
        httpMethod.addHeader("User-Agent", "Appbonus Android App");
    }

    private <T, K> T doPost(K request, Class<K> requestType, Class<T> responseType, String... path) throws Throwable {
        String[] array = null;
        if (path != null) {
            array = new String[path.length + 2];
            array[0] = API_SUFX;
            array[1] = API_VERSION;
            System.arraycopy(path, 0, array, 2, path.length);
        }
        HttpMethod method = new MethodPost(HOST_URI, toJson(request, requestType), array);
        String answer = method.perform(context);
        return toObject(answer, responseType);
    }

    private <T> JSONObject toJson(T obj, Class<T> tClass) throws Throwable {
        JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.toJsonObject(obj);
    }

    private <T> T toObject(String string, Class<T> tClass) {
        JsonHandler<T> jsonHandler = new JsonHandler<>(tClass);
        return jsonHandler.fromJsonString(string);
    }
}
