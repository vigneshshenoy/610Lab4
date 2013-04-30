package vkshenoy.controller.tasks;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import vkshenoy.common.Constants;
import vkshenoy.common.Util;
import vkshenoy.dao.DAOObject;
import vkshenoy.data.Event;
import vkshenoy.data.TimerObj;
import vkshenoy.data.Voter;

public class VoteTask extends TimerTask {
	
	private DAOObject daoObj;
	private int timerId;
	private Timer timer;
	
	public VoteTask(DAOObject daoObj, int timerId, Timer timer) {
		this.daoObj = daoObj;
		this.timerId = timerId;
		this.timer = timer;
	}

	@Override
	public void run() {
		TimerObj t = daoObj.getTimerDao().getTimerByID(timerId);
		long currTime = System.currentTimeMillis();
		if(currTime >= t.getDeadline())	{
			//check if everyone has voted
			
			boolean res = Util.updateReplicas2Phase(Constants.OP_GET_VOTES, t.getEvent().getEventName(), null);
			
			int status = Constants.VOTER_ACCEPTED;
			if(res)	{
				List<Voter> voters = t.getEvent().getVoters();
				for(Voter v : voters)	{
					if(v.getVoteStatus() == Constants.VOTER_NOT_VOTED || v.getVoteStatus() == Constants.VOTER_REJECTED)	{
						status = v.getVoteStatus();
						break;
					}
				}
			}	else	{
				status = Constants.VOTER_REJECTED;
			}
			
			Event event = t.getEvent();
			event = daoObj.getEventDao().getByEventName(event.getEventName());
			
			String op = "";
			if(status != Constants.VOTER_ACCEPTED)	{
				event.setState(Constants.EVENT_STATE_ABORTED);
				op = Constants.OP_CANCEL_EVENT;
			}	else	{
				event.setState(Constants.EVENT_STATE_COMMITTED);
				op = Constants.OP_PUBLISH_EVENT;
			}
			
			res = Util.updateReplicas2Phase(op, event.getEventName(), null);
			
			if(!res)	{
				event.setState(Constants.EVENT_STATE_ABORTED);
			}
			daoObj.getEventDao().updateEvent(event);
			timer.cancel();
		}	else	{
			timer.cancel();
			timer = new Timer();
			System.out.println("Timer reset for "+((t.getDeadline() - currTime)/1000)+" sec.");
			timer.schedule(new VoteTask(daoObj, timerId, timer), (t.getDeadline() - currTime));
		}
	}

}
