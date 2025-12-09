package it.molinari.service;

import it.molinari.utility.ActionType;

public class Response {

    private String code;
    private String message;
    private String token;
    private ActionType actionType;
    private String dataJson;

    public Response(String raw) {
        String[] parts = raw.split("\t");
        this.code = parts.length>0?parts[0]:"";
        this.message = parts.length>1?parts[1]:"";
        this.token = parts.length>2?parts[2]:"";
        this.actionType = parts.length>3?ActionType.valueOf(parts[3]):null;
        this.dataJson = parts.length>4?parts[4]:"";
    }

    public boolean isOk() { return "200".equals(code); }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public ActionType getActionType() {
		return actionType;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public String getDataJson() {
		return dataJson;
	}

	public void setDataJson(String dataJson) {
		this.dataJson = dataJson;
	}
    
    
}

