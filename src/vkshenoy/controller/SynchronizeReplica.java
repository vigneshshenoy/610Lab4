package vkshenoy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.mybeans.form.FormBeanFactory;

import vkshenoy.common.Constants;
import vkshenoy.common.Util;
import vkshenoy.controller.tasks.VoteTask;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Photo;
import vkshenoy.data.TimerObj;
import vkshenoy.data.Transaction;
import vkshenoy.data.Voter;
import vkshenoy.form.SyncForm;

public class SynchronizeReplica extends Action {

	private FormBeanFactory<SyncForm> formBeanFactory = FormBeanFactory.getInstance(SyncForm.class);

	DAOObject daoObj;

	public SynchronizeReplica(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	@Override
	public String getName() {
		return "sync.do";
	}

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();

		ArrayList<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			SyncForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			if (!form.isPresent()) {
				return "error.jsp";
			}
			
			String op = form.getOperation();
			Transaction t = new Transaction();
			t.setTid(form.getTidInt());
			t.setEventName(form.getEventName());
			t.setOperation(op);
			
			if(form.getData().equals(""))	{
				t.setTransactionData(null);
			}	else	{
				t.setTransactionData(Base64.decodeBase64(form.getData()));
			}
			
			if(op.equals(Constants.OP_CREATE_EVENT))	{
				String result = createEvent(t);
				Util.respond(response, result);
			}	else if(op.equals(Constants.OP_ADD_PHOTO))	{
				String result = addPhoto(t);
				Util.respond(response, result);
			}	else if(op.equals(Constants.OP_GET_VOTES))	{
				String result = getVotes(t);
				Util.respond(response, result);
			}	else if(op.equals(Constants.COMMIT))	{
				commitTransaction(t.getTid());
			}	else if(op.equals(Constants.ABORT))		{
				abortTransaction(t.getTid());
			}	else if(op.equals(Constants.OP_CANCEL_EVENT) || op.equals(Constants.OP_PUBLISH_EVENT))	{
				String result = changeEventState(t);
				Util.respond(response, result);
			}	else	{
				//error
				Util.respond(response, Constants.FAILURE);
			}
			
		}	catch(Exception e)	{
			e.printStackTrace();
		}
		return "";
	}
	
	private String createEvent(Transaction t)
	{
		if(Util.canEventBeCreated(t.getEventName()))	{
			daoObj.getTransactionDao().createTransaction(t);
			return Constants.SUCCESS;
		}
		return Constants.FAILURE;
	}
	
	private String addPhoto(Transaction t)
	{
		if(daoObj.getEventDao().getByEventName(t.getEventName()) != null)	{
			daoObj.getTransactionDao().createTransaction(t);
			return Constants.SUCCESS;
		}
		return Constants.FAILURE;
	}
	
	private void commitTransaction(long tid)
	{
		Transaction t = daoObj.getTransactionDao().getTransactionByID(tid);
		if(t == null)	{
			return;
		}
		String op = t.getOperation();
		if(Constants.OP_CREATE_EVENT.equals(op))	{
			
			Event event = (Event)Util.deserialize(Base64.encodeBase64String(t.getTransactionData()));
			daoObj.getEventDao().createEvent(event);
			
		}	else if(Constants.OP_ADD_PHOTO.equals(op))	{
			
			Photo photo = (Photo)Util.deserialize(Base64.encodeBase64String(t.getTransactionData()));
			Event event = daoObj.getEventDao().getByEventName(t.getEventName());
			photo.setEvent(event);
			event.getImages().add(photo);
			
			List<Voter> voters = event.getVoters();
			for(Voter v : voters)	{
				v.setVoteStatus(Constants.VOTER_NOT_VOTED);
			}
			event.setVoters(voters);
			
			TimerObj tim = event.getTimer();
			boolean newTask = false;
			if(tim == null) 	{
				tim = new TimerObj();
				newTask = true;
			}
			tim.setDeadline(System.currentTimeMillis() + Constants.VOTE_TIMEOUT);
			tim.setEvent(event);
			event.setTimer(tim);
			
			daoObj.getEventDao().updateEvent(event);
			
			if(newTask && Util.myUrl.equals(event.getMaster()))	{
				Timer timer = new Timer();
				timer.schedule(new VoteTask(daoObj, event.getTimer().getTimerId(), timer), Constants.VOTE_TIMEOUT);
			}
			
		}	else if(Constants.OP_CANCEL_EVENT.equals(op))	{
			
			Event event = daoObj.getEventDao().getByEventName(t.getEventName());
			event.setState(Constants.EVENT_STATE_ABORTED);
			daoObj.getEventDao().updateEvent(event);
			
		}	else if(Constants.OP_PUBLISH_EVENT.equals(op))	{
			
			Event event = daoObj.getEventDao().getByEventName(t.getEventName());
			event.setState(Constants.EVENT_STATE_COMMITTED);
			daoObj.getEventDao().updateEvent(event);
			
		}	else	{
			//error
		}
		daoObj.getTransactionDao().deleteTransaction(t);
	}
	
	private void abortTransaction(long tid)
	{
		Transaction t = daoObj.getTransactionDao().getTransactionByID(tid);
		if(t != null)	{
			daoObj.getTransactionDao().deleteTransaction(t);
		}
	}
	
	private String changeEventState(Transaction t)
	{
		daoObj.getTransactionDao().createTransaction(t);
		return Constants.SUCCESS;
	}

	private String getVotes(Transaction t)
	{
		String result = Constants.SUCCESS;
		Event event = daoObj.getEventDao().getByEventName(t.getEventName());
		List<Voter> voters = event.getVoters();
		if(voters != null)	{
			for(Voter v : voters)	{
				if(v.getVoteStatus() == Constants.VOTER_NOT_VOTED || v.getVoteStatus() == Constants.VOTER_REJECTED)	{
					result = Constants.FAILURE;
					break;
				}
			}
		}
		return result;
	}
	
}
