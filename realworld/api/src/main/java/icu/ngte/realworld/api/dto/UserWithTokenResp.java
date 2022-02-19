package icu.ngte.realworld.api.dto;

import icu.ngte.realworld.infra.dmr.data.UserData;
import lombok.Getter;

@Getter
public class UserWithTokenResp {
    private String email;
    private String username;
    private String bio;
    private String image;
    private String token;

    public UserWithTokenResp(UserData userData, String token) {
        this.email = userData.getEmail();
        this.username = userData.getUsername();
        this.bio = userData.getBio();
        this.image = userData.getImage();
        this.token = token;
    }

}
