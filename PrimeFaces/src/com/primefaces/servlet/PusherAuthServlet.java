package com.primefaces.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.json.JSONException;
import org.primefaces.json.JSONObject;

import com.primefaces.pusher.auth.HMACDigest;

@WebServlet(name="PusherAuthenticationServlet", urlPatterns={"/pusher/auth"})
public class PusherAuthServlet extends HttpServlet {
	private static final long serialVersionUID = -1975754668041256508L;
	
	private static final String PUSHER_SECRET = "3c569e4428f95086de5b";
	private static final String PUSHER_APP_KEY = "74c8e3a5bc3f420957e2";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Os parametros passados sao socket_id, channel.
		
		String channel = req.getParameter("channel_name");
		String socketId = req.getParameter("socket_id");
		
		if(channel == null || socketId == null) {
			resp.setStatus(401); // Unauthorized
			return;
		}
		
		if(validate(channel, socketId)) {
			JSONObject response = new JSONObject();
			
			try {
				response.put("auth", PUSHER_APP_KEY.concat(":").concat(HMACDigest.HMACDigestSHA256(socketId.concat(":").concat(channel), PUSHER_SECRET)));
			} catch (JSONException e) {
				resp.setStatus(500);
				return;
			}
			
			resp.getWriter().write(response.toString());
		} else {
			resp.setStatus(401);
		}
	}
	
	// A principio o channel deve ter alguma rela��o com algum recurso do usu�rio (Projeto, Sprint, Conta, etc...)
	// E poderia ser validado se os dados da sessao do usu�rio bate com o channel passado.
	protected boolean validate(String channel, String socketId) {
		return true;
	}
}
