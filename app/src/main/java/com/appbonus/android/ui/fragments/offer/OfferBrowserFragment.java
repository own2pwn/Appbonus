package com.appbonus.android.ui.fragments.offer;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appbonus.android.R;
import com.appbonus.android.api.Api;
import com.appbonus.android.api.ApiImpl;
import com.appbonus.android.loaders.OfferLoader;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.api.OfferWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
import com.appbonus.android.ui.fragments.profile.settings.faq.FaqListFragment;
import com.appbonus.android.ui.helper.RoubleHelper;
import com.dolphin.helper.IntentHelper;
import com.dolphin.loader.AbstractLoader;
import com.dolphin.ui.fragment.SimpleFragment;
import com.dolphin.utils.ClipboardUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class OfferBrowserFragment extends SimpleFragment implements LoaderManager.LoaderCallbacks<OfferWrapper>, View.OnClickListener {
    private static final int LOADER_ID = 1;

    protected Api api;

    protected ImageView avatar;
    protected TextView title;
    protected TextView cost;
    protected TextView description;
    protected Button downloadBtn;
    protected View rules;
    protected View share;

    protected Offer offer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = new ApiImpl(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_browse_layout, null);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        avatar = (ImageView) view.findViewById(R.id.avatar);
        title = (TextView) view.findViewById(R.id.title);
        cost = (TextView) view.findViewById(R.id.cost);
        cost.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "rouble.otf"));
        description = (TextView) view.findViewById(R.id.description);
        downloadBtn = (Button) view.findViewById(R.id.download);
        downloadBtn.setOnClickListener(this);
        rules = view.findViewById(R.id.rules_button);
        rules.setOnClickListener(this);
        share = view.findViewById(R.id.share);
        share.setOnClickListener(this);

        setPrimaryData();
    }

    private void setPrimaryData() {
        Bundle bundle = getArguments();
        offer = (Offer) bundle.getSerializable("offer");

        ImageLoader.getInstance().displayImage(offer.getIcon(), avatar, new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(10))
                .build());
        title.setText(offer.getTitle());
        cost.setText("+" + RoubleHelper.convert(offer.getReward()));
        description.setText(offer.getDescription());
        if (offer.isDone()) {
            downloadBtn.setEnabled(false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setDrawerIndicatorEnabled(false);
        if (getLoaderManager().getLoader(LOADER_ID) == null) {
            Loader<OfferWrapper> loader = getLoaderManager().initLoader(LOADER_ID, null, this);
            loader.forceLoad();
        }
    }

    @Override
    public Loader<OfferWrapper> onCreateLoader(int id, Bundle args) {
        return new OfferLoader(getActivity(), api, offer);
    }

    @Override
    public void onLoadFinished(Loader<OfferWrapper> loader, OfferWrapper data) {
        if (((AbstractLoader) loader).isSuccess()) {
            setData(data);
        }
    }

    private void setData(OfferWrapper data) {
        offer = data.getOffer();
    }

    @Override
    public void onLoaderReset(Loader<OfferWrapper> loader) {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.rules_button:
                placeProperFragment(FaqListFragment.class.getName());
                break;
            case R.id.download:
                if (!offer.isDone()) {
                    String link = offer.getDownloadLink();
                    download(link);
                }
                break;
            case R.id.share:
                share();
                break;
        }
    }

    private void download(String link) {
        Log.d("download_link", link);
        startActivity(IntentHelper.openLink(link));
    }

    private void share() {
        String referrerLink = String.format(getString(R.string.referrer_app_link), SharedPreferencesStorage.getUserId(getActivity()));
        ClipboardUtils.copyToClipboard(getActivity(), referrerLink);
        Toast.makeText(getActivity(), R.string.referrer_link_was_copied, Toast.LENGTH_SHORT).show();
    }
}
