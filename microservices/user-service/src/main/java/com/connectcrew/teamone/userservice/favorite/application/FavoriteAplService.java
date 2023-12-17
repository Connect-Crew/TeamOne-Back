package com.connectcrew.teamone.userservice.favorite.application;

import com.connectcrew.teamone.userservice.favorite.application.port.in.QueryFavoriteUseCase;
import com.connectcrew.teamone.userservice.favorite.application.port.in.SaveFavoriteUseCase;
import com.connectcrew.teamone.userservice.favorite.application.port.in.command.SetFavoriteCommand;
import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoriteQuery;
import com.connectcrew.teamone.userservice.favorite.application.port.in.query.FindFavoritesQuery;
import com.connectcrew.teamone.userservice.favorite.application.port.out.DeleteFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.application.port.out.FindFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.application.port.out.SaveFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteAplService implements SaveFavoriteUseCase, QueryFavoriteUseCase {
    private final FindFavoriteOutput findFavoriteOutput;
    private final SaveFavoriteOutput saveFavoriteOutput;
    private final DeleteFavoriteOutput deleteFavoriteOutput;

    @Override
    public Mono<Boolean> setFavorite(SetFavoriteCommand command) {
        return findFavoriteOutput.existsByUserIdAndTypeAndTarget(command.userId(), command.favoriteType(), command.target())
                .flatMap(exists -> {
                    if (exists) return deleteFavoriteOutput.delete(command.toDomain()).thenReturn(false);
                    else return saveFavoriteOutput.save(command.toDomain()).thenReturn(true);
                });
    }

    @Override
    public Mono<Map<Long, Boolean>> findMapByCommand(FindFavoritesQuery query) {
        return findFavoriteOutput.findAllByUserIdAndTypeAndTargets(query.userId(), query.favoriteType(), query.targets())
                .collectMap(Favorite::target, favorite -> true)
                .map(map -> {
                    Arrays.stream(query.targets()).forEach(id -> map.putIfAbsent(id, false));
                    return map;
                });
    }

    @Override
    public Mono<Boolean> findByCommand(FindFavoriteQuery query) {
        return findFavoriteOutput.findByUserIdAndTypeAndTarget(query.userId(), query.favoriteType(), query.target())
                .map(favorite -> true)
                .defaultIfEmpty(false);
    }
}
