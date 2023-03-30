package ru.azamatkomaev.storage.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azamatkomaev.storage.model.Event;
import ru.azamatkomaev.storage.model.File;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {
    private Long id;
    private String name;
    private String file_path;

    public static FileResponse toFileResponse(File file) {
        return FileResponse.builder()
            .id(file.getId())
            .name(file.getName())
            .file_path(file.getFilePath())
            .build();
    }
}