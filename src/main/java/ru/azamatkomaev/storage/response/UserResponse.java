package ru.azamatkomaev.storage.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.azamatkomaev.storage.model.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String username;

    public static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .username(user.getUsername())
            .build();
    }

    public User fromUserResponse() {
        return User.builder()
            .id(id)
            .username(username)
            .build();
    }
}
