package com.connectcrew.teamone.compositeservice.wish.application.port.out;

import com.connectcrew.teamone.compositeservice.wish.domain.Wish;

public interface SendWishMessageOutput {
    Wish send(Wish wish);
}
