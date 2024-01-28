package com.connectcrew.teamone.compositeservice.wish.application;

import com.connectcrew.teamone.compositeservice.wish.adapter.in.web.response.WishResponse;
import com.connectcrew.teamone.compositeservice.wish.application.port.in.SendWishUseCase;
import com.connectcrew.teamone.compositeservice.wish.application.port.in.command.SendWishCommand;
import com.connectcrew.teamone.compositeservice.wish.application.port.out.SendWishMessageOutput;
import com.connectcrew.teamone.compositeservice.wish.domain.Wish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WishAplService implements SendWishUseCase {
    private final SendWishMessageOutput sendWishMessageOutput;


    @Override
    public Wish sendWish(SendWishCommand command) {
        return sendWishMessageOutput.send(command.toDomain());
    }
}
