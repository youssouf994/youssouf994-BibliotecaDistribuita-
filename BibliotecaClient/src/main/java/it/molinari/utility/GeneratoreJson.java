package it.molinari.utility;

import java.util.List;

public class GeneratoreJson {

    private String token;
    private ActionType actionType;


    private List<Object> listaData;

    // Costruttore
    public GeneratoreJson(String token, ActionType actionType, List<Object> listaData) {
        this.token = token;
        this.actionType = actionType;
        this.listaData = listaData;
    }

    public GeneratoreJson() {}

    // Metodo STATICO usato dal server per generare correttamente l'oggetto
    public static GeneratoreJson getOggettoRequest(String token, ActionType actionType, List<Object> data) {
        return new GeneratoreJson(token, actionType, data);
    }

    //getter/setter

    public String getToken() {
        return token;
    }

    public ActionType getActionType() {
        return actionType;
    }

    public List<Object> getListaData() {
        return listaData;
    }

    public void setToken(String t) { token = t; }
    public void setActionType(ActionType a) { actionType = a; }
    public void setListaData(List<Object> d) { listaData = d; }
}

