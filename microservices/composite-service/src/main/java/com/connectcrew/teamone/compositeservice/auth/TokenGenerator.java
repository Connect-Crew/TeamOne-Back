package com.connectcrew.teamone.compositeservice.auth;

import com.connectcrew.teamone.api.user.auth.Role;

public interface TokenGenerator {

    String createAccessToken(String account, Role role);

    String createRefreshToken(String account, Role role);
}
