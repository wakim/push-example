package com.primefaces.components;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

public class SocketMessageEvent extends AjaxBehaviorEvent{
	
	private static final long serialVersionUID = 32945374979480662L;
	
	private String data;
	private String uuid;
	
	public SocketMessageEvent(UIComponent component, Behavior behavior, String data) {
		super(component, behavior);
		this.data = data;
	}
	
	public SocketMessageEvent(UIComponent component, Behavior behavior, String data, String uuid) {
		this(component, behavior, data);
		this.uuid = uuid;
	}
	
	@Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return (faceslistener instanceof AjaxBehaviorListener);
	}
	
	@Override
	public void processListener(FacesListener faceslistener) {
		((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
