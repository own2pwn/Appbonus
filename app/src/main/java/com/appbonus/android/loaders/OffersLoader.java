package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.api.DoneOffersWrapper;
import com.appbonus.android.model.api.OffersWrapper;
import com.dolphin.loader.AbstractLoader;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class OffersLoader extends AbstractLoader<OffersWrapper> {
    protected Api api;
    protected long page;

    public OffersLoader(Context context, Api api, long page) {
        super(context);
        this.api = api;
        this.page = page;
    }

    @Override
    protected OffersWrapper backgroundLoading() throws Throwable {
        DoneOffersWrapper doneOffers = api.readDoneIds();
        OffersWrapper offers = api.getOffers(new PagingRequest(page));
        return markCompletedOffers(offers, doneOffers);
    }

    private OffersWrapper markCompletedOffers(OffersWrapper offersWrapper, DoneOffersWrapper doneOffersWrapper) {
        List<Offer> offers = offersWrapper.getOffers();
        if (CollectionUtils.isNotEmpty(offers)) {
            for (Offer offer : offers) {
                boolean done = ArrayUtils.contains(doneOffersWrapper.getDoneIds(), offer.getId().intValue());
                boolean installed = ArrayUtils.contains(doneOffersWrapper.getInstalledIds(), offer.getId().intValue());
                offer.setInstalled(installed);
                offer.setSharingEnable(!done);
            }
        }
        return offersWrapper;
    }
}
