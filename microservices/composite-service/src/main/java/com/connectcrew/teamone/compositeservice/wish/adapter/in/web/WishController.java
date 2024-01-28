package com.connectcrew.teamone.compositeservice.wish.adapter.in.web;

import com.connectcrew.teamone.compositeservice.auth.application.JwtProvider;
import com.connectcrew.teamone.compositeservice.auth.domain.TokenClaim;
import com.connectcrew.teamone.compositeservice.wish.adapter.in.web.request.WishRequest;
import com.connectcrew.teamone.compositeservice.wish.adapter.in.web.response.WishResponse;
import com.connectcrew.teamone.compositeservice.wish.application.port.in.SendWishUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WishController {
    private final JwtProvider jwtProvider;

    private final SendWishUseCase sendWishUseCase;

    @PostMapping("/wish")
    public WishResponse sendWish(@RequestHeader(JwtProvider.AUTH_HEADER) String token, @RequestBody WishRequest request) {
        TokenClaim claim = jwtProvider.getTokenClaim(token);
        Long id = claim.id();

        return WishResponse.of(sendWishUseCase.sendWish(request.toCommand(id)));
    }
}
