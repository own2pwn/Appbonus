package com.appbonus.android.api.model;

import com.appbonus.android.model.Offer;

public class OfferRequest extends SimpleRequest {
    protected Offer offer;

    public OfferRequest(String authToken, Offer offer) {
        super(authToken);
        this.offer = offer;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Long getId() {
        return offer.getId();
    }
}
