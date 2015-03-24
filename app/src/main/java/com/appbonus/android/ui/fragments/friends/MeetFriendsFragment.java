package com.appbonus.android.ui.fragments.friends;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.model.Settings;
import com.appbonus.android.model.User;
import com.appbonus.android.storage.Config;
import com.appbonus.android.storage.Storage;
import com.dolphin.ui.fragment.SimpleFragment;

public class MeetFriendsFragment extends SimpleFragment implements View.OnClickListener {
    protected View meet;
    protected TextView promo;
    protected TextView meetFriendsPromo;
    protected EditText promoLink;

    protected User user;

    protected MeetFriendsFragmentListener listener;

    public interface MeetFriendsFragmentListener {
        void sendInviteMessage();

        User getUser();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (MeetFriendsFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = listener.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meet_friends_layout, null);
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle(R.string.meet_friends);
        setDrawerIndicatorEnabled(false);
    }

    private void initUI(View view) {
        promoLink = (EditText) view.findViewById(R.id.promo_link);
        promo = (TextView) view.findViewById(R.id.promo);
        meetFriendsPromo = (TextView) view.findViewById(R.id.meet_friends_promo);

        meet = view.findViewById(R.id.meet);
        meet.setOnClickListener(this);
        if (!TextUtils.isEmpty(user.getInviteCode())) {
            promo.setText(user.getInviteCode());

            promoLink.setText(String.format(getString(R.string.promo_link), user.getInviteCode()));
        }

        SpannableString spannableString = promoSum();
        meetFriendsPromo.setText(spannableString);
    }

    private SpannableString promoSum() {
        Settings settings = Storage.load(getActivity(), Config.SETTINGS, Settings.class);
        String sum = String.valueOf(Double.valueOf(settings.getPartnerSignUpBonus()).intValue()) + " " + getString(R.string.roubles);
        String format = String.format(getString(R.string.meet_friends_promo), sum);
        SpannableString spannableString = new SpannableString(format);
        int start = format.indexOf(sum);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), start, start + sum.length(), 0);
        return spannableString;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.meet:
                makeMeeting();
                break;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.meet_friends_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_meet_friends:
                makeMeeting();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void makeMeeting() {
        listener.sendInviteMessage();
    }
}
