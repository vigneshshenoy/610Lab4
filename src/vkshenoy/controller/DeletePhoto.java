package vkshenoy.controller;

import java.util.ArrayList;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FormBeanFactory;

import vkshenoy.common.Constants;
import vkshenoy.controller.tasks.VoteTask;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Photo;
import vkshenoy.data.TimerObj;
import vkshenoy.form.IdForm;

public class DeletePhoto extends Action {

	private FormBeanFactory<IdForm> formBeanFactory = FormBeanFactory.getInstance(IdForm.class);

	DAOObject daoObj;
	
	public DeletePhoto(DAOObject daoObj) {
		this.daoObj = daoObj;
	}
	
	@Override
	public String getName() {
		return "delete_photo.do";
	}

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();

		ArrayList<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			IdForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			if (!form.isPresent()) {
	            return "error.jsp";
	        }
	        
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "error.jsp";
	        }
	        
	        int photoId = form.getIdAsInt();
	        
	        Event event = (Event)session.getAttribute("event");
	        if(event == null) 	{
	        	errors.add("Not logged in to any event");
	        	return "error.jsp";
	        }
	        
	        if(event.getState() != Constants.EVENT_STATE_ACTIVE)	{
	        	errors.add("This event is not active anymore.");
	        	return "error.jsp";
	        }
	        
	        Photo p = daoObj.getPhotoDao().getPhotoByID(photoId);
	        
	        event = daoObj.getEventDao().getByEventName(event.getEventName());
	        
	        event.getImages().remove(p);
	        
	        daoObj.getEventDao().updateEvent(event);
	        
	        TimerObj t = event.getTimer();
			boolean newTask = false;
			if(t == null) 	{
				throw new Exception("Timer not set");
			}
			t.setDeadline(System.currentTimeMillis() + Constants.VOTE_TIMEOUT);
			t.setEvent(event);
			event.setTimer(t);
			
			daoObj.getEventDao().updateEvent(event);
			
			if(newTask)	{
				Timer timer = new Timer();
				timer.schedule(new VoteTask(daoObj, t.getTimerId(), timer), Constants.VOTE_TIMEOUT);
			}
	        
	        session.setAttribute("event", event);
	        
	        return "event.jsp";
			
		}	catch (Exception e) {
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}

}
