package com.connectcrew.teamone.compositeservice.wish.application.port.in;

import com.connectcrew.teamone.compositeservice.wish.application.port.in.command.SendWishCommand;
import com.connectcrew.teamone.compositeservice.wish.domain.Wish;

public interface SendWishUseCase {
    Wish sendWish(SendWishCommand command);
}
