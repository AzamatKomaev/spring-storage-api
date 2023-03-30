package ru.azamatkomaev.storage.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.azamatkomaev.storage.response.EventResponse;
import ru.azamatkomaev.storage.service.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/events")
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("")
    public List<EventResponse> getAllEvents() {
        return eventService.getAll()
            .stream()
            .map(EventResponse::toEventResponse).toList();
    }

    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable("id") Long id) {
        return EventResponse.toEventResponse(eventService.getById(id));
    }
}
