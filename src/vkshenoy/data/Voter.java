package vkshenoy.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="vkshenoy_voter")
public class Voter implements Serializable {

	@Id
	@Column(nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int voterId;
	
	@Column(nullable=false)
	private String voterName;
	
	@ManyToOne
	@JoinColumn(name="eventId")
	private Event event;
	
	@Column(columnDefinition = "int default 0")
	private int voteStatus;
	
	public Voter() {
		
	}

	public int getVoterId() {
		return voterId;
	}

	public void setVoterId(int voterId) {
		this.voterId = voterId;
	}

	public String getVoterName() {
		return voterName;
	}

	public void setVoterName(String voterName) {
		this.voterName = voterName;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public int getVoteStatus() {
		return voteStatus;
	}

	public void setVoteStatus(int voteStatus) {
		this.voteStatus = voteStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		Voter other = (Voter)obj;
		return this.voterName.equals(other.voterName);
	}
	
}
