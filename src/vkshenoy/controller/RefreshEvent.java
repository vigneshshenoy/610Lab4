package vkshenoy.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanFactory;

import vkshenoy.common.Constants;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Voter;
import vkshenoy.form.EventForm;
import vkshenoy.form.EventNameForm;

public class RefreshEvent extends Action {
	
	private FormBeanFactory<EventNameForm> formBeanFactory = FormBeanFactory.getInstance(EventNameForm.class);

	DAOObject daoObj;

	public RefreshEvent(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	@Override
	public String getName() {
		return "refresh_event.do";
	}

	@Override
	public String perform(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();

		ArrayList<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			EventNameForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			if (!form.isPresent()) {
	            return "error.jsp";
	        }
	        
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "error.jsp";
	        }
	        
	        String eventName = form.getEventName();
	        
	        Event event = daoObj.getEventDao().getByEventName(eventName);
	        if(event == null)	{
	        	errors.add("Event "+eventName+" does not exist.");
	        	return "error.jsp";
	        }
	        
	        session.setAttribute("event", event);
	        
	        String ip = request.getRemoteAddr();
	        List<Voter> voters = event.getVoters();
	        Voter voter = null;
	        for(Voter v : voters)	{
	        	if(v.getVoterName().equals(ip))	{
	        		voter = v;
	        		break;
	        	}
	        }
	        
	        session.setAttribute("voter", voter);
	        
	        if(event.getState() == Constants.EVENT_STATE_ACTIVE)	{
	        	if(event.getTimer() != null)	{
	        		int dur = (int)(event.getTimer().getDeadline() - System.currentTimeMillis())/1000;
	        		request.setAttribute("vote_duration", ""+dur);
	        	}
	        }
	        
	        return "event.jsp";
			
		}	catch (Exception e) {
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}
}
