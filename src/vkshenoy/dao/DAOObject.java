package vkshenoy.dao;

public class DAOObject {
	
	private PhotoDAO photoDao;
	private EventDAO eventDao;
	private VoterDAO voterDao;
	private TimerDAO timerDao;
	private TransactionDAO transactionDao;
	
	public DAOObject()	{
		photoDao = new PhotoDAO();
		eventDao = new EventDAO();
		voterDao = new VoterDAO();
		timerDao = new TimerDAO();
		transactionDao = new TransactionDAO();
	}

	public PhotoDAO getPhotoDao() {
		return photoDao;
	}

	public void setPhotoDao(PhotoDAO photoDao) {
		this.photoDao = photoDao;
	}

	public EventDAO getEventDao() {
		return eventDao;
	}

	public void setEventDao(EventDAO eventDao) {
		this.eventDao = eventDao;
	}

	public VoterDAO getVoterDao() {
		return voterDao;
	}

	public void setVoterDao(VoterDAO voterDao) {
		this.voterDao = voterDao;
	}

	public TimerDAO getTimerDao() {
		return timerDao;
	}

	public void setTimerDao(TimerDAO timerDao) {
		this.timerDao = timerDao;
	}

	public TransactionDAO getTransactionDao() {
		return transactionDao;
	}

	public void setTransactionDao(TransactionDAO transactionDao) {
		this.transactionDao = transactionDao;
	}

}
