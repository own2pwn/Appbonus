package com.appbonus.android.ui.fragments.friends;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.model.Level;
import com.appbonus.android.model.Meta;
import com.appbonus.android.model.ReferralsDetails;
import com.appbonus.android.model.ReferralsHistory;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.appbonus.android.ui.fragments.profile.settings.faq.ReferralsInfoFragment;
import com.appbonus.android.ui.helper.RoubleHelper;
import com.dolphin.loader.AbstractLoader;
import com.dolphin.ui.LoadingDialogHelper;
import com.dolphin.ui.fragment.root.RootListFragment;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;

import java.util.List;

public class FriendsFragment extends RootListFragment<PagingListView, FriendsFragment.ReferralsHistoryAdapter> implements View.OnClickListener {
    public static final int REFERRALS_DETAILS_LOADER_ID = 1;
    public static final int REFERRALS_HISTORY_LOADER_ID = 2;

    protected View footer;
    protected View emptyFooter;
    protected Meta meta;

    protected long currentPage = -1L;

    protected TextView friendsProfit;

    protected TextView firstLevel;
    protected TextView secondLevel;
    protected TextView thirdLevel;

    protected TextView firstLevelCount;
    protected TextView secondLevelCount;
    protected TextView thirdLevelCount;

    protected View referralsInfo;
    protected View meetFriends;

    protected Typeface typeface;

    protected FriendsFragmentListener listener;

    protected ReferralsDetailsHandler referralsDetailsHandler;
    protected ReferralsHistoryHandler referralsHistoryHandler;

    public interface FriendsFragmentListener extends LoadingDialogHelper {
        Loader<ReferralsDetailsWrapper> createReferralsDetailsLoader();

