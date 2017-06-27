package com.ideas2it.chatbot.model;

import java.util.List;

public class Project {

	private int id;

	private String name;

	private String projectDescription;

	private String clientDetail;

	private String status;

	private List<String> tags;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getClientDetail() {
		return clientDetail;
	}

	public void setClientDetail(String clientDetail) {
		this.clientDetail = clientDetail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
}
