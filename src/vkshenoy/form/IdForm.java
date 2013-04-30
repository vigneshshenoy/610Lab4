package vkshenoy.form;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class IdForm extends FormBean {
	
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getIdAsInt()	{
		int id = -1;
		
		try	{
			id = Integer.parseInt(this.id);
		}	catch (Exception e) {
			id = -1;
		}
		
		return id;
	}
	
	@Override
	public List<String> getValidationErrors() {
		ArrayList<String> errors = new ArrayList<String>();
		if(id == null || id.trim().equals(""))	{
			errors.add("Invalid ID");
		}
		return errors;
	}

}
