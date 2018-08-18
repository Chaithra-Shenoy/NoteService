/**
 * 
 */
package com.bridgeit.discoveryclientnote.noteservice.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgeit.discoveryclientnote.noteservice.model.Label;
import com.bridgeit.discoveryclientnote.noteservice.model.LabelDto;
import com.bridgeit.discoveryclientnote.noteservice.model.MetaData;
import com.bridgeit.discoveryclientnote.noteservice.model.Note;
import com.bridgeit.discoveryclientnote.noteservice.model.NoteDto;
import com.bridgeit.discoveryclientnote.noteservice.repository.IElasticRepoLabel;
import com.bridgeit.discoveryclientnote.noteservice.repository.IElasticSearchRepository;
import com.bridgeit.discoveryclientnote.noteservice.repository.ILabelRepository;
import com.bridgeit.discoveryclientnote.noteservice.repository.INoteRepository;
import com.bridgeit.discoveryclientnote.utilityservice.Messages;
import com.bridgeit.discoveryclientnote.utilityservice.PreCondition;
import com.bridgeit.discoveryclientnote.utilityservice.Utility;
import com.bridgeit.discoveryclientnote.utilityservice.exception.ToDoException;

/**
 * 
 * 
 * @author Chaithra-Shenoy
 * @since Date 18-07-2018 <br>
 *        <p>
 *        <b>NoteService implementation class having implementation for the
 *        methods declared in INoteService interface..</b>
 *        </p>
 */
@Service
public class NoteServiceImpl implements INoteService {
	public static final Logger logger = LoggerFactory.getLogger(NoteServiceImpl.class);

	@Autowired
	INoteRepository repository;

	@Autowired
	ILabelRepository labelRepository;

	@Autowired
	Utility util;

	@Autowired
	private ModelMapper model;

	@Autowired
	Messages messages;

	@Autowired
	PreCondition precondition;

	@Autowired
	IElasticSearchRepository elasticRepository;