        Loader<ReferralsHistoryWrapper> createReferralsHistoryLoader(long page);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listener = (FriendsFragmentListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "rouble.otf");
        referralsDetailsHandler = new ReferralsDetailsHandler();
        referralsHistoryHandler = new ReferralsHistoryHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View header = inflater.inflate(R.layout.friends_header, null);
        footer = inflater.inflate(R.layout.more_footer, null);
        emptyFooter = inflater.inflate(R.layout.empty_footer, null);
        initUI(header);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getLoaderManager().getLoader(REFERRALS_DETAILS_LOADER_ID) == null) {
            getLoaderManager().initLoader(REFERRALS_DETAILS_LOADER_ID, null, referralsDetailsHandler);
        }
        if (getLoaderManager().getLoader(REFERRALS_HISTORY_LOADER_ID) == null) {
            getLoaderManager().initLoader(REFERRALS_HISTORY_LOADER_ID, null, referralsHistoryHandler);
        }
        setTitle(R.string.friends);
        setDrawerIndicatorEnabled(true);
    }

    private void initUI(View header) {
        friendsProfit = (TextView) header.findViewById(R.id.friend_profit);
        friendsProfit.setTypeface(typeface);

        firstLevel = (TextView) header.findViewById(R.id.first_level);
        firstLevel.setTypeface(typeface);
        secondLevel = (TextView) header.findViewById(R.id.second_level);
        secondLevel.setTypeface(typeface);
        thirdLevel = (TextView) header.findViewById(R.id.third_level);
        thirdLevel.setTypeface(typeface);
        firstLevelCount = (TextView) header.findViewById(R.id.first_level_count);
        secondLevelCount = (TextView) header.findViewById(R.id.second_level_count);
        thirdLevelCount = (TextView) header.findViewById(R.id.third_level_count);

        referralsInfo = header.findViewById(R.id.referrals_info);
        referralsInfo.setOnClickListener(this);

        meetFriends = header.findViewById(R.id.action_meet_friends);
        meetFriends.setOnClickListener(this);

        listView.addHeaderView(header, null, false);
        listView.addFooterView(footer, null, false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (footer.equals(v)) {
            if (currentPage != meta.getTotalPages()) {
                getLoaderManager().restartLoader(REFERRALS_HISTORY_LOADER_ID, null, referralsHistoryHandler);
            } else hideMoreFooter();
            return;
        }
        switch (id) {
            case R.id.referrals_info:
                placeProperFragment(ReferralsInfoFragment.class.getName());
                break;
            case R.id.action_meet_friends:
                placeProperFragment(MeetFriendsFragment.class.getName());
                break;
        }
    }

    public class ReferralsDetailsHandler implements LoaderManager.LoaderCallbacks<ReferralsDetailsWrapper> {

        @Override
        public Loader<ReferralsDetailsWrapper> onCreateLoader(int id, Bundle args) {
            Loader<ReferralsDetailsWrapper> loader = listener.createReferralsDetailsLoader();
            loader.forceLoad();
            listener.showLoadingDialog();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<ReferralsDetailsWrapper> loader, ReferralsDetailsWrapper data) {
            if (((AbstractLoader) loader).isSuccess())
                setData(data.getReferralsDetails());
            listener.dismissLoadingDialog();
        }

        @Override
        public void onLoaderReset(Loader<ReferralsDetailsWrapper> loader) {
            listener.dismissLoadingDialog();
        }
    }

    public class ReferralsHistoryHandler implements LoaderManager.LoaderCallbacks<ReferralsHistoryWrapper> {

        @Override
        public Loader<ReferralsHistoryWrapper> onCreateLoader(int id, Bundle args) {
            Loader<ReferralsHistoryWrapper> loader = listener.createReferralsHistoryLoader(currentPage + 1);
            loader.forceLoad();
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<ReferralsHistoryWrapper> loader, ReferralsHistoryWrapper data) {
            if (((AbstractLoader) loader).isSuccess()) {
                meta = data.getMeta();
                setReferralsHistory(data);
                currentPage = meta.getCurrentPage();
            }
        }

        @Override
        public void onLoaderReset(Loader<ReferralsHistoryWrapper> loader) {
        }
    }

    private void hideMoreFooter() {
        footer.setVisibility(View.GONE);
        listView.removeFooterView(footer);
        listView.addFooterView(emptyFooter, null, false);
        listView.onFinishLoading(false, null);
    }

    private void setReferralsHistory(ReferralsHistoryWrapper data) {
        if (adapter == null) {
            adapter = new ReferralsHistoryAdapter(data.getReferralsHistory());
            setListSettings();
            setListAdapter(adapter);
        } else {
            if (currentPage == meta.getCurrentPage()) {
                setListAdapter(adapter);
                setListSettings();
            } else listView.onFinishLoading(currentPage != meta.getTotalPages(), data.getReferralsHistory());
        }
    }

    private void setListSettings() {
        if (meta.getTotalPages() <= 1) {
            footer.setOnClickListener(null);
            listView.setHasMoreItems(false);
            hideMoreFooter();
        } else {
            listView.setHasMoreItems(false);
            footer.setOnClickListener(this);
        }
    }

    private void setData(ReferralsDetails data) {
        friendsProfit.setText(createRouble(data.getReferralsProfit()));
        List<Level> levels = data.getLevels();
        Level firstLevel = levels.get(0);
        Level secondLevel = levels.get(1);
        Level thirdLevel = levels.get(2);

        this.firstLevel.setText(createRouble(firstLevel.getProfit()));
        firstLevelCount.setText(createHumansCount(firstLevel.getCount()));

        this.secondLevel.setText(createRouble(secondLevel.getProfit()));
        secondLevelCount.setText(createHumansCount(secondLevel.getCount()));

        this.thirdLevel.setText(createRouble(thirdLevel.getProfit()));
        thirdLevelCount.setText(createHumansCount(thirdLevel.getCount()));
    }

    private String createHumansCount(Integer count) {
        return String.format(getString(R.string.human_count), count);
    }

    private String createRouble(Object obj) {
        return RoubleHelper.convert(obj);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                toggleDrawer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int layout() {
        return R.layout.friends_layout;
    }

    public class ReferralsHistoryAdapter extends PagingBaseAdapter<ReferralsHistory> {

        public ReferralsHistoryAdapter(List<ReferralsHistory> referralsHistory) {
            super(referralsHistory);
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            ReferralsHistory history = (ReferralsHistory) getItem(position);

            if (convertView == null) {
                convertView = inflateView(R.layout.referral_history_item);
                viewHolder = new ViewHolder();
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.mail = (TextView) convertView.findViewById(R.id.mail);
                viewHolder.amount.setTypeface(typeface);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.amount.setText(createRouble(history.getAmount()));
            viewHolder.mail.setText(history.getInitiator().getEmail());
            return convertView;
        }

        class ViewHolder {
            TextView amount;
            TextView mail;
        }
    }
}
