package com.primefaces.pusher.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.primefaces.json.JSONObject;

import com.pusher.Pusher;

public class PusherService {
	public void triggerEvent(String channelName, String eventName, JSONObject data) throws IllegalStateException {
		triggerEvent(channelName, eventName, data, null);
	}
	
	public void triggerEvent(String channelName, String eventName, JSONObject data, String socketId) throws IllegalStateException {
		HttpResponse res = Pusher.triggerPush(channelName, eventName, data.toString(), socketId);
		
		if(res.getStatusLine().getStatusCode() != 200) {
			try {
				throw new IllegalStateException(res.toString() + "\nBody: " + EntityUtils.toString(res.getEntity()));
			} catch (ParseException e) {
				throw new IllegalStateException(res.toString());
			} catch (IOException e) {
				throw new IllegalStateException(res.toString());
			}
		}
	}
}
