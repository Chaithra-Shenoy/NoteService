/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

import com.bridgeit.discoveryclientnote.noteservice.model.Label;
import com.bridgeit.discoveryclientnote.noteservice.model.LabelDto;
import com.bridgeit.discoveryclientnote.noteservice.model.Note;
import com.bridgeit.discoveryclientnote.noteservice.model.NoteDto;
import com.bridgeit.discoveryclientnote.utilityservice.exception.ToDoException;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>INoteService interface having abstract methods.</b>
 *        </p>
 */

public interface INoteService {

	/**
	 * @param noteId
	 * @param userId
	 * @return String NoteId that is deleted
	 * @throws ToDoException
	 * 
	 *             <p>
	 *             To delete a particular record and keep that record in trash based
	 *             on token provided.
	 *             </p>
	 */
	String delete(String noteId, String userId) throws ToDoException;

	/**
	 * @param noteId
	 * @param note
	 * @param userId
	 * @throws ToDoException
	 * 
	 *             <p>
	 *             To update a note record based on noteId and token provided.
	 *             </p>
	 */
	String update(String noteId, NoteDto note, String userId) throws ToDoException;

	/**
	 * @param userId
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             to display the note id based on the token provided.
	 *             </p>
	 */
	List<Note> display(String userId) throws ToDoException;

	/**
	 * @param noteId
	 * @param userId
	 *            <p>
	 *            To delete a note permanently from database.
	 *            </p>
	 * @throws ToDoException
	 */
	public String deletePermanent(String noteId, String userId) throws ToDoException;

	/**
	 * @param noteId
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To restore a note from the trash.
	 *             </p>
	 */
	void restoreFromTrash(String noteId, String userId) throws ToDoException;

	/**
	 * @param noteId
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To make a note as important note
	 *             </p>
	 */
	public void pinNote(String noteId, String userId) throws ToDoException;

	/**
	 * @param note
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To create a new note by passing note information and token.
	 *             </p>
	 * @throws IOException
	 */
	String createNote(NoteDto note, String userId) throws ToDoException, IOException;

	/**
	 * @param lableDto
	 * @param userId
	 *            <p>
	 *            To create a new label.
	 *            </p>
	 * @throws ToDoException
	 */
	void createLabel(LabelDto lableDto, String userId) throws ToDoException;

	/**
	 * @param noteId
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To make a note as archieve.
	 *             </p>
	 */
	void archieveNote(String noteId, String userId) throws ToDoException;

	/**
	 * @param userId
	 * @param id
	 * @param reminderTime
	 * @return
	 * @throws ParseException
	 * @throws ToDoException
	 * @throws Exception
	 *             <p>
	 *             To keep a reminder to a particular note based on note id and
	 *             token passed. When the reminder date and time is set and on that
	 *             date and time a particular message is sent to user email.
	 *             </p>
	 */
	Note setReminder(String userId, String id, String reminderTime) throws ToDoException, ParseException;

	/**
	 * @param id
	 * @param lableDto
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To update a existing label
	 *             </p>
	 */
	void updateLabel(String id, LabelDto lableDto, String userId) throws ToDoException;

	/**
	 * @param labelId
	 * @param userId
	 * @param noteId
	 * @throws ToDoException
	 *             <p>
	 *             Add existing label to the note
	 *             </p>
	 */
	void addLabel(String labelId, String userId, String noteId) throws ToDoException;

	/**
	 * @param labelId
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             delete a label from note and labelList
	 *             </p>
	 */
	void deleteLabel(String labelId, String userId) throws ToDoException;

	/**
	 * @param labelId
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             Deletes a label from note.
	 *             </p>
	 */
	void deleteLabelFromNote(String labelId, String userId) throws ToDoException;

	/**
	 * @param labelId
	 * @param userId
	 * @param newLabelName
	 * @throws ToDoException
	 *             <p>
	 *             To Rename a labelName in labelList and Note.
	 *             </p>
	 */
	void renameLabel(String labelId, String userId, String newLabelName) throws ToDoException;

	/**
	 * @param note
	 * @param labelName
	 * @param userId
	 * @throws ToDoException
	 *             <p>
	 *             To add a new label to note and LabelList.
	 *             </p>
	 */
	void addNewLabel(String note, String labelName, String userId) throws ToDoException;

	/**
	 * @param url
	 * @param noteId
	 * @param userId
	 * @return Optional
	 * @throws IOException
	 * @throws ToDoException
	 * 
	 *             <p>
	 *             To add new url or string in description variable. if Url is
	 *             provided then metadata of that url is stored. if String data is
	 *             provided then it simply stores that string. It returns the note
	 *             information corresponding to noteId provided.
	 *             </p>
	 */
	Optional<Note> addimage(String url, String noteId, String userId) throws IOException, ToDoException;

	/**
	 * @param userId
	 * @return List
	 * @throws ToDoException
	 *             <p>
	 *             This method used to display the label in sorted order by their
	 *             name.
	 *             </p>
	 */
	List<Label> sortLabelByName(String userId) throws ToDoException;

}
