package vkshenoy.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import vkshenoy.data.Photo;
import vkshenoy.data.TimerObj;

public class TimerDAO extends BaseDAO {
	
	public TimerDAO() {
		super();
	}
	
	public TimerObj getTimerByID(int timerId)
	{
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(TimerObj.class);
		criteria.add(Restrictions.eq("timerId", timerId));
		List <TimerObj> timers = criteria.list();
		TimerObj timer = null;
		if(timers != null && timers.size() > 0)	{
			timer = timers.get(0);
			timer.getEvent().load();
		}
		commitTransaction(session);
		return timer;
	}

}
