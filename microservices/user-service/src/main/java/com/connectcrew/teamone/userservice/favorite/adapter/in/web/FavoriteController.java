package com.connectcrew.teamone.userservice.favorite.adapter.in.web;

import com.connectcrew.teamone.userservice.favorite.application.port.in.QueryFavoriteUseCase;
import com.connectcrew.teamone.userservice.favorite.application.port.in.SaveFavoriteUseCase;
import com.connectcrew.teamone.userservice.favorite.application.port.in.command.SetFavoriteCommand;
import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoriteQuery;
import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoritesQuery;
import com.connectcrew.teamone.userservice.favorite.domain.enums.FavoriteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final QueryFavoriteUseCase queryFavoriteUseCase;
    private final SaveFavoriteUseCase saveFavoriteUseCase;

    @GetMapping("/favorites")
    Mono<Map<Long, Boolean>> getFavorites(Long userId, FavoriteType type, Long[] targets) {
        return queryFavoriteUseCase.findMapByCommand(new FindFavoritesQuery(userId, type, targets));
    }

    @GetMapping("/")
    Mono<Boolean> getFavorite(Long userId, FavoriteType type, Long target) {
        return queryFavoriteUseCase.findByCommand(new FindFavoriteQuery(userId, type, target));
    }

    @PostMapping("/")
    Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target) {
        return saveFavoriteUseCase.setFavorite(new SetFavoriteCommand(userId, type, target));
    }
}
