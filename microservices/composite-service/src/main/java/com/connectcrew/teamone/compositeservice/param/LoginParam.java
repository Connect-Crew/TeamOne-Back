package com.connectcrew.teamone.compositeservice.param;

import com.connectcrew.teamone.api.user.auth.Social;

public record LoginParam (
        String token,
        Social social
){
}
