package com.appbonus.android.api.model;

import com.appbonus.android.model.Offer;

public class OfferRequest {
    protected Offer offer;

    public OfferRequest(Offer offer) {
        this.offer = offer;
    }

    public Long getId() {
        return offer.getId();
    }
}
