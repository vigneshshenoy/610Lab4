package vkshenoy.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import vkshenoy.data.Event;

public class EventDAO extends BaseDAO {

	public EventDAO() {
		super();
	}
	
	public void createEvent(Event event)
	{
		Session session = beginTransaction();
		session.save(event);
		commitTransaction(session);
	}
	
	public void updateEvent(Event event)
	{
		Session session = beginTransaction();
		session.update(event);
		commitTransaction(session);
	}
	
	public Event getByEventName(String eventName) 
	{
		Event event = null;
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Event.class);
		criteria.add(Restrictions.like("eventName", eventName));
		List<Event> events = criteria.list();
		if(events != null && events.size() > 0)	{
			event = events.get(0);
			event.load();
		}
		commitTransaction(session);
		return event;
	}
	
}
