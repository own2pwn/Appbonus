package com.appbonus.android.model.api;

import com.appbonus.android.model.Offer;

import java.io.Serializable;

public class OfferWrapper implements Serializable {
    protected Offer offer;

    public Offer getOffer() {
        return offer;
    }
}
