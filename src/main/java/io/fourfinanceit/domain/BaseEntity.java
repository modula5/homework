package io.fourfinanceit.domain;

import static java.time.LocalDateTime.now;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;
	
	@Column(name = "created")
	private LocalDateTime created = now();
	
	@Column(name = "updated")
	private LocalDateTime updated;
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getId() {
		return id;
	}
	
	public LocalDateTime getCreated() {
		return created;
	}
	
	public void setUpdated(LocalDateTime updated) {
		this.updated = updated;
	}
	
	public LocalDateTime getUpdated() {
		return updated;
	}
	
}
