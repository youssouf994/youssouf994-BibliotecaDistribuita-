package it.molinari.service;

import it.molinari.utility.ActionType;

public class RicercaRequest extends Request {

    private String valore;
    private int modalita;

    public RicercaRequest(String token, String valore, int modalita) {
        super(token, ActionType.SEARCH_ITEMS_REQUEST);
        this.valore = valore;
        this.modalita = modalita;
    }

    public String getValore() { return valore; }
    public int getModalita() { return modalita; }
}
