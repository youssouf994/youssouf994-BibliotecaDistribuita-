package it.molinari.service;

import it.molinari.model.Item;
import it.molinari.utility.ActionType;

public class AggiungiItemRequest extends Request {

    private Item item;

    public AggiungiItemRequest(String token, Item item) {
        super(token, ActionType.ADD_ITEMS_REQUEST);
        this.item = item;
    }

    public Item getItem() { return item; }
}

