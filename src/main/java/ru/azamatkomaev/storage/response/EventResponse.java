package ru.azamatkomaev.storage.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azamatkomaev.storage.model.Event;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse {
    private Long id;
    private UserResponse user;
    private FileResponse file;

    public static EventResponse toEventResponse(Event event) {
        return EventResponse.builder()
            .id(event.getId())
            .user(UserResponse.toUserResponse(event.getUser()))
            .file(FileResponse.toFileResponse(event.getFile()))
            .build();
    }
}