	@Autowired
	IElasticRepoLabel elasticLabel;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#createNote(com.
	 * bridgeit.todoapplication.noteservice.model.Note, java.lang.String)
	 */
	@Override
	public String createNote(NoteDto note, String userId) throws ToDoException, IOException {
		MetaData data = new MetaData();
		precondition.checkNotNull(note.getDescription(), messages.get("109"));
		precondition.checkNotNull(note.getTitle(), messages.get("110"));
		precondition.checkNotNull(userId, messages.get("102"));
		logger.debug("-user id from token-");
		note.equals(note.getDescription());
		logger.info(userId);
		if (note.getDescription().contains("https://") || note.getDescription().contains("http://")) {
			Document doc = Jsoup.connect(note.getDescription()).get();
			String title = doc.select("title").first().text();
			System.out.println("Title : " + title);
			data.setTitle(title);
			String image = doc.select("img").first().attr("src");
			System.out.println("image : " + image);
			data.setLink(image);
			note.getData().add(data);
		}
		note.setDescription(note.getDescription());
		logger.debug("-Details entered by user is mapped to Note-");
		Note noteModel = model.map(note, Note.class);
		List<Label> label = noteModel.getLabel();

		System.out.println(label);
		noteModel.setUser(userId);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String createdDate = simpleDateFormat.format(new Date());
		noteModel.setUpdatedAt(createdDate);
		noteModel.setCreatedAt(createdDate);
		logger.debug("-Note Saved-");
		repository.save(noteModel);
		elasticRepository.save(noteModel);
		return repository.save(noteModel).getNoteId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#delete(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public String delete(String noteId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkArgument(repository.existsById(noteId), messages.get("112"));
		Note note = elasticRepository.findById(noteId).get();
		logger.info(note.toString());
		note.setTrashStatus(true);
		repository.save(note);
		elasticRepository.save(note);
		return repository.save(note).getNoteId();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#update(java.
	 * lang.String, com.bridgeit.todoapplication.noteservice.model.Note,
	 * java.lang.String)
	 */
	@Override
	public String update(String noteId, NoteDto note, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkNotNull(note.getDescription(), messages.get("109"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		Optional<Note> note1 = elasticRepository.findById(noteId);
		logger.info("note is found from elasticdb" + note1.toString());
		if (note1.get().isTrashStatus()) {
			logger.error(messages.get("114"));
			throw new ToDoException(messages.get("114"));
		}
		Note noteModel = model.map(note, Note.class);
		noteModel.setNoteId(noteId);
		noteModel.setUser(userId);
		noteModel.setCreatedAt(note1.get().getCreatedAt());
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		noteModel.setUpdatedAt(simpleDateFormat.format(new Date()));
		elasticRepository.save(noteModel);
		logger.info("Note updated in Elastic database");
		return repository.save(noteModel).getUpdatedAt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#display(java.
	 * lang.String)
	 */
	@Override
	public List<Note> display(String userId) throws ToDoException {
		List<Note> list = new ArrayList<>();
		// List<Note> modifiedList = new ArrayList<>();
		precondition.checkNotNull(userId, messages.get("102"));
		List<Note> list1 = elasticRepository.findByUser(userId);
		list1.stream().filter(streamNote -> (streamNote.isPinStatus() == true && streamNote.isTrashStatus() == false
				&& streamNote.isArchieve() == false)).forEach(list3 -> list.add(list3));
		list1.stream().filter(streamNote -> (streamNote.isPinStatus() == false && streamNote.isTrashStatus() == false
				&& streamNote.isArchieve() == false)).forEach(list3 -> list.add(list3));
		list1.stream().filter(streamNote -> (streamNote.isPinStatus() == false && streamNote.isTrashStatus() == false
				&& streamNote.isArchieve() == true)).forEach(list3 -> list.add(list3));
		/*
		 * for (Note n : list) { if (n.isPinStatus() && !n.isTrashStatus() &&
		 * !n.isArchieve()) { modifiedList.add(n); } } for (Note n : list) { if
		 * (!n.isArchieve() && !n.isTrashStatus() && !n.isPinStatus()) {
		 * modifiedList.add(n); } } for (Note n : list) { if (n.isArchieve() &&
		 * !n.isTrashStatus()) { modifiedList.add(n); } } return modifiedList;
		 */
		return list1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#deletePermanent
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public String deletePermanent(String noteId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		elasticRepository.deleteById(noteId);
		repository.deleteById(noteId);
		return noteId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bridgeit.todoapplication.noteservice.service.INoteService#
	 * restoreFromTrash(java.lang.String, java.lang.String)
	 */
	@Override
	public void restoreFromTrash(String noteId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("100"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		Note note = elasticRepository.findById(noteId).get();
		if (note.isTrashStatus()) {
			note.setTrashStatus(false);
			elasticRepository.save(note);
			repository.save(note);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#pinNote(java.
	 * lang.String, java.lang.String)
	 */
	@Override
	public void pinNote(String noteId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		Note note = elasticRepository.findById(noteId).get();
		logger.info(note.toString());
		if (!note.isTrashStatus()||note.isPinStatus()==false) {
			note.setPinStatus(true);
		}
		if (!note.isTrashStatus()||note.isPinStatus()==true) {
			note.setPinStatus(false);
		}
		elasticRepository.save(note);
		repository.save(note);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#ArchieveNote(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void archieveNote(String noteId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		Note note = elasticRepository.findById(noteId).get();
		logger.info(note.toString());
		if (!note.isTrashStatus()||note.isArchieve()==false) {
			note.setArchieve(true);
		}
		if (!note.isTrashStatus()||note.isArchieve()==true) {
			note.setArchieve(false);
		}
		elasticRepository.save(note);
		repository.save(note);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#setReminder(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Note setReminder(String userId, String id, String reminderTime) throws ParseException, ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(id, messages.get("111"));
		Optional<Note> note = precondition.checkNotNull(elasticRepository.findById(id), messages.get("112"));
		Timer timer = null;
		if (note.isPresent()) {
			Date reminder = new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(reminderTime);
			long timeDifference = reminder.getTime() - new Date().getTime();
			timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					System.out.println("Reminder task:" + note.toString());
				}
			}, timeDifference);
		}
		return note.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#createLabel(com
	 * .bridgeit.todoapplication.noteservice.model.LabelDto, java.lang.String)
	 */
	@Override
	public void createLabel(LabelDto lableDto, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(lableDto.getName(), messages.get("126"));
		logger.info(userId);
		Label labelModel = model.map(lableDto, Label.class);
		System.out.println(labelModel);
		labelModel.setUser(userId);
		elasticLabel.save(labelModel);
		labelRepository.save(labelModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#updateLabel(
	 * java.lang.String, com.bridgeit.todoapplication.noteservice.model.LabelDto,
	 * java.lang.String)
	 */
	@Override
	public void updateLabel(String id, LabelDto lableDto, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(id, messages.get("111"));
		precondition.checkNotNull(lableDto.getName(), messages.get("126"));
		logger.info(userId);
		precondition.checkArgument(elasticLabel.existsById(id), messages.get("110"));
		Label labelModel = model.map(lableDto, Label.class);
		labelModel.setId(id);
		labelModel.setUser(userId);
		elasticLabel.save(labelModel);
		labelRepository.save(labelModel);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#addLabel(java.
	 * lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void addLabel(String labelId, String userId, String noteId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(noteId, messages.get("111"));
		precondition.checkNotNull(labelId, messages.get("128"));
		precondition.checkArgument(elasticRepository.existsById(noteId), messages.get("112"));
		logger.info("adding label");
		Optional<Note> note = elasticRepository.findById(noteId);
		precondition.checkArgument(note.isPresent(), messages.get("113"));
		if (note.get().getLabel() == null) {
			List<Label> newLabelList = new ArrayList<Label>();
			note.get().setLabel(newLabelList);
		}
		Optional<Label> labelFound = elasticLabel.findById(labelId);
		Label label = new Label();
		for (int i = 0; i < note.get().getLabel().size(); i++) {
			if (labelId.equals(note.get().getLabel().get(i).getId())) {
				logger.error("label already present");
				throw new ToDoException(messages.get("129"));
			}
		}
		label.setId(labelFound.get().getId());
		label.setName(labelFound.get().getName());
		note.get().getLabel().add(label);
		elasticRepository.save(note.get());
		repository.save(note.get());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#deleteLabel(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteLabel(String labelId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(labelId, messages.get("128"));
		logger.info("deleting label");
		Optional<Label> labelFound = elasticLabel.findById(labelId);
		if (labelFound == null) {
			logger.error("label not found");
			throw new ToDoException(messages.get("127"));
		}
		elasticLabel.deleteById(labelId);
		labelRepository.deleteById(labelId);
		List<Note> noteList = repository.findAll();
		for (int i = 0; i < noteList.size(); i++) {
			for (int j = 0; j < noteList.get(i).getLabel().size(); j++) {
				if (labelId.equals(noteList.get(i).getLabel().get(j).getId())) {
					noteList.get(i).getLabel().remove(j);
					Note note = noteList.get(i);
					logger.info("label deleted suceesfully");
					elasticRepository.save(note);
					repository.save(note);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#renameLabel(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void renameLabel(String labelId, String userId, String newLabelName) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(labelId, messages.get("128"));
		Optional<Label> labelFound = elasticLabel.findById(labelId);
		precondition.checkArgument(labelFound.isPresent(), messages.get("127"));
		logger.info("renaming the label....");
		labelFound.get().setName(newLabelName);
		elasticLabel.save(labelFound.get());
		labelRepository.save(labelFound.get());
		List<Note> noteList = repository.findAll();
		for (int i = 0; i < noteList.size(); i++) {
			if (noteList.get(i).getLabel() == null) {
				continue;
			}
			for (int j = 0; j < noteList.get(i).getLabel().size(); j++) {
				if (labelId.equals(noteList.get(i).getLabel().get(j).getId())) {
					noteList.get(i).getLabel().get(j).setName(newLabelName);
					Note note = noteList.get(i);
					logger.info("label updated");
					elasticRepository.save(note);
					repository.save(note);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bridgeit.todoapplication.noteservice.service.INoteService#
	 * deleteLabelFromNote(java.lang.String, java.lang.String)
	 */
	@Override
	public void deleteLabelFromNote(String labelId, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(labelId, messages.get("128"));
		logger.info("deleting label");
		List<Note> noteList = repository.findAll();
		for (int i = 0; i < noteList.size(); i++) {
			for (int j = 0; j < noteList.get(i).getLabel().size(); j++) {
				if (labelId.equals(noteList.get(i).getLabel().get(j).getId())) {
					noteList.get(i).getLabel().remove(j);
					Note note = noteList.get(i);
					logger.info("label deleted suceesfully");
					elasticRepository.save(note);
					repository.save(note);
					break;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#addNewLabel(
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unused")
	@Override
	public void addNewLabel(String note, String labelName, String userId) throws ToDoException {
		precondition.checkNotNull(userId, messages.get("102"));
		precondition.checkNotNull(note, messages.get("111"));
		precondition.checkNotNull(labelName, messages.get("126"));
		precondition.checkArgument(elasticRepository.existsById(note), messages.get("112"));
		Optional<Note> optionalNote = elasticRepository.findById(note);
		List<Note> listOfNote = repository.findAll();
		System.out.println(listOfNote);
		LabelDto label = new LabelDto();
		precondition.checkArgument(elasticRepository.existsById(note), messages.get("112"));
		for (Note n : listOfNote) {
			if (n.getNoteId().equals(note)) {
				label.setName(labelName);
				Label labelMap = model.map(label, Label.class);
				elasticLabel.save(labelMap);
				labelRepository.save(labelMap);
				Note noteLabel = model.map(label, Note.class);
				n.getLabel().add(labelMap);
				elasticRepository.save(n);
				repository.save(n);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.bridgeit.todoapplication.noteservice.service.INoteService#addimage(java.
	 * lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public Optional<Note> addimage(String url, String noteId, String userId) throws IOException, ToDoException {
		MetaData data = new MetaData();
		Optional<Note> note = elasticRepository.findById(noteId);
		if (url.contains("https://") || url.contains("http://")) {
			String desc = note.get().getDescription();
			note.get().setDescription(desc + "," + url);
			Document doc = Jsoup.connect(url).get();
			String title = doc.select("title").first().text();
			System.out.println("Title : " + title);
			data.setTitle(title);
			String image = doc.select("img").first().attr("src");
			System.out.println("image : " + image);
			data.setLink(image);
			precondition.checkArgument(note.isPresent(), messages.get("113"));
			if (note.get().getData() == null) {
				List<MetaData> newLabelList = new ArrayList<MetaData>();
				note.get().setData(newLabelList);
			}
		} 
		else {
			note.get().setDescription(note.get().getDescription() + "," + url);
		}
		note.get().getData().add(data);
		elasticRepository.save(note.get());
		repository.save(note.get());
		return note;
	}

	/* (non-Javadoc)
	 * @see com.bridgeit.discoveryclientnote.noteservice.service.INoteService#sortLabelByName(java.lang.String)
	 */
	@Override
	public List<Label> sortLabelByName(String userId,boolean asc) throws ToDoException {
		List<Label> list = null;
		List<Label> newLabelList = new ArrayList<Label>();
		list = labelRepository.findAll();
		newLabelList=list;
		System.out.println(list);
		if(asc==true) {
		Collections.sort(list, (list1, list2) -> {
			return list1.getName().compareTo(list2.getName());
		});
		}
		else if(asc==false) {
			Collections.sort(newLabelList, Collections.reverseOrder());
			return list;	
			
		}
		return list;
	}
	
	@Override
	public List<Label> sortLabelById(String userId) throws ToDoException {
		List<Label> list = null;
		List<Label> newLabelList = new ArrayList<Label>();
		list = labelRepository.findAll();
		System.out.println(list);
		Collections.sort(list, (list1, list2) -> {
			return list1.getId().compareTo(list2.getId());
		});
		return list;
	}

}
