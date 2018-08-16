/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>Note Dto class.</b>
 *        </p>
 */
@SuppressWarnings("serial")
public class NoteDto implements Serializable {

	private String title;
	private String description;
	private String user;
	private List<LabelDto> label;
	private List<MetaData> data;

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
	 * @return the label
	 */
	public List<LabelDto> getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            the label to set
	 */
	public void setLabel(List<LabelDto> label) {
		this.label = label;
	}
	

	/**
	 * @return the imageDesc
	 */
	public List<MetaData> getData() {
		return data;
	}

	/**
	 * @param imageDesc the imageDesc to set
	 */
	public void setData(List<MetaData> data) {
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 1;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		NoteDto other = (NoteDto) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
