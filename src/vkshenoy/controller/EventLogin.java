package vkshenoy.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.mybeans.form.FormBeanFactory;

import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Voter;
import vkshenoy.form.EventForm;

import com.google.gson.Gson;

public class EventLogin extends Action {
	
	private FormBeanFactory<EventForm> formBeanFactory = FormBeanFactory.getInstance(EventForm.class);

	DAOObject daoObj;

	public EventLogin(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	@Override
	public String getName() {
		return "edit_event.do";
	}

	@Override
	public String perform(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		ArrayList<String> errors = new ArrayList<String>();
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
	        
	        Event event = daoObj.getEventDao().getByEventName(eventName);
	        if(event == null)	{
	        	errors.add("Event "+eventName+" does not exist.");
	        	return "error.jsp";
	        }
	        
	        if(!event.getEventKey().equals(eventKey))	{
	        	errors.add("Incorrect Event Key.");
	        	return "error.jsp";
	        }
	        
	        String ip = request.getRemoteAddr();
	        List<Voter> voters = event.getVoters();
	        Voter voter = null;
	        for(Voter v : voters)	{
	        	if(v.getVoterName().equals(ip))	{
	        		voter = v;
	        		break;
	        	}
	        }
	        
	        if(voter == null)	{
	        	voter = new Voter();
	        	voter.setVoterName(ip);
	        	voter.setEvent(event);
	        }
	        
	        event.getVoters().add(voter);
	        
	        daoObj.getEventDao().updateEvent(event);
	        session.setAttribute("event", event);
	        session.setAttribute("voter", voter);
	        
	        if(event.getTimer() != null)	{
	        	int dur = (int)(event.getTimer().getDeadline() - System.currentTimeMillis())/1000;
	        	request.setAttribute("vote_duration", ""+dur);
	        }
	        
	        return "event.jsp";
			
		}	catch (Exception e) {
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}
}
