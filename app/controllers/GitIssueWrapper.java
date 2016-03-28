package controllers;

import java.util.Date;

public class GitIssueWrapper {
	public String state;
	public String created_at;
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCreated() {
		return created_at;
	}
	public void setCreated(String created) {
		this.created_at = created;
	}
}
