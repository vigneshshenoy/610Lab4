package vkshenoy.form;

import org.mybeans.form.FormBean;

public class SyncForm extends FormBean {
	
	private String tid;
	private String eventName;
	private String operation;
	private String data;
	
	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getOperation() {
		return operation;
	}
	
	public void setOperation(String operation) {
		this.operation = operation;
	}
	
	public String getData() {
		return data;
	}
	
	public void setData(String data) {
		this.data = data;
	}
	
	public long getTidInt()
	{
		return Long.parseLong(tid);
	}

}
