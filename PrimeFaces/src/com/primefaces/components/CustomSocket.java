package com.primefaces.components;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.FacesEvent;

import org.primefaces.util.Constants;

@FacesComponent(CustomSocket.COMPONENT_TYPE)
public class CustomSocket extends UIComponentBase implements ClientBehaviorHolder {
	
	public static final String TAG_NAME = "socket";
	public static final String COMPONENT_TYPE = "shSocket";
	public static final String COMPONENT_FAMILY = "socket";
	
	private static final Collection<String> EVENT_NAMES = Collections.unmodifiableCollection(Arrays.asList("message", "error"));
	
	public enum PropertyKeys {
		channel,
		autoConnect,
		event,
		widgetVar,
		onMessage,
		onError
	}
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public Collection<String> getEventNames() {
		return EVENT_NAMES;
	}
	
	public String getChannel(){
		return (String) getStateHelper().eval(PropertyKeys.channel);
	}
	
	public void setChannel(String channel){
		getStateHelper().put(PropertyKeys.channel, channel);
	}
	
	public Boolean getAutoConnect(){
		return (Boolean) getStateHelper().eval(PropertyKeys.autoConnect);
	}
	
	public void setAutoConnect(Boolean autoConnect){
		getStateHelper().put(PropertyKeys.autoConnect, autoConnect);
	}
	
	public String getWidgetVar(){
		return (String) getStateHelper().eval(PropertyKeys.widgetVar);
	}
	
	public void setWidgetVar(String widgetVar){
		getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
	}
	
	public String getOnMessage(){
		return (String) getStateHelper().eval(PropertyKeys.onMessage);
	}
	
	public void setOnMessage(String onMessage){
		getStateHelper().put(PropertyKeys.onMessage, onMessage);
	}
	
	public String getOnError(){
		return (String) getStateHelper().eval(PropertyKeys.onError);
	}
	
	public void setOnError(String onError){
		getStateHelper().put(PropertyKeys.onError, onError);
	}
	
	public String getEvent(){
		return (String) getStateHelper().eval(PropertyKeys.event);
	}
	
	public void setEvent(String event){
		getStateHelper().put(PropertyKeys.event, event);
	}
	
	@Override
	public void queueEvent(FacesEvent event) {
		FacesContext context = getFacesContext();
		
		if(isRequestSource(context) && event instanceof AjaxBehaviorEvent) {
			Map<String,String> params = context.getExternalContext().getRequestParameterMap();
			
			String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
			String clientId = this.getClientId(context);
			
			AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;
			
			AjaxBehaviorEvent customEvent = null;
			
			if(EVENT_NAMES.contains(eventName)) {
				if(eventName.equals("message")) {
					customEvent = new SocketMessageEvent(this, behaviorEvent.getBehavior(), params.get(clientId + "_data"), params.get(clientId + "_uuid"));
					customEvent.setPhaseId(behaviorEvent.getPhaseId());
				} else if(eventName.equals("error")) {
					customEvent = new SocketErrorEvent(this, behaviorEvent.getBehavior());
					customEvent.setPhaseId(behaviorEvent.getPhaseId());
				}
				
				super.queueEvent(customEvent);
			}
		} else {
			super.queueEvent(event);
		}
	}

	private boolean isRequestSource(FacesContext context) {
		return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.RequestParams.PARTIAL_SOURCE_PARAM));
	}
}