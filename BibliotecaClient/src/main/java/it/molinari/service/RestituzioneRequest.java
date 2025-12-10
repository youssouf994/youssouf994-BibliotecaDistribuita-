package it.molinari.service;

import it.molinari.model.ItemPrestato;
import it.molinari.utility.ActionType;

public class RestituzioneRequest extends Request {

    private ItemPrestato item;

    public RestituzioneRequest(String token, ItemPrestato item) {
        super(token, ActionType.RETURN_ITEM_REQUEST);
        this.item = item;
    }

    public ItemPrestato getItem() { return item; }
}
