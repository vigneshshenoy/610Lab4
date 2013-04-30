package vkshenoy.form;

import java.util.ArrayList;
import java.util.List;

import org.mybeans.form.FormBean;

public class VoteForm extends FormBean {
	
	private String vote;

	public String getVote() {
		return vote;
	}

	public void setVote(String vote) {
		this.vote = vote;
	}
	
	@Override
	public List<String> getValidationErrors() {
		ArrayList<String> errors = new ArrayList<String>();
		if(vote == null || vote.trim().equals(""))	{
			errors.add("Invalid Vote.");
		}
		return errors;
	}

}
