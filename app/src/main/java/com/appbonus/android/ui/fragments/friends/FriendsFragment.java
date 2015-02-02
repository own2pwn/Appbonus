package com.appbonus.android.ui.fragments.friends;

import android.annotation.TargetApi;
import android.graphics.Typeface;
import android.os.Build;
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
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.loaders.ReferralsDetailsLoader;
import com.appbonus.android.loaders.ReferralsHistoryLoader;
import com.appbonus.android.model.Level;
import com.appbonus.android.model.Meta;
import com.appbonus.android.model.ReferralsDetails;
import com.appbonus.android.model.ReferralsHistory;
import com.appbonus.android.model.api.ReferralsDetailsWrapper;
import com.appbonus.android.model.api.ReferralsHistoryWrapper;
import com.appbonus.android.ui.fragments.profile.settings.faq.FaqListFragment;
import com.appbonus.android.ui.helper.RoubleHelper;
import com.dolphin.activity.fragment.BaseFragment;
import com.dolphin.loader.AbstractLoader;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;

import java.util.List;

public class FriendsFragment extends BaseFragment implements View.OnClickListener {
    public static final int REFERRALS_DETAILS_LOADER_ID = 1;
    public static final int REFERRALS_HISTORY_LOADER_ID = 2;

    protected Api api;

    protected PagingListView listView;
    protected View footer;
    protected View emptyFooter;
    protected ReferralsHistoryAdapter adapter;
    protected Meta meta;

    protected long currentPage = -1L;
    protected int top;
    protected int selectionItem;

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

    protected ReferralsDetailsHandler referralsDetailsHandler;
    protected ReferralsHistoryHandler referralsHistoryHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "rouble.otf");
        referralsDetailsHandler = new ReferralsDetailsHandler();
        referralsHistoryHandler = new ReferralsHistoryHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friends_layout, null);
        View header = inflater.inflate(R.layout.friends_header, null);
        footer = inflater.inflate(R.layout.more_footer, null);
        emptyFooter = inflater.inflate(R.layout.empty_footer, null);
        initUI(view, header);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getLoaderManager().getLoader(REFERRALS_DETAILS_LOADER_ID) == null) {
            Loader<ReferralsDetailsWrapper> loader = getLoaderManager()
                    .initLoader(REFERRALS_DETAILS_LOADER_ID, null, referralsDetailsHandler);
            loader.forceLoad();
        }
        if (getLoaderManager().getLoader(REFERRALS_HISTORY_LOADER_ID) == null) {
            Loader<ReferralsHistoryWrapper> loader = getLoaderManager()
                    .initLoader(REFERRALS_HISTORY_LOADER_ID, null, referralsHistoryHandler);
            loader.forceLoad();
        }
        setTitle(R.string.friends);
        setDrawerIndicatorEnabled(true);
    }

    private void initUI(View view, View header) {
        listView = (PagingListView) view.findViewById(android.R.id.list);

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
                Loader<ReferralsHistoryWrapper> loader =
                        getLoaderManager().restartLoader(REFERRALS_HISTORY_LOADER_ID, null, referralsHistoryHandler);
                loader.forceLoad();
            } else listView.onFinishLoading(false, null);
            return;
        }
        switch (id) {
            case R.id.referrals_info:
                placeProperFragment(FaqListFragment.class.getName());
                break;
            case R.id.action_meet_friends:
                placeProperFragment(MeetFriendsFragment.class.getName());
                break;
        }
    }

    public class ReferralsDetailsHandler implements LoaderManager.LoaderCallbacks<ReferralsDetailsWrapper> {

        @Override
        public Loader<ReferralsDetailsWrapper> onCreateLoader(int id, Bundle args) {
            return new ReferralsDetailsLoader(getActivity(), api);
        }

        @Override
        public void onLoadFinished(Loader<ReferralsDetailsWrapper> loader, ReferralsDetailsWrapper data) {
            if (((AbstractLoader) loader).isSuccess())
                setData(data.getReferralsDetails());
        }

        @Override
        public void onLoaderReset(Loader<ReferralsDetailsWrapper> loader) {
        }
    }

    public class ReferralsHistoryHandler implements LoaderManager.LoaderCallbacks<ReferralsHistoryWrapper> {

        @Override
        public Loader<ReferralsHistoryWrapper> onCreateLoader(int id, Bundle args) {
            return new ReferralsHistoryLoader(getActivity(), api, currentPage);
        }

        @Override
        public void onLoadFinished(Loader<ReferralsHistoryWrapper> loader, ReferralsHistoryWrapper data) {
            if (((AbstractLoader) loader).isSuccess()) {
                meta = data.getMeta();
                if (meta.getTotalCount() == 0) {
                    hideMoreFooter();
                }
                setReferralsHistory(data);
                currentPage = meta.getCurrentPage();
            }
        }

        @Override
        public void onLoaderReset(Loader<ReferralsHistoryWrapper> loader) {
        }
    }

    private void hideMoreFooter() {
        listView.removeFooterView(footer);
        listView.addFooterView(emptyFooter, null, false);
        listView.onFinishLoading(false, null);
    }

    private void setReferralsHistory(ReferralsHistoryWrapper data) {
        if (adapter == null) {
            adapter = new ReferralsHistoryAdapter(data.getReferralsHistory());
            listView.setAdapter(adapter);
            setListSettings();
        } else {
            if (currentPage == meta.getCurrentPage()) {
                listView.setAdapter(adapter);
                setListSettings();
            } else listView.onFinishLoading(false, data.getReferralsHistory());
        }
    }

    private void setListSettings() {
        if (meta.getTotalPages() == 1) {
            footer.setOnClickListener(null);
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
    public void onPause() {
        selectionItem = listView.getFirstVisiblePosition();
        View v = listView.getChildAt(0);
        top = (v == null) ? 0 : v.getTop();
        super.onPause();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onResume() {
        super.onResume();
        listView.setSelectionFromTop(selectionItem, top);
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
