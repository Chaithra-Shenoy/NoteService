/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>POJO Class having Note related information and method.</b>
 *        </p>
 */
@Document(indexName = "test", type = "note")
public class Note {

	@Id
	private String noteId;
	private String title;
	private String description;
	@ApiModelProperty(hidden = true)
	private String createdAt;
	@ApiModelProperty(hidden = true)
	private String updatedAt;
	private String user;
	@ApiModelProperty(hidden = true)
	private boolean trashStatus = false;
	@ApiModelProperty(hidden = true)
	private boolean pinStatus = false;
	private boolean isArchieve = false;
	// @DBRef(db="label")
	private List<Label> label;
	private List<MetaData> data;

	/**
	 * @param noteId
	 * @param title
	 * @param description
	 * @param createdAt
	 * @param updatedAt
	 * @param user
	 * @param trashStatus
	 * @param pinStatus
	 * @param isArchieve
	 * @param labels
	 */
	public Note(String noteId, String title, String description, String createdAt, String updatedAt, String user,
			boolean trashStatus, boolean pinStatus, boolean isArchieve, List<Label> labels) {
		super();
		this.noteId = noteId;
		this.title = title;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.user = user;
		this.trashStatus = trashStatus;
		this.pinStatus = pinStatus;
		this.isArchieve = isArchieve;
		this.label = labels;
	}

	/**
	 * 
	 */
	public Note() {
	}

	/**
	 * @return the noteId
	 */
	public String getNoteId() {
		return noteId;
	}

	/**
	 * @param noteId
	 *            the noteId to set
	 */
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the createdAt
	 */
	public String getCreatedAt() {
		return createdAt;
	}

	/**
	 * @param createdAt
	 *            the createdAt to set
	 */
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * @return the updatedAt
	 */
	public String getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @param updatedAt
	 *            the updatedAt to set
	 */
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * @return the userId
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the trashStatus
	 */
	public boolean isTrashStatus() {
		return trashStatus;
	}

	/**
	 * @param trashStatus
	 *            the trashStatus to set
	 */
	public void setTrashStatus(boolean trashStatus) {
		this.trashStatus = trashStatus;
	}

	/**
	 * @return the pinStatus
	 */
	public boolean isPinStatus() {
		return pinStatus;
	}

	/**
	 * @param pinStatus
	 *            the pinStatus to set
	 */
	public void setPinStatus(boolean pinStatus) {
		this.pinStatus = pinStatus;
	}

	/**
	 * @return the isArchieve
	 */
	public boolean isArchieve() {
		return isArchieve;
	}

	/**
	 * @param isArchieve
	 *            the isArchieve to set
	 */
	public void setArchieve(boolean isArchieve) {
		this.isArchieve = isArchieve;
	}

	/**
	 * @return the labels
	 */
	public List<Label> getLabel() {
		return label;
	}

	/**
	 * @param labels
	 *            the labels to set
	 */
	public void setLabel(List<Label> labels) {
		this.label = labels;
	}

	/**
	 * @return the imageDesc
	 */
	public List<MetaData> getData() {
		return data;
	}

	/**
	 * @param imageDesc
	 *            the imageDesc to set
	 */
	public void setData(List<MetaData> data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", title=" + title + ", description=" + description + ", createdAt="
				+ createdAt + ", updatedAt=" + updatedAt + ", user=" + user + ", trashStatus=" + trashStatus
				+ ", pinStatus=" + pinStatus + ", isArchieve=" + isArchieve + ", label=" + label + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (isArchieve ? 1231 : 1237);
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((noteId == null) ? 0 : noteId.hashCode());
		result = prime * result + (pinStatus ? 1231 : 1237);
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + (trashStatus ? 1231 : 1237);
		result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Note other = (Note) obj;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (isArchieve != other.isArchieve)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (noteId == null) {
			if (other.noteId != null)
				return false;
		} else if (!noteId.equals(other.noteId))
			return false;
		if (pinStatus != other.pinStatus)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (trashStatus != other.trashStatus)
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
