/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgeit.discoveryclientnote.noteservice.model.LabelDto;
import com.bridgeit.discoveryclientnote.noteservice.model.Note;
import com.bridgeit.discoveryclientnote.noteservice.model.NoteDto;
import com.bridgeit.discoveryclientnote.noteservice.model.ResponseDto;
import com.bridgeit.discoveryclientnote.noteservice.repository.IFeign;
import com.bridgeit.discoveryclientnote.noteservice.service.INoteService;
import com.bridgeit.discoveryclientnote.utilityservice.Messages;
import com.bridgeit.discoveryclientnote.utilityservice.exception.ToDoException;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>Note Controller Class.</b>
 *        </p>
 */
@RestController
@RequestMapping("/note")
public class NoteController {
	public static final Logger logger = LoggerFactory.getLogger(NoteController.class);

	final String REQ_ID = "IN_Note";
	final String RES_ID = "OUT_Note";

	@Autowired
	INoteService service;

	/*
	 * @Autowired NoteInterceptor noteInterceptor;
	 */

	@Autowired
	Messages messages;

	@Autowired
	IFeign feign;

	/**
	 * @param note
	 * @param token
	 * @return ResponseEntity
	 *         </p>
	 *         To create a Note based on token passed.
	 *         </p>
	 * @throws IOException
	 */
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@ApiOperation(value = "Create new note")
	public ResponseEntity<String> createNote(@RequestBody NoteDto note, HttpServletRequest request,
			HttpServletResponse res, @RequestHeader("token") String token) throws ToDoException, IOException {
		logger.debug("---Creating Note---");
		logger.info(REQ_ID + " Creating note");
		if (request.getHeader("userId") != null) {
			res.addHeader("userId", request.getHeader("userId"));
		}
		String userId = request.getHeader("userId");
		System.out.println("------------" + userId);
		String noteId = service.createNote(note, userId);
		logger.info(RES_ID + " Note Created");
		return new ResponseEntity<>(messages.get("115") + "  " + noteId, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity
	 *         <p>
	 *         To move a note to Trash based on token and noteId.
	 *         </p>
	 * @throws ToDoException
	 */
	@RequestMapping(value = "/trash", method = RequestMethod.DELETE)
	@ApiOperation(value = "Move note to trash")
	public ResponseEntity<String> deleteNote(@RequestParam String noteId, @RequestHeader("token") String token,
			HttpServletRequest request) throws ToDoException {
		logger.debug("---Deleting Note To Trash---");
		logger.info(REQ_ID + " Deleting note to trash");
		String userId = (String) request.getHeader("userId");
		String id = service.delete(noteId, userId);
		logger.info(RES_ID + " Note moved to trash");
		logger.warn("Note is in Trash If Needed, It can restored back...");
		return new ResponseEntity<>(messages.get("118") + " " + id + " is present in trash", HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param note
	 * @param token
	 * @return ResponseEntity
	 *         <p>
	 *         To update a existing Note based on token and noteId.
	 *         </p>
	 * @throws ToDoException
	 */
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@ApiOperation(value = "update exist note")
	public ResponseEntity<String> updateNote(@RequestParam("note id") String noteId, @RequestBody NoteDto note,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Updating Note---");
		logger.info(REQ_ID + " Updating note");
		String userId = (String) request.getHeader("userId");
		String updateAt = service.update(noteId, note, userId);
		logger.info(RES_ID + " Updated Note");
		logger.warn("Note is in Trash are not displayed...");
		return new ResponseEntity<>(messages.get("117") + " " + updateAt, HttpStatus.OK);
	}

	/**
	 * @param token
	 * @return ResponseEntity
	 *         <p>
	 *         To display a particular note based on token.
	 *         </p>
	 * @throws ToDoException
	 */
	@RequestMapping(value = "/display", method = RequestMethod.GET)
	@ApiOperation(value = "Display all notes")
	public ResponseEntity<List<Note>> displayNote(@RequestHeader("token") String token, HttpServletRequest req,
			HttpServletResponse res) throws ToDoException {
		logger.debug("---Displaying Note---");
		logger.info(REQ_ID + " Display notes");
		if (req.getHeader("userId") != null) {
			res.addHeader("userId", req.getHeader("userId"));
		}
		String userId = req.getHeader("userId");
		System.out.println(userId);
		List<Note> note = null;
		note = service.display(userId);
		return new ResponseEntity<>(note, HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity
	 *         <p>
	 *         To delete a note permanently from database.
	 *         </p>
	 * @throws ToDoException
	 */
	@RequestMapping(value = "/delete/{noteID}", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete note from database")
	public ResponseEntity<String> deleteNotePermanently(@PathVariable("noteID") String noteId,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---deleting Note---");
		logger.info(REQ_ID + " To Delete Note");
		String userId = (String) request.getHeader("userId");
		String id = service.deletePermanent(noteId, userId);
		logger.info(RES_ID + " Note deleted");
		return new ResponseEntity<>(id + " " + messages.get("116"), HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity
	 * @throws ToDoException
	 *             <p>
	 *             To Restore the data from trash.
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/restore", method = RequestMethod.PUT)
	@ApiOperation(value = "restore note from trash")
	public ResponseEntity<String> restoreFromTrash(@RequestParam("NoteId") String noteId,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Restore Note---");
		logger.info(REQ_ID + " To restore Note from trash");
		String userId = (String) request.getHeader("userId");
		service.restoreFromTrash(noteId, userId);
		logger.info(RES_ID + " Note Restored");
		return new ResponseEntity(messages.get("119"), HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity
	 * @throws ToDoException
	 *             <p>
	 *             To make a note as Important note.
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/pin", method = RequestMethod.PUT)
	@ApiOperation(value = "pin note")
	public ResponseEntity<String> pinNote(@RequestParam("NoteId") String noteId, @RequestHeader("token") String token,
			HttpServletRequest request) throws ToDoException {
		logger.debug("---Pin Note---");
		logger.info(REQ_ID + " To make a Note as Important");
		String userId = (String) request.getHeader("userId");
		service.pinNote(noteId, userId);
		logger.info(RES_ID + " Note is Pinned");
		return new ResponseEntity(messages.get("120"), HttpStatus.OK);
	}

	/**
	 * @param noteId
	 * @param token
	 * @return ResponseEntity
	 * @throws ToDoException
	 *             <p>
	 *             x To make a note Archieve
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/archieve", method = RequestMethod.PUT)
	@ApiOperation(value = "Archieve note")
	public ResponseEntity<String> ArchieveNote(@RequestParam("NoteId") String noteId,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Archieve Note---");
		logger.info(REQ_ID + " To make a Note as Archieve");
		String userId = (String) request.getHeader("userId");
		service.archieveNote(noteId, userId);
		logger.info(RES_ID + " Note is Archieved");
		return new ResponseEntity(messages.get("122"), HttpStatus.OK);
	}

	/**
	 * @param token
	 * @param id
	 * @param reminderTime
	 * @return ResponseEntity
	 * @throws Exception
	 *             <p>
	 *             To make Reminder of particular note based on noteId given.
	 *             </P>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/remind", method = RequestMethod.PUT)
	@ApiOperation(value = "Reminder note")
	public ResponseEntity<String> remindNote(@RequestHeader("token") String token, @RequestParam("NoteId") String id,
			@RequestParam("time") String reminderTime, HttpServletRequest request) throws Exception {
		logger.debug("---Remind Note---");
		logger.info(REQ_ID + " Note Reminder");
		String userId = (String) request.getHeader("userId");
		Note remind = service.setReminder(userId, id, reminderTime);
		return new ResponseEntity(messages.get("121") + " " + remind, HttpStatus.OK);
	}

	/**
	 * @param labelDto
	 * @param token
	 * @return ResponseEntity
	 *         <p>
	 *         To create a label
	 *         </p>
	 * @throws ToDoException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/createlabel", method = RequestMethod.POST)
	@ApiOperation(value = "Create new label")
	public ResponseEntity<String> createLabel(@RequestBody LabelDto labelDto, @RequestHeader("token") String token,
			HttpServletRequest request) throws ToDoException {
		logger.debug("---Creating Label---");
		logger.info(REQ_ID + " To create a label");
		String userId = (String) request.getHeader("userId");
		service.createLabel(labelDto, userId);
		return new ResponseEntity("Label created", HttpStatus.OK);
	}

	/**
	 * @param token
	 * @param name
	 * @return ResponseEntity
	 * @throws ToDoException
	 *             <p>
	 *             To delete a label based on label name
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/deletelabel", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete label from note and labellist")
	public ResponseEntity<String> deletelabel(@RequestHeader("token") String token, @RequestParam("labelId") String id,
			HttpServletRequest request) throws ToDoException {
		logger.debug("---Deleting Label---");
		logger.info(REQ_ID + " To delete a label");
		String userId = (String) request.getHeader("userId");
		service.deleteLabel(id, userId);
		return new ResponseEntity("Label deleted", HttpStatus.OK);
	}

	/**
	 * @param token
	 * @param id
	 * @param labelDto
	 * @return ResponseEntity
	 * @throws ToDoException
	 *             <p>
	 *             To update a existing label
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/updatelabel", method = RequestMethod.POST)
	@ApiOperation(value = "update a label")
	public ResponseEntity<String> updateLabel(@RequestHeader("token") String token, @RequestParam("id") String id,
			@RequestBody LabelDto labelDto, HttpServletRequest request) throws ToDoException {
		logger.debug("---Updating Label---");
		logger.info(REQ_ID + " To update a label");
		String userId = (String) request.getHeader("userId");
		service.updateLabel(id, labelDto, userId);
		return new ResponseEntity("Label updated", HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param note
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             Add existing label to the note
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/addlabel", method = RequestMethod.POST)
	@ApiOperation(value = "add existing label to note")
	public ResponseEntity<String> addLabel(@RequestParam String labelId, @RequestParam("NoteId") String note,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Adding Label to Note ---");
		logger.info(REQ_ID + " To add a label to note");
		String userId = (String) request.getHeader("userId");
		service.addLabel(labelId, userId, note);
		return new ResponseEntity("Label added", HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             delete a label from note and labelList
	 *             </p>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "/deletelabelfromnote", method = RequestMethod.DELETE)
	@ApiOperation(value = "delete label from note")
	public ResponseEntity<String> deleteLabelFromNote(@RequestParam("LabelId") String labelId,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Deleting Label From Note---");
		logger.info(REQ_ID + " To delete a label from note");
		String userId = (String) request.getHeader("userId");
		service.deleteLabelFromNote(labelId, userId);
		return new ResponseEntity("Label removed", HttpStatus.OK);
	}

	/**
	 * @param labelId
	 * @param note
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             To Rename a labelName in labelList and Note.
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/rename", method = RequestMethod.POST)
	@ApiOperation(value = "rename the label name")
	public ResponseEntity<String> rename(@RequestParam String labelId, @RequestParam("New Label name") String note,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Renaming Label---");
		logger.info(REQ_ID + " To rename a label");
		String userId = (String) request.getHeader("userId");
		service.renameLabel(labelId, userId, note);
		return new ResponseEntity("Label renamed successfully", HttpStatus.OK);
	}

	/**
	 * @param labelName
	 * @param note
	 * @param token
	 * @return
	 * @throws ToDoException
	 *             <p>
	 *             To add a new label to note and LabelList.
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/addnewlabel", method = RequestMethod.POST)
	@ApiOperation(value = "add new label")
	public ResponseEntity<String> add(@RequestParam String labelName, @RequestParam("NoteId") String note,
			@RequestHeader("token") String token, HttpServletRequest request) throws ToDoException {
		logger.debug("---Adding New Label---");
		logger.info(REQ_ID + " To add a label to note");
		String userId = (String) request.getHeader("userId");
		service.addNewLabel(note, labelName, userId);
		return new ResponseEntity("Label added successfully", HttpStatus.OK);
	}

	/**
	 * @param url
	 * @param token
	 * @param request
	 * @param note
	 * @return ResponseEntity of string type
	 * @throws IOException
	 * @throws ToDoException
	 *             <p>
	 *             To add new url or string in description variable. if Url is
	 *             provided then metadata of that url is stored. if String data is
	 *             provided then it simply stores that string.
	 *             </p>
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = "/addimage", method = RequestMethod.POST)
	public ResponseEntity<String> addImage(@RequestParam("Description") String url,
			@RequestHeader("token") String token, HttpServletRequest request, @RequestParam("NoteId") String note)
			throws IOException, ToDoException {
		logger.debug("---Adding New image---");
		String userId = (String) request.getHeader("userId");
		Optional<Note> details = service.addimage(url, note, userId);
		return new ResponseEntity(details, HttpStatus.OK);
	}

	/**
	 * @returList of user details
	 *            <p>
	 *            This method returns list of user present in UserService using
	 *            Feign client.
	 *            </p>
	 */
	@GetMapping("/getuser")
	public List<?> getUserFromUserService() {
		logger.info("Getting user details");  
		List<?> user = feign.getUser();
		
		logger.info("{}", user);
		return user;
	}
	
	@GetMapping("/finduser/{email}")
	public Optional<?> getUserFromEmail(@PathVariable String email) {
		logger.info("Getting user details");  
		Optional<?> user = feign.getUserByEmail(email);
		logger.info("{}", user);
		return user;
	}
}
