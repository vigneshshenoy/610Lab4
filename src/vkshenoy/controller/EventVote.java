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
import vkshenoy.form.VoteForm;

public class EventVote extends Action {

	private FormBeanFactory<VoteForm> formBeanFactory = FormBeanFactory.getInstance(VoteForm.class);

	DAOObject daoObj;

	public EventVote(DAOObject daoObj) {
		this.daoObj = daoObj;
	}
	
	@Override
	public String getName() {
		return "event_vote.do";
	}

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		
		HttpSession session = request.getSession();
		
		ArrayList<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			VoteForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
			
			if (!form.isPresent()) {
	            return "error.jsp";
	        }
	        
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "error.jsp";
	        }
	        
	        Event event = (Event)session.getAttribute("event");
	        Voter voter = (Voter)session.getAttribute("voter");
	        
	        if(event == null || voter == null)	{
	        	errors.add("Please login to an event.");
	        	return "error.jsp";
	        }
	        
	        event = daoObj.getEventDao().getByEventName(event.getEventName());
	        voter = daoObj.getVoterDao().getVoterByID(voter.getVoterId());

	        if(event.getState() != Constants.EVENT_STATE_ACTIVE)	{
	        	errors.add("This event is not active anymore.");
	        	return "error.jsp";
	        }
	        
	        if(voter.getVoteStatus() == Constants.VOTER_ACCEPTED || voter.getVoteStatus() == Constants.VOTER_REJECTED)	{
	        	errors.add("Already voted.");
	        	return "error.jsp";
	        }
	        
	        String vote = form.getVote();
	        if(vote.equals("accept"))	{
	        	voter.setVoteStatus(Constants.VOTER_ACCEPTED);
	        }	else if(vote.equals("reject"))	{
	        	voter.setVoteStatus(Constants.VOTER_REJECTED);
	        	//event.setState(Constants.EVENT_STATE_ABORTED);
	        	daoObj.getEventDao().updateEvent(event);
	        }	else	{
	        	errors.add("Invalid vote");
	        	return "error.jsp";
	        }
	        
	        
	        daoObj.getVoterDao().updateVoter(voter);
	        event = daoObj.getEventDao().getByEventName(event.getEventName());
	        
	        /*if(event.getState() != Constants.EVENT_STATE_ABORTED && isEventCommitted(event))	{
	        	event.setState(Constants.EVENT_STATE_COMMITTED);
	        }*/
	        
	        //daoObj.getEventDao().updateEvent(event);
	        
	        session.setAttribute("event", event);
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
	
	public boolean isEventCommitted(Event event)	{
		boolean committed = true;
		List<Voter> voters = event.getVoters();
		for(Voter v : voters)	{
			if(v.getVoteStatus() == Constants.VOTER_NOT_VOTED)	{
				committed = false;
				break;
			}
		}
		return committed;
	}

}
