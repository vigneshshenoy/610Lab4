package vkshenoy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mybeans.form.FileProperty;
import org.mybeans.form.FormBeanFactory;

import vkshenoy.common.Constants;
import vkshenoy.common.Util;
import vkshenoy.controller.tasks.VoteTask;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.Photo;
import vkshenoy.data.TimerObj;
import vkshenoy.data.Voter;
import vkshenoy.form.PhotoUploadForm;

public class UploadPhoto extends Action {

	private FormBeanFactory<PhotoUploadForm> formBeanFactory = FormBeanFactory.getInstance(PhotoUploadForm.class);

	DAOObject daoObj;

	public UploadPhoto(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	@Override
	public String getName() {
		return "upload_photo.do";
	}

	@Override
	public String perform(HttpServletRequest request,
			HttpServletResponse response) {
		HttpSession session = request.getSession();

		ArrayList<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try	{

			PhotoUploadForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			if (!form.isPresent()) {
				return "error.jsp";
			}

			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "error.jsp";
			}

			Event event = (Event)session.getAttribute("event");
			if(event == null) 	{
				errors.add("Please login using a event name and password.");
				return "error.jsp";
			}

			event = daoObj.getEventDao().getByEventName(event.getEventName());

			if(event.getState() != Constants.EVENT_STATE_ACTIVE || daoObj.getTransactionDao().hasConflictingTransaction(event.getEventName()))	{
				errors.add("This event is not active anymore.");
				return "error.jsp";
			}

			FileProperty file = form.getPhoto();
			/*if(file.getBytes().length > 1048500)	{
				errors.add("Image too big.");
				return "error.jsp";
			}*/

			if(file != null && file.getBytes() != null && file.getBytes().length > 0)	{
				Photo p = new Photo();
				p.setImageData(file.getBytes());
				p.setContentType(file.getContentType());

				boolean succ = Util.updateReplicas2Phase(Constants.OP_ADD_PHOTO, event.getEventName(), p);
				if(!succ)	{
					errors.add("Photo upload failed.");
					return "error.jsp";
				}
				event.getImages().add(p);
				p.setEvent(event);
			}	else	{
				errors.add("Photo not uploaded.");
				return "error.jsp";
			}

			List<Voter> voters = event.getVoters();

			for(Voter v : voters)	{
				v.setVoteStatus(Constants.VOTER_NOT_VOTED);
			}
			event.setVoters(voters);

			TimerObj t = event.getTimer();
			boolean newTask = false;
			if(t == null) 	{
				t = new TimerObj();
				newTask = true;
			}
			t.setDeadline(System.currentTimeMillis() + Constants.VOTE_TIMEOUT);
			t.setEvent(event);
			event.setTimer(t);

			daoObj.getEventDao().updateEvent(event);

			if(newTask && Util.myUrl.equals(event.getMaster()))	{
				Timer timer = new Timer();
				timer.schedule(new VoteTask(daoObj, event.getTimer().getTimerId(), timer), Constants.VOTE_TIMEOUT);
			}

			Voter voter = (Voter)session.getAttribute("voter");
			voter = daoObj.getVoterDao().getVoterByID(voter.getVoterId());

			session.setAttribute("event", event);
			session.setAttribute("voter", voter);
			request.setAttribute("vote_duration", ""+(Constants.VOTE_TIMEOUT / 1000));

			return "event.jsp";

		}	catch(Exception e) 	{
			e.printStackTrace();
			errors.add("Internal error occurred.");
			return "error.jsp";
		}
	}

}
