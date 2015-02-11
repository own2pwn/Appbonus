package com.appbonus.android.ui.fragments.offer;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.loaders.OffersLoader;
import com.appbonus.android.model.Meta;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.friends.MeetFriendsFragment;
import com.appbonus.android.ui.fragments.profile.ProfileBrowserFragment;
import com.appbonus.android.ui.helper.RoubleHelper;
import com.dolphin.loader.AbstractLoader;
import com.dolphin.ui.fragment.root.RootListFragment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.paging.listview.PagingBaseAdapter;
import com.paging.listview.PagingListView;

import java.util.List;

public class OfferListFragment extends RootListFragment<PagingListView, OfferListFragment.OfferAdapter>
        implements LoaderManager.LoaderCallbacks<OffersWrapper>, View.OnClickListener {
    public static final int LOADER_ID = 1;

    protected Api api;

    protected TextView balance;
    protected View inputProfile;
    protected View meetFriends;
    protected ImageView inputProfileAvatar;
    protected TextView inputProfileCost;
    protected TextView inputProfileText;

    protected OfferAdapter adapter;
    protected Meta meta;

    protected long currentPage = -1L;

    protected Typeface typeFace;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
        typeFace = Typeface.createFromAsset(getActivity().getAssets(), "rouble.otf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        View header = inflateView(R.layout.offer_list_header);
        initUI(header);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDrawerIndicatorEnabled(true);
        setTitle(R.string.offers);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            getLoaderManager().restartLoader(LOADER_ID, null, this);
        }
    }

    private void initUI(View header) {
        listView.addHeaderView(header, null, false);
        balance = (TextView) header.findViewById(R.id.balance);

        inputProfile = header.findViewById(R.id.input_profile);
        inputProfileAvatar = (ImageView) inputProfile.findViewById(R.id.avatar);
        inputProfileCost = (TextView) inputProfile.findViewById(R.id.cost);
        inputProfileText = (TextView) inputProfile.findViewById(android.R.id.text1);
        meetFriends = header.findViewById(R.id.action_meet_friends);

        changeInputProfileView(getActivity());
        balance.setText(RoubleHelper.convert(SharedPreferencesStorage.getBalance(getActivity())));
        balance.setTypeface(typeFace);

        inputProfile.setOnClickListener(this);
        meetFriends.setOnClickListener(this);
    }

    private void changeInputProfileView(Context context) {
        if (/*!SharedPreferencesStorage.isPhoneConfirmed(context)*/false) {
            inputProfileCost.setText(R.string.input_profile_cost);
            inputProfileCost.setTypeface(typeFace);
            inputProfileText.setText(R.string.input_profile);
            inputProfileText.setTextColor(getResources().getColor(R.color.main_green));
        } else inputProfile.setVisibility(View.GONE);
    }

    @Override
    public Loader<OffersWrapper> onCreateLoader(int id, Bundle args) {
        OffersLoader loader = new OffersLoader(getActivity(), api, currentPage + 1);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<OffersWrapper> loader, OffersWrapper data) {
        if (((AbstractLoader) loader).isSuccess()) {
            meta = data.getMeta();
            setData(data);
            currentPage = meta.getCurrentPage();
        }
    }

    private void setData(OffersWrapper data) {
        if (adapter == null) {
            adapter = new OfferAdapter(data.getOffers());
            setListSettings();
            setListAdapter(adapter);
        } else {
            if (currentPage == meta.getCurrentPage()) {
                setListAdapter(adapter);
                setListSettings();
            } else listView.onFinishLoading(currentPage != meta.getTotalPages(), data.getOffers());
        }
    }

    private void setListSettings() {
        if (meta.getTotalPages() == 1) {
            listView.setHasMoreItems(false);
        } else {
            listView.setHasMoreItems(true);
            listView.setPagingableListener(new PagingListView.Pagingable() {
                @Override
                public void onLoadMoreItems() {
                    if (currentPage != meta.getTotalPages()) {
                        Loader<OffersWrapper> loader = getLoaderManager().restartLoader(LOADER_ID, null, OfferListFragment.this);
                        loader.forceLoad();
                    } else listView.onFinishLoading(false, null);
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<OffersWrapper> loader) {
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.input_profile:
                placeProperFragment(ProfileBrowserFragment.class.getName());
                break;
            case R.id.action_meet_friends:
                placeProperFragment(MeetFriendsFragment.class.getName());
                break;
        }
    }

    @Override
    protected int layout() {
        return R.layout.offers_list;
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

    public class OfferAdapter extends PagingBaseAdapter<Offer> {
        public static final int TYPE_COUNT = 2;
        public static final int SIMPLE_OFFER = 0;
        public static final int DONE_OFFER = 1;

        protected DisplayImageOptions options;

        public OfferAdapter(List<Offer> offers) {
            super(offers);
            options = new DisplayImageOptions.Builder()
                    .resetViewBeforeLoading(true)
                    .cacheInMemory(true)
                    .cacheOnDisc(true)
                    .displayer(new RoundedBitmapDisplayer(10))
                    .build();
        }

        @Override
        public int getItemViewType(int position) {
            Offer offer = (Offer) getItem(position);
            return offer.isDone() ? DONE_OFFER : SIMPLE_OFFER;
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_COUNT;
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
            if (convertView == null) {
                int type = getItemViewType(position);
                viewHolder = new ViewHolder();

                switch (type) {
                    case SIMPLE_OFFER:
                        convertView = inflateView(R.layout.offer_view);
                        viewHolder.cost = (TextView) convertView.findViewById(R.id.cost);
                        viewHolder.cost.setTypeface(typeFace);
                        break;
                    case DONE_OFFER:
                        convertView = inflateView(R.layout.offer_view_ok);
                        break;
                }
                viewHolder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
                viewHolder.title = (TextView) convertView.findViewById(android.R.id.text1);
                convertView.setTag(viewHolder);
            } else viewHolder = (ViewHolder) convertView.getTag();

            final Offer offer = (Offer) getItem(position);
            viewHolder.title.setText(offer.getTitle());
            if (viewHolder.cost != null) {
                viewHolder.cost.setText("+" + RoubleHelper.convert(offer.getReward()));
            }
            ImageLoader.getInstance().displayImage(offer.getIcon(), viewHolder.avatar, options);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(offer);
                }
            });

            return convertView;
        }

        public void onItemClick(Offer offer) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("offer", offer);
            placeProperFragment(OfferBrowserFragment.class.getName(), bundle);
        }

        class ViewHolder {
            TextView title;
            ImageView avatar;
            TextView cost;
        }
    }
}
