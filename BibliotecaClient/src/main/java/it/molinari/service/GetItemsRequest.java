package it.molinari.service;

import it.molinari.utility.ActionType;

public class GetItemsRequest extends Request {
	
	public GetItemsRequest(String token) {
        super(token, ActionType.GET_ITEMS_REQUEST);
    }
}

