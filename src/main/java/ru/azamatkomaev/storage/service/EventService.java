package ru.azamatkomaev.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.azamatkomaev.storage.exception.NotFoundException;
import ru.azamatkomaev.storage.model.Event;
import ru.azamatkomaev.storage.model.File;
import ru.azamatkomaev.storage.model.User;
import ru.azamatkomaev.storage.repository.EventRepository;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public Event getById(Long id) {
        return eventRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Cannot find any event with id: " + id));
    }

    public Event getByFileId(Long fileId) {
        return eventRepository
            .findByFileId(fileId)
            .orElseThrow(() -> new NotFoundException("Cannot find any event with file_id: " + fileId));
    }

    public Event save(User user, File file) {
        Event event = Event.builder().user(user).file(file).build();
        return eventRepository.save(event);
    }
}
