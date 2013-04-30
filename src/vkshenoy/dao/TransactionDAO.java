package vkshenoy.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import vkshenoy.common.Constants;
import vkshenoy.data.Event;
import vkshenoy.data.TimerObj;
import vkshenoy.data.Transaction;

public class TransactionDAO extends BaseDAO {
	
	public TransactionDAO() {
		super();
	}
	
	public void createTransaction(Transaction t)	{
		Session session = beginTransaction();
		session.save(t);
		commitTransaction(session);
	}
	
	public void deleteTransactions()
	{
		Session session = beginTransaction();
		session.createSQLQuery("DELETE FROM vkshenoy_transaction").executeUpdate();
		commitTransaction(session);
	}
	
	public void deleteTransaction(Transaction t)
	{
		Session session = beginTransaction();
		session.delete(t);
		commitTransaction(session);
	}
	
	public Transaction getTransactionByID(long tid)
	{
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Transaction.class);
		criteria.add(Restrictions.eq("tid", tid));
		List <Transaction> transactions = criteria.list();
		Transaction t = null;
		if(transactions != null && transactions.size() > 0)	{
			t = transactions.get(0);
		}
		commitTransaction(session);
		return t;
	}
	
	public boolean hasConflictingTransaction(String eventName)
	{
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Transaction.class);
		criteria.add(Restrictions.like("eventName", eventName));
		criteria.add(Restrictions.or(Restrictions.like("operation", Constants.OP_CANCEL_EVENT), Restrictions.like("operation", Constants.OP_PUBLISH_EVENT)));
		List <Transaction> transactions = criteria.list();
		Transaction t = null;
		if(transactions != null && transactions.size() > 0)	{
			return true;
		}
		commitTransaction(session);
		return false;
	}
	
	public boolean isTransactionPresent(String eventName) 
	{
		boolean present = false;
		Session session = beginTransaction();
		Criteria criteria = session.createCriteria(Transaction.class);
		criteria.add(Restrictions.like("eventName", eventName));
		List<Transaction> transactions = criteria.list();
		if(transactions != null && transactions.size() > 0)	{
			present = true;
		}
		commitTransaction(session);
		return present;
	}

}
