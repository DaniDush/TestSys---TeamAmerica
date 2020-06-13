package il.cshaifasweng.HSTS.entities;

import java.io.Serializable;
import java.time.Duration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class AddTimeRequest implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int requestId;		//primary key
	
	@Column
	private int examination_id;
	
	@Column (name = "request_name")
	private String requestReason;
	
	@Column (name = "request_duration")
	private Duration requestedDuration;
	
	@Column
	private boolean reviewed;
	
	@Column
	private boolean approved;
	
	public AddTimeRequest(String requestReason, Duration requestedDuration) {
			this.requestedDuration = requestedDuration;
			this.requestReason = requestReason;
			reviewed = false;
			approved = false;
	}
	
	public AddTimeRequest() {
		
	}

	public String getRequestReason() {
		return requestReason;
	}

	public void setRequestReason(String requestReason) {
		this.requestReason = requestReason;
	}

	public Duration getRequestedDuration() {
		return requestedDuration;
	}

	public void setRequestedDuration(Duration requestedDuration) {
		this.requestedDuration = requestedDuration;
	}

	public boolean isReviewed() {
		return reviewed;
	}

	public void setReviewed(boolean reviewed) {
		this.reviewed = reviewed;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}
	
	
}
