package com.connectcrew.teamone.userservice.favorite;

import com.connectcrew.teamone.api.user.favorite.FavoriteType;
import com.connectcrew.teamone.userservice.favorite.adapter.FavoriteEntity;
import com.connectcrew.teamone.userservice.favorite.adapter.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;

    @GetMapping("/favorites")
    Mono<Map<Long, Boolean>> getFavorites(Long userId, FavoriteType type, Long[] ids) {
        return favoriteRepository.findAllByProfileIdAndTypeAndTargetIn(userId, type.name(), Arrays.stream(ids).toList()).collectList()
                .map(favorites -> {
                    Map<Long, Boolean> result = new HashMap<>();
                    favorites.forEach(favorite -> result.put(favorite.getTarget(), true));
                    Arrays.stream(ids).forEach(id -> result.putIfAbsent(id, false));
                    return result;
                });
    }

    @GetMapping("/")
    Mono<Boolean> getFavorite(Long userId, FavoriteType type, Long target) {
        return favoriteRepository.existsByProfileIdAndTypeAndTarget(userId, type.name(), target);
    }

    @PostMapping("/")
    Mono<Boolean> setFavorite(Long userId, FavoriteType type, Long target) {
        return favoriteRepository.existsByProfileIdAndTypeAndTarget(userId, type.name(), target)
                .flatMap(exists -> {
                    if (exists) {
                        return favoriteRepository.deleteByProfileIdAndTypeAndTarget(userId, type.name(), target).thenReturn(false);
                    } else {
                        return favoriteRepository.save(FavoriteEntity.builder()
                                        .profileId(userId)
                                        .type(type.name())
                                        .target(target)
                                        .build())
                                .thenReturn(true);
                    }
                });
    }
}
