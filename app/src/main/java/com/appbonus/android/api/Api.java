package com.appbonus.android.api;

import com.appbonus.android.api.model.ChangePasswordRequest;
import com.appbonus.android.api.model.ConfirmPhoneRequest;
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

public interface Api {
    String HOST_URI = "https://staging.appbonus.ru/";
    String API_SUFX = "api";
    String API_VERSION = "v1";

    String HEADER_USER_AGENT_VALUE = "Appbonus Android App";

    String SUFX_SIGNIN = "signin";
    String SUFX_SIGNUP = "signup";
    String SUFX_RESET_PASSWORD = "reset_password";
    String SUFX_VK_AUTH = "vk_auth";

    String SUFX_MY = "my";
    String SUFX_BALANCE = "balance";
    String SUFX_CONFIRM_PHONE = "confirm_phone";
    String SUFX_REQUEST_CONFIRMATION = "request_confirmation";
    String SUFX_HISTORY = "history";
    String SUFX_REFERRALS_HISTORY = "referrals_history";
    String SUFX_REFERRALS_DETAILS = "referrals_details";
    String SUFX_REGISTER_DEVICE = "register_device";
    String SUFX_UNREGISTER_DEVICE = "unregister_device";
    String SUFX_WITHDRAWAL = "withdrawal";

    String SUFX_OFFERS = "offers";

    String SUFX_FAQ = "faq";

    /*
     *  POST /api/v1/signup
        AUTH no
        ROLE no-user
        PARAMS user: {email: "email@email.com",password: "password", country: "russia|ukraine|belarus", phone: "xxxxxxxxx", device_id: "ID", promo_code: "code"}
        RESPONSE
          {
            "user": user.as_json, "auth_token": "xxxxxxxxxxxxxxxxxxxx"
          }
     */
    LoginWrapper registration(RegisterRequest request) throws Throwable;

    /*
     *  POST /api/v1/signin
        AUTH no
        ROLE no-user
        PARAMS user: {email: "email@email.com",password: "password"}
        RESPONSE
          {
            "user": user.as_json, "auth_token": "xxxxxxxxxxxxxxxxxxxx"
          }
     */
    LoginWrapper login(LoginRequest loginRequest) throws Throwable;

    /*
     *  POST /api/v1/reset_password
        AUTH no
        ROLE no-user
        PARAMS user: {email: "email@email.com"}
        RESPONSE
          {
            success: true
          }
     */
    SimpleResult resetPassword(ResetPasswordRequest request) throws Throwable;

    /*
     *  GET /api/v1/my
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            user: {
              id: 1
              email: "email@email.com"
              phone: "7777777777",
              country: "russia|ukraine|belarus",
              balance: XXX,
              iphone: true|false,
              ipad: true|false,
              android: true,
              referrer_id: null,
              notify: true|false,
              notify_sound: true|false,
              phone_confirmed: true|false,
              auth_services: [{
                id: 15,
                user_id: 19,
                provider: "vkontakte",
                url: "http://vk.com/kelion"
              }]
            }
          }
     */
    UserWrapper readProfile(SimpleRequest request) throws Throwable;

    /*
     *  GET /api/v1/my/history
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            meta: {
              per_page: 10,
              current_page: 1,
              total_pages: 10,
              total_count: 101
            },
            history: [
              {
                amount: XXX
                created_at: "2014-02-28T23:31:17.503+04:00"
                held: false
                id: XXX
                operation_type: "withdrawal"
                user_id: XX,
                offer_title: 'Clash of Clans',
                offer_icon: 'http://....'
              }, {...}
              ...]
     */
    HistoryWrapper readHistory(PagingRequest request) throws Throwable;

    /*
     *  GET /api/v1/my/referrals_history
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            meta: {
              per_page: 10,
              current_page: 1,
              total_pages: 10,
              total_count: 101
            },
            referrals_history: [
                {
                  amount: XXX
                   created_at: "2014-02-28T23:31:17.503+04:00"
                   held: false
                   id: XXX
                   operation_type: "withdrawal"
                   user_id: XX,
                   offer_title: 'Clash of Clans',
                   offer_icon: 'http://....'
                  initiator: {
                    id: 63049
                    email: email@email.com
                  }
                }
                ...
              ]
     */
    ReferralsHistoryWrapper readReferralsHistory(PagingRequest request) throws Throwable;

    /*
     *  GET /api/v1/my/balance
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            balance: {
              active_balance: 1079
              held_balance: 0
              pending_withdrawal: 0  // Баланс на вывод ждущий подтверждения
              referrals_profit: 0
              tasks_profit: 1100
              withdrawal_sum: 253
            }
          }
     */
    BalanceWrapper readBalance(SimpleRequest request) throws Throwable;

