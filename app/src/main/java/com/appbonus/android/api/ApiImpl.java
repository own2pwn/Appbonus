package com.appbonus.android.api;

import android.content.Context;

import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.api.model.DeviceRequest;
import com.appbonus.android.api.model.LoginRequest;
import com.appbonus.android.api.model.OfferRequest;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.api.model.RegisterRequest;
import com.appbonus.android.api.model.ResetPasswordRequest;
import com.appbonus.android.api.model.SimpleRequest;
import com.appbonus.android.api.model.UserRequest;
import com.appbonus.android.api.model.VkLoginRequest;
import com.appbonus.android.api.model.WithdrawalRequest;
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
import com.dolphin.net.methods.HttpMethod;
import com.dynamixsoftware.ErrorAgent;

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
    public UserWrapper readProfile(SimpleRequest request) throws Throwable {
        return doGet(request, SimpleRequest.class, UserWrapper.class, SUFX_MY);
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
    public BalanceWrapper readBalance(SimpleRequest request) throws Throwable {
        return doGet(request, SimpleRequest.class, BalanceWrapper.class, SUFX_MY, SUFX_BALANCE);
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
        return doPost(request, ChangePasswordRequest.class, UserWrapper.class, SUFX_MY);
    }

    @Override
    public DataWrapper confirmPhone(SimpleRequest request) throws Throwable {
        return doPost(request, SimpleRequest.class, DataWrapper.class, SUFX_MY, SUFX_CONFIRM_PHONE);
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
    public SimpleResult vkExit(SimpleRequest request) throws Throwable {
        return doDelete(request, SimpleRequest.class, SimpleResult.class, SUFX_VK_AUTH);
    }

    @Override
    public OffersWrapper getOffers(PagingRequest request) throws Throwable {
        return doGet(request, PagingRequest.class, OffersWrapper.class, SUFX_OFFERS);
    }

    @Override
    public OfferWrapper showOffer(OfferRequest request) throws Throwable {
        return doGet(request, OfferRequest.class, OfferWrapper.class, SUFX_OFFERS, String.valueOf(request.getId()));
    }

    @Override
    public QuestionsWrapper getFaq() throws Throwable {
        return doGet(null, null, QuestionsWrapper.class, SUFX_FAQ);
    }

    @Override
    public ReferralsDetailsWrapper readReferralsDetails(SimpleRequest request) throws Throwable {
        return doGet(request, SimpleRequest.class, ReferralsDetailsWrapper.class, SUFX_MY, SUFX_REFERRALS_DETAILS);
    }

    protected void preparation(HttpMethod httpMethod) {
        addDefaultHeader(httpMethod);
        addDefaultErrorHandler(httpMethod);
    }

    private void addDefaultErrorHandler(HttpMethod httpMethod) {
        httpMethod.setErrorHandler(errorHandler);
    }

    private void addDefaultHeader(HttpMethod httpMethod) {
        httpMethod.addHeader("User-Agent", "Appbonus Android App");
    }

    @Override
    public String host() {
        return HOST_URI;
    }

    @Override
    public String[] apiParameters() {
        return new String[] {API_SUFX, API_VERSION};
    }

    @Override
    protected void report(String tag, String message) {
        ErrorAgent.reportError(new Throwable(message), tag);
    }
}
