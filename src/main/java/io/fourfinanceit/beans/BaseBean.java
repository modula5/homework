package io.fourfinanceit.beans;

import java.time.LocalDateTime;

public class BaseBean {

	private LocalDateTime created;
	
	private LocalDateTime updated;

	public LocalDateTime getCreated() {
		return created;
	}

	public void setCreated(LocalDateTime created) {
		this.created = created;
	}

	public LocalDateTime getUpdated() {
		return updated;
	}

	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	
}
