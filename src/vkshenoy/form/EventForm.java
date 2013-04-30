package vkshenoy.form;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class EventForm extends FormBean {
	
	private String eventName;
	private String eventKey;
	
	public EventForm() {
		
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if(eventName == null || eventName.trim().equals(""))	{
			errors.add("Please enter an Event Name.");
		}
		if(eventKey == null || eventKey.trim().equals(""))	{
			errors.add("Please enter an Event Key.");
		}
		return errors;
	}

}
