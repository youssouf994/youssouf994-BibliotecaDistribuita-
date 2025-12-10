package it.molinari.service;

import it.molinari.model.ItemPrestato;
import it.molinari.utility.ActionType;

public class PrestitoRequest extends Request {

    private ItemPrestato item;

    public PrestitoRequest(String token, ItemPrestato item) {
        super(token, ActionType.BORROW_ITEM_REQUEST);
        this.item = item;
    }

    public ItemPrestato getItem() { return item; }
}
