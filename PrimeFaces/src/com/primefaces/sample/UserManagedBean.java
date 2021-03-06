package com.primefaces.sample;

import java.util.Collection;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.primefaces.components.SocketMessageEvent;
import com.primefaces.pusher.service.PusherService;

@ManagedBean
@ApplicationScoped
public class UserManagedBean {
	UserService userService = new UserService();
	
	private String username;
	private String password;
	private String searchUser;
	private Collection<User> searchUsersResults;
	private User selectedUser;
	private PusherService pusherService = new PusherService();
	
	public String getUsername()
	{
		return username;
	}
	public void setUsername(String username)
	{
		this.username = username;
	}
	public String getPassword()
	{
		return password;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	
	public User getSelectedUser()
	{
		if(selectedUser == null){
			selectedUser = new User();
		}
		return selectedUser;
	}
	
	public void setSelectedUser(User selectedUser)
	{
		this.selectedUser = selectedUser;
	}
	public Collection<User> getSearchUsersResults()
	{
		return searchUsersResults;
	}
	public void setSearchUsersResults(Collection<User> searchUsersResults)
	{
		this.searchUsersResults = searchUsersResults;
	}
	public String getSearchUser()
	{
		return searchUser;
	}
	public void setSearchUser(String searchUser)
	{
		this.searchUser = searchUser;
	}
	
	public String login()
	{
		if("test".equalsIgnoreCase(getUsername()) && "test".equals(getPassword()))
		{
			return "home";
		}
		else
		{
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage("username", new FacesMessage("Invalid UserName and Password"));
			return "login";
		}
	}
	
	public String searchUser()
	{
		String username = (this.searchUser == null)? "":this.searchUser.trim();
		this.searchUsersResults = userService.searchUsers(username);
		return "home";
	}
	
	public String updateUser()
	{
		userService.update(this.selectedUser);
		return "home";
	}
	
	public void onUserSelect(SelectEvent event)
	{ 
		selectedUser =  (User)event.getObject();
		System.out.println("selectedUser = "+selectedUser);
	}
	public void onUserUnselect(UnselectEvent event)
	{
		selectedUser =  null;
	}
	
	public void onSocketMessage(SocketMessageEvent event) {
		try {
			JSONObject obj = new JSONObject();
			
			obj.put("message", "response to " + event.getData());
			obj.put("no-reply", true);
			
			pusherService.triggerEvent("private-testando", "sh-all", obj, event.getUuid());
		} catch (JSONException e) {}
	}
}
