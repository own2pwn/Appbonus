package com.appbonus.android.ui.fragments.balance;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.loaders.BalanceLoader;
import com.appbonus.android.loaders.HistoryLoader;
import com.appbonus.android.model.Balance;
import com.appbonus.android.model.History;
import com.appbonus.android.model.Meta;
import com.appbonus.android.model.api.BalanceWrapper;
import com.appbonus.android.model.api.HistoryWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.balance.autowithdrawal.AutowithdrawalFragment;
import com.appbonus.android.ui.fragments.balance.withdrawal.WithdrawalFragment;
import com.appbonus.android.ui.fragments.profile.ProfileBrowserFragment;
import com.appbonus.android.ui.helper.RoubleHelper;
import com.dolphin.activity.fragment.root.RootBaseFragment;
import com.dolphin.loader.AbstractLoader;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;

import java.util.List;

public class BalanceBrowserFragment extends RootBaseFragment implements View.OnClickListener {
    public static final int BALANCE_LOADER_ID = 1;
    public static final int HISTORY_LOADER_ID = 2;

    protected Api api;
    protected BalanceHandler balanceHandler;
    protected HistoryHandler historyHandler;

    protected Typeface typeface;

    protected TextView currentBalance;
    protected TextView myProfit;
    protected TextView friendsProfit;
    protected TextView withdrawalSum;

    protected Button withdrawalMoney;
    protected Button autowithdrawal;
    protected View withdrawalIsNotAccessView;

    protected PagingListView listView;
    protected View footer;
    protected View emptyFooter;
    protected Meta meta;

