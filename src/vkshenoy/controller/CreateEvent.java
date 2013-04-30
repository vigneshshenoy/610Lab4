package vkshenoy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.mybeans.form.FormBeanFactory;

import vkshenoy.common.Constants;
import vkshenoy.common.Util;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Voter;
import vkshenoy.form.EventForm;

public class CreateEvent extends Action {
	
	private FormBeanFactory<EventForm> formBeanFactory = FormBeanFactory.getInstance(EventForm.class);

	DAOObject daoObj;

	public CreateEvent(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	@Override
	public String getName() {
		return "create_event.do";
	}

	@Override
	public String perform(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			EventForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			if (!form.isPresent()) {
	            return "error.jsp";
	        }
	        
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "error.jsp";
	        }
	        
	        String eventName = form.getEventName();
	        String eventKey = form.getEventKey();
	        
	        if(daoObj.getEventDao().getByEventName(eventName) != null || daoObj.getTransactionDao().isTransactionPresent(eventName))	{
	        	errors.add("Event "+eventName+" already present. Please pick a new name");
	        	return "error.jsp";
	        }
	        
	        Event event = new Event();
	        event.setEventName(eventName);
	        event.setEventKey(eventKey);
	        event.setMaster(Util.myUrl);
	        
	        boolean succ = Util.updateReplicas2Phase(Constants.OP_CREATE_EVENT, event.getEventName(), event);
	        
	        //Save event to database
	        if(succ)	{
	        	String ip = request.getRemoteAddr();
		        
		        Voter v = new Voter();
		        v.setVoterName(ip);
		        v.setEvent(event);
		        
		        event.getVoters().add(v);
	        	daoObj.getEventDao().createEvent(event);
	        	session.setAttribute("event", event);
		        session.setAttribute("voter", v);
		        //session.setAttribute("vote_duration", null);
		        
		        return "event.jsp";
	        }	else	{
	        	errors.add("Could not create event.");
	        	return "error.jsp";
	        }
			
		}	catch (Exception e) {
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}
}
