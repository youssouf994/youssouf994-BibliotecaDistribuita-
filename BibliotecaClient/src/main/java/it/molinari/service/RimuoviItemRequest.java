package it.molinari.service;

import it.molinari.model.Item;
import it.molinari.utility.ActionType;

public class RimuoviItemRequest extends Request {

    private Item item;

    public RimuoviItemRequest(String token, Item item) {
        super(token, ActionType.REMOVE_ITEMS_REQUEST);
        this.item = item;
    }

    public Item getItem() { return item; }
}
