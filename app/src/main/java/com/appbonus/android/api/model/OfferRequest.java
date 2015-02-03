package com.appbonus.android.api.model;

import com.appbonus.android.model.Offer;

public class OfferRequest extends SimpleRequest {
    protected Offer offer;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
