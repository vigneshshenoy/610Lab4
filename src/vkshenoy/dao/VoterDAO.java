package vkshenoy.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import vkshenoy.data.Event;
import vkshenoy.data.Voter;

public class VoterDAO extends BaseDAO {
	
	public VoterDAO() {
		super();
	}
	
	public Voter getVoterByName(String voterName)
	{
		Voter voter = null;
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Event.class);
		criteria.add(Restrictions.like("voterName", voterName));
		List<Voter> voters = criteria.list();
		if(voters != null && voters.size() > 0)	{
			voter = voters.get(0);
			voter.getEvent().load();
		}
		commitTransaction(session);
		return voter;
	}
	
	public Voter getVoterByID(int id)	{
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Voter.class);
		criteria.add(Restrictions.eq("voterId", id));
		List <Voter> voters = criteria.list();
		Voter voter = null;
		if(voters != null && voters.size() > 0)	{
			voter = voters.get(0);
			voter.getEvent().load();
		}
		commitTransaction(session);
		return voter;
	}
	
	public void updateVoter(Voter v)	{
		Session session = beginTransaction();
		session.update(v);
		commitTransaction(session);
	}

}
