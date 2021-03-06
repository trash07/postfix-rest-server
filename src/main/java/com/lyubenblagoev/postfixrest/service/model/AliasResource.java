package com.lyubenblagoev.postfixrest.service.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lyubenblagoev.postfixrest.entity.Alias;

public class AliasResource {

	private Long id;
	
	private String name;
	
	private String email;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
	private Date created;
	
	@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ssZ")
	private Date updated;
	
	private boolean enabled;
	
	public static AliasResource fromAlias(Alias alias) {
		AliasResource resource = new AliasResource();
		resource.setId(alias.getId());
		resource.setName(alias.getAlias());
		resource.setEmail(alias.getEmail());
		resource.setEnabled(alias.isEnabled());
		resource.setCreated(alias.getCreated());
		resource.setUpdated(alias.getUpdated());
		return resource;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
