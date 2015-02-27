package com.appbonus.android.model.api;

import com.appbonus.android.model.Meta;
import com.appbonus.android.model.Offer;

import java.io.Serializable;
import java.util.List;

public class OffersWrapper implements Serializable {
    protected Meta meta;
    protected List<Offer> offers;

    public Meta getMeta() {
        return meta;
    }

    public List<Offer> getOffers() {
        return offers;
    }
}
