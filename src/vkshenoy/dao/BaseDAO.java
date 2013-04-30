package vkshenoy.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import vkshenoy.data.Event;
import vkshenoy.data.Photo;
import vkshenoy.data.TimerObj;
import vkshenoy.data.Voter;

public class BaseDAO {
	
	private AnnotationConfiguration config;
	private SessionFactory sessionFactory;
	
	public BaseDAO()	{
		config = new AnnotationConfiguration();
		config.addAnnotatedClass(Event.class);
		config.addAnnotatedClass(Photo.class);
		config.addAnnotatedClass(Voter.class);
		config.addAnnotatedClass(TimerObj.class);
		config.addAnnotatedClass(vkshenoy.data.Transaction.class);
		
		config.configure("hibernate.cfg.xml");
		config.setProperty("hibernate.connection.url", System.getenv().get("dburl"));
		//new SchemaExport(config).create(true, true);
		new SchemaUpdate(config).execute(true, true);
		sessionFactory = config.buildSessionFactory();
	}
	
	protected Session beginTransaction()	{
		Session session = sessionFactory.getCurrentSession();
		session.beginTransaction();
		return session;
	}
	
	protected void commitTransaction(Session session)	{
		if(session != null)	{
			try {
				session.getTransaction().commit();
				if(session.isConnected())	{
					session.disconnect();
				}
			} catch (Exception e) {
				if(session != null)	{
					Transaction tx = session.getTransaction();
					tx.rollback();
					if(session.isConnected())	{
						session.disconnect();
					}
				}
			}
		}
	}
	
	protected void rollbackTransaction(Session session)	{
		if(session != null)	{
			Transaction tx = session.getTransaction();
			tx.rollback();
		}
	}

}