    /*
     *  POST /api/v1/my/register_device
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", device_token: "TOKEN"
        AUTH required
        ROLE any
        RESPONSE
          {
            data: "ok"
          }
     */
    DataWrapper registerDevice(DeviceRequest request) throws Throwable;

    /*
     *  POST /api/v1/my/unregister_device
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", device_token: "TOKEN"
        AUTH required
        ROLE any
        RESPONSE
          {
            data: "ok"
          }
     */
    DataWrapper unregisterDevice(DeviceRequest request) throws Throwable;

    /*
     *  POST /api/v1/my/withdrawal
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", withdrawal_request: {request_type:"phone|qiwi",amount:"XXXX"}
        AUTH required
        ROLE any
        RESPONSE
          {
            data: "ok|fail",
            info: errors if fails
          }
     */
    DataWrapper makeWithdrawal(WithdrawalRequest request) throws Throwable;

    /*
     *
     *  my/confirm_phone
        PATCH /api/v1/my/confirm_phone
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", {user: {
          phone_code: '1234'
        }}
        AUTH required
        ROLE any
        RESPONSE
          {
            data: "ok", //if ok
            errors: errors //if fails
          }
     */
    DataWrapper confirmPhone(ConfirmPhoneRequest request) throws Throwable;

    /*
     *  PATCH /api/v1/my
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", user: {... user_attribures ...}
        AUTH required
        ROLE any
        RESPONSE
          {
            "user": user.as_json
          }
     */
    UserWrapper writeProfile(UserRequest request) throws Throwable;

    UserWrapper changePassword(ChangePasswordRequest request) throws Throwable;

    /*
     *  POST /api/v1/vk_auth
        AUTH no
        ROLE no-user
        PARAMS user: {email: "email@email.com", phone: "xxxxxxxxx", vk_token: "TOKEN"}
        RESPONSE
          {
            "user": user.as_json, "auth_token": "xxxxxxxxxxxxxxxxxxxx"
          }
     */
    LoginWrapper vkRegister(VkLoginRequest request) throws Throwable;

    LoginWrapper vkLogin(VkLoginRequest request) throws Throwable;

    /*
     *  DELETE /api/v1/vk_auth
        PARAMS no
        AUTH required
        ROLE no-user
        RESPONSE
          {
            success: true
          }
     */
    SimpleResult vkExit(SimpleRequest request) throws Throwable;

    /*
     *  GET /api/v1/offers
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx", page: Number
        AUTH required
        ROLE any
        RESPONSE
          {
            meta: {
              per_page: 10,
              current_page: 1,
              total_pages: 10,
              total_count: 101
            },
            offers: [
              ...,
              {
                description: "Самое очаровательное приложение всех времен «Заботливые мишки»! "
                icon: "http://a4.mzstatic.com/us/r30/Purple/v4/db/4c/7d/db4c7d49-6cd7-50ef-fd91-742069551ffd/mzl.zutfkxcr.175x175-75.jpg"
                id: 64
                reward: 7
                title: "Заботливые мишки [iPad]",
                done: true|false
              },
              ...
            ]
          }
     */
    OffersWrapper getOffers(PagingRequest request) throws Throwable;

    /*
     *  GET /api/v1/offers/:id
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            offer: {
              [
                {
                  description: "Самое очаровательное приложение всех времен «Заботливые мишки»! "
                  download_link: "/download/15/64"
                  icon: "http://a4.mzstatic.com/us/r30/Purple/v4/db/4c/7d/db4c7d49-6cd7-50ef-fd91-742069551ffd/mzl.zutfkxcr.175x175-75.jpg"
                  id: 64
                  reward: 7
                  done: true|false,
                  title: "Заботливые мишки [iPad]"
                },
                ...
              ]
            }
          }
     */
    OfferWrapper showOffer(OfferRequest request) throws Throwable;


    /*
     *  GET /api/v1/faq
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            questions: [...{
              id: 1,
              text: 'За что начисляется баланс?',
              answer: "Чтобы баланс начислился...."
            }...
            ]
          }
     */
    QuestionsWrapper getFaq() throws Throwable;

    /*
     *  GET /api/v1/my/referrals_details
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            referrals_details: {
              user_id: 7143,
              referrals_profit: "55.0",
              levels: {
                {
                  count: 23,
                  profit: "55.0"
                },
                {
                  count: 0,
                  profit: "0.0"
                },
                {
                  count: 0,
                  profit: "0.0"
                }
              }
            }
          }
     */
    ReferralsDetailsWrapper readReferralsDetails(SimpleRequest request) throws Throwable;

    /*
        my/request_confirmation
        POST /api/v1/my/request_confirmation
        PARAMS auth_token: "xxxxxxxxxxxxxxxxxxxx"
        AUTH required
        ROLE any
        RESPONSE
          {
            data: "sms sent message", //if ok
            errors: errors //if fails
          }
     */
    DataWrapper requestConfirmation(SimpleRequest request) throws Throwable;


}
