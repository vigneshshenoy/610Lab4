package vkshenoy.form;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FileProperty;
import org.mybeans.form.FormBean;

public class PhotoUploadForm extends FormBean {
	
	private FileProperty photo;
	
	public PhotoUploadForm() {
		
	}
	
	public FileProperty getPhoto() {
		return photo;
	}

	public void setPhoto(FileProperty photo) {
		this.photo = photo;
	}

	public List<String> getValidationErrors() {
		List<String> errors = new ArrayList<String>();
		if(photo == null)	{
			errors.add("Please select a photo.");
		}
		return errors;
	}

}
