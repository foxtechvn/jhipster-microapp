package com.ft.search;

import java.io.Serializable;

public class SearchCriteria implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SearchCriteria(String key, String operation, Object value) {
		super();
		this.key = key;
		this.operation = operation;
		this.value = value;
	}
	public String getKey() {
		return this.key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getOperation() {
		return this.operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public Object getValue() {
		return this.value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	private String key;
    private String operation;
    private Object value;
    
    public Integer numericValue(){
    	try {
    		return Integer.parseInt(this.value.toString());
    	} catch (NumberFormatException e){
    		return null;
    	}
    }
}
