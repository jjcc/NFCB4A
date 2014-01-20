package com.nfc.common;

public class NDEFStructure {

	private String message;
	private String date;
	private String type;

	public NDEFStructure(String message, String date, String type)
	{
		super();
		this.setMessage(message);
		this.setDate(date);
		this.setType(type);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

}
