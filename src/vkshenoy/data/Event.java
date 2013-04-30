package vkshenoy.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="vkshenoy_event")
public class Event implements Serializable {
	
	@Id
	@Column(nullable=false)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int eventId;
	
	@Column(nullable=false)
	private String eventName;
	
	@Column(nullable=false)
	private String eventKey;
	
	@OneToMany(targetEntity=Photo.class, mappedBy="event", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Photo> images;
	
	@OneToMany(targetEntity=Voter.class, mappedBy="event", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Voter> voters;
	
	@Column(columnDefinition = "int default 0")
	private int state;
	
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="timerId")
	private TimerObj timer;
	
	private String master;
	
	public Event() {
		images = new ArrayList<Photo>();
		voters = new ArrayList<Voter>();
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
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

	public List<Photo> getImages() {
		return images;
	}

	public void setImages(List<Photo> images) {
		this.images = images;
	}

	public List<Voter> getVoters() {
		return voters;
	}

	public void setVoters(List<Voter> voters) {
		this.voters = voters;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public TimerObj getTimer() {
		return timer;
	}

	public void setTimer(TimerObj timer) {
		this.timer = timer;
	}
	
	public void load()
	{
		this.images.size();
		this.voters.size();
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}
	
}
