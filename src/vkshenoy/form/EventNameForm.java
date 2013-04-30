package vkshenoy.form;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class EventNameForm extends FormBean {
	
	private String eventName;
	
	public EventNameForm() {
		
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	
	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if(eventName == null || eventName.trim().equals(""))	{
			errors.add("Event Name is empty.");
		}
		return errors;
	}

}
