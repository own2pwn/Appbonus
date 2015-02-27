package com.appbonus.android.loaders;

import android.content.Context;

import com.appbonus.android.api.Api;
import com.appbonus.android.api.model.PagingRequest;
import com.appbonus.android.api.model.SimpleRequest;
import com.appbonus.android.model.Offer;
import com.appbonus.android.model.api.DoneOffersWrapper;
import com.appbonus.android.model.api.OffersWrapper;
import com.appbonus.android.storage.SharedPreferencesStorage;
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
        String token = SharedPreferencesStorage.getToken(getContext());
        DoneOffersWrapper doneOffers = api.readDoneIds(new SimpleRequest(token));
        OffersWrapper offers = api.getOffers(new PagingRequest(token, page));
        return markCompletedOffers(offers, doneOffers);
    }

    private OffersWrapper markCompletedOffers(OffersWrapper offersWrapper, DoneOffersWrapper doneOffersWrapper) {
        List<Offer> offers = offersWrapper.getOffers();
        int[] doneIds = doneOffersWrapper.getDoneIds();
        if (CollectionUtils.isNotEmpty(offers)) {
            if (ArrayUtils.isNotEmpty(doneIds)) {
                for (Offer offer : offers) {
                    offer.setCompleted(ArrayUtils.contains(doneIds, offer.getId().intValue()));
                }
            }
        }
        return offersWrapper;
    }
}