    protected long currentPage = -1L;
    protected int selectionItem;
    protected int top;
    protected HistoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
        typeface = Typeface.createFromAsset(getActivity().getAssets(), "rouble.otf");
        balanceHandler = new BalanceHandler();
        historyHandler = new HistoryHandler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.balance_layout, null);
        View header = inflater.inflate(R.layout.balance_header, null);
        footer = inflater.inflate(R.layout.more_footer, null);
        emptyFooter = inflater.inflate(R.layout.empty_footer, null);
        initUI(view, header);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getLoaderManager().getLoader(BALANCE_LOADER_ID) == null) {
            Loader<BalanceWrapper> loader = getLoaderManager().initLoader(BALANCE_LOADER_ID, null, balanceHandler);
            loader.forceLoad();
        }
        if (getLoaderManager().getLoader(HISTORY_LOADER_ID) == null) {
            Loader<HistoryWrapper> loader = getLoaderManager().initLoader(HISTORY_LOADER_ID, null, historyHandler);
            loader.forceLoad();
        }
        setDrawerIndicatorEnabled(true);
        setTitle(R.string.balance);
        setWithdrawalVisibility();
    }

    private void initUI(View view, View header) {
        currentBalance = (TextView) header.findViewById(R.id.current_balance);
        currentBalance.setTypeface(typeface);
        myProfit = (TextView) header.findViewById(R.id.my_profit);
        myProfit.setTypeface(typeface);
        friendsProfit = (TextView) header.findViewById(R.id.friend_profit);
        friendsProfit.setTypeface(typeface);
        withdrawalSum = (TextView) header.findViewById(R.id.withdrawal_sum);
        withdrawalSum.setTypeface(typeface);

        withdrawalMoney = (Button) header.findViewById(R.id.withdrawal_money);
        autowithdrawal = (Button) header.findViewById(R.id.auto_withdrawal);
        withdrawalIsNotAccessView = header.findViewById(R.id.withdrawal_is_not_access_view);

        withdrawalMoney.setOnClickListener(this);
        autowithdrawal.setOnClickListener(this);
        withdrawalIsNotAccessView.setOnClickListener(this);

        listView = (PagingListView) view.findViewById(android.R.id.list);
        listView.addHeaderView(header);
        listView.addFooterView(footer);

        setWithdrawalVisibility();
    }


    private void setWithdrawalVisibility() {
        boolean phoneConfirmed = SharedPreferencesStorage.isPhoneConfirmed(getActivity());
        withdrawalMoney.setVisibility(phoneConfirmed ? View.VISIBLE : View.GONE);
        withdrawalIsNotAccessView.setVisibility(phoneConfirmed ? View.GONE : View.VISIBLE);
    }

    private void setBalance(BalanceWrapper data) {
        Balance balance = data.getBalance();
        currentBalance.setText(createRouble(balance.getActiveBalance()));
        myProfit.setText("+" + createRouble(balance.getTasksProfit()));
        friendsProfit.setText("+" + createRouble(balance.getReferralsProfit()));
        withdrawalSum.setText("-" + createRouble(balance.getWithdrawalSum()));
    }

    private String createRouble(Object obj) {
        return RoubleHelper.convert(obj);
    }

    private void setHistory(HistoryWrapper data) {
        if (adapter == null) {
            adapter = new HistoryAdapter(data.getHistory());
            listView.setAdapter(adapter);
            setListSettings();
        } else {
            if (currentPage == meta.getCurrentPage()) {
                listView.setAdapter(adapter);
                setListSettings();
            } else listView.onFinishLoading(false, data.getHistory());
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (footer.equals(v)) {
            if (currentPage != meta.getTotalPages()) {
                Loader<HistoryWrapper> loader =
                        getLoaderManager().restartLoader(HISTORY_LOADER_ID, null, historyHandler);
                loader.forceLoad();
            } else hideMoreFooter();
            return;
        }
        switch (id) {
            case R.id.withdrawal_money:
                placeProperFragment(WithdrawalFragment.class.getName());
                break;
            case R.id.auto_withdrawal:
                placeProperFragment(AutowithdrawalFragment.class.getName());
                break;
            case R.id.withdrawal_is_not_access_view:
                placeProperFragment(ProfileBrowserFragment.class.getName());
                break;
        }
    }

    public class BalanceHandler implements LoaderManager.LoaderCallbacks<BalanceWrapper> {

        @Override
        public Loader<BalanceWrapper> onCreateLoader(int id, Bundle args) {
            return new BalanceLoader(getActivity(), api);
        }

        @Override
        public void onLoadFinished(Loader<BalanceWrapper> loader, BalanceWrapper data) {
            if (((AbstractLoader) loader).isSuccess()) {
                setBalance(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<BalanceWrapper> loader) {
        }
    }

    public class HistoryHandler implements LoaderManager.LoaderCallbacks<HistoryWrapper> {

        @Override
        public Loader<HistoryWrapper> onCreateLoader(int id, Bundle args) {
            return new HistoryLoader(getActivity(), api, currentPage + 1);
        }

        @Override
        public void onLoadFinished(Loader<HistoryWrapper> loader, HistoryWrapper data) {
            if (((AbstractLoader) loader).isSuccess()) {
                meta = data.getMeta();
                if (meta.getTotalCount() == 0 || meta.getCurrentPage().equals(meta.getTotalPages())) {
                    hideMoreFooter();
                }
                setHistory(data);
                currentPage = meta.getCurrentPage();
            }
        }

        @Override
        public void onLoaderReset(Loader<HistoryWrapper> loader) {
        }
    }

    private void hideMoreFooter() {
        listView.removeFooterView(footer);
        listView.addFooterView(emptyFooter, null, false);
        listView.onFinishLoading(false, null);
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

    public class HistoryAdapter extends PagingBaseAdapter<History> {
        public static final int TYPE_COUNT = 3;
        public static final int OPERATION_TYPE_WITHDRAWAL = 0;
        public static final int OPERATION_TYPE_PROFIT = 1;
        public static final int OPERATION_TYPE_IN_PROGRESS = 2;

        protected DisplayImageOptions options;

        public HistoryAdapter(List<History> items) {
            super(items);
            options = new DisplayImageOptions.Builder()
                    .displayer(new RoundedBitmapDisplayer(10))
                    .build();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            History item = (History) getItem(position);
            int itemViewType = getItemViewType(position);
            ViewHolder viewHolder;
            String amountSign = "";
            if (convertView == null) {
                viewHolder = new ViewHolder();
                switch (itemViewType) {
                    case OPERATION_TYPE_WITHDRAWAL:
                        convertView = inflateView(R.layout.balance_withdrawal);
                        break;
                    case OPERATION_TYPE_PROFIT:
                        convertView = inflateView(R.layout.balance_profit);
                        viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                        break;
                    case OPERATION_TYPE_IN_PROGRESS:
                        convertView = inflateView(R.layout.balance_withdrawal_in_progress);
                        break;
                }
                viewHolder.amount = (TextView) convertView.findViewById(R.id.amount);
                viewHolder.description = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();

            if (itemViewType == OPERATION_TYPE_PROFIT) {
                viewHolder.description.setText(createDescription(item));
                amountSign = "+";
            }
            viewHolder.amount.setTypeface(typeface);
            viewHolder.amount.setText(amountSign + createRouble(item.getAmount()));
            if (viewHolder.avatar != null) {
                ImageLoader.getInstance().displayImage(item.getOfferIcon(), viewHolder.avatar, options);
            }
            return convertView;
        }

        private String createDescription(History item) {
            return String.format(getString(R.string.balance_profit), item.getOfferTitle());
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
        public int getItemViewType(int position) {
            History item = (History) getItem(position);
            String operationType = item.getOperationType();

            switch (operationType) {
                case History.OPERATION_TYPE_WITHDRAWAL:
                    return OPERATION_TYPE_WITHDRAWAL;
                case History.OPERATION_TYPE_PROFIT:
                    return OPERATION_TYPE_PROFIT;
                case History.OPERATION_TYPE_IN_PROGRESS:
                    return OPERATION_TYPE_IN_PROGRESS;
            }
            return -1;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
        }

        class ViewHolder {
            TextView amount;
            TextView description;
            ImageView avatar;
        }
    }
}
