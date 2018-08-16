/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.model;

import org.jsoup.nodes.Element;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 10-07-2018 <br>
 *        <p>
 *        <b>POJO Class having Metadata of Url related information and method.</b>
 *        </p>
 */
public class MetaData {

	private String title;
	private String link;
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the link
	 */
	public String getLink() {
		return link;
	}
	/**
	 * @param image the link to set
	 */
	public void setLink(String image) {
		this.link = image;
	}
	
}
