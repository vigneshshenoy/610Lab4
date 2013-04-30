package vkshenoy.common;

public class Constants {
	
	public static final int EVENT_STATE_ACTIVE = 0;
	public static final int EVENT_STATE_COMMITTED = 1;
	public static final int EVENT_STATE_ABORTED = 2;
	
	public static final int VOTER_NOT_VOTED = 0;
	public static final int VOTER_ACCEPTED = 1;
	public static final int VOTER_REJECTED = 2;
	
	public static final String COOKIE_NAME = "15610Lab4_Event_User";
	
	public static final long VOTE_TIMEOUT = 2 * 60 * 1000; //2 minutes
	
	public static final String OPERATION = "operation";
	public static final String OP_CREATE_EVENT = "CREATE_EVENT";
	public static final String OP_ADD_PHOTO = "ADD_PHOTO";
	public static final String OP_CANCEL_EVENT = "CANCEL_EVENT";
	public static final String OP_PUBLISH_EVENT = "PUBLISH_EVENT";
	public static final String TRANSACTION_ID = "tid";
	public static final String EVENT_ID = "EVENT_ID";
	public static final String OP_GET_VOTES = "GET_VOTES";
	public static final String DATA = "data";
	public static final String EVENT_NAME = "eventName";
	
	public static final String SUCCESS = "SUCCESS";
	public static final String FAILURE = "FAILURE";
	
	public static final String COMMIT = "COMMIT";
	public static final String ABORT = "ABORT";
}
