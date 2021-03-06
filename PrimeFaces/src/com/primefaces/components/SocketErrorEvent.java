package com.primefaces.components;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

public class SocketErrorEvent extends AjaxBehaviorEvent{
	
	private static final long serialVersionUID = 32945374979480662L;
	
	public SocketErrorEvent(UIComponent component, Behavior behavior) {
		super(component, behavior);
	}
	
	@Override
	public boolean isAppropriateListener(FacesListener faceslistener) {
		return (faceslistener instanceof AjaxBehaviorListener);
	}
	
	@Override
	public void processListener(FacesListener faceslistener) {
		((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
	}
}
