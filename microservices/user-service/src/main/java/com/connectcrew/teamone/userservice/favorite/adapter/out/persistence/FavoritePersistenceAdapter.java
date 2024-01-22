package com.connectcrew.teamone.userservice.favorite.adapter.out.persistence;

import com.connectcrew.teamone.userservice.favorite.adapter.out.persistence.entity.FavoriteEntity;
import com.connectcrew.teamone.userservice.favorite.adapter.out.persistence.repository.FavoriteRepository;
import com.connectcrew.teamone.userservice.favorite.application.port.out.DeleteFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.application.port.out.FindFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.application.port.out.SaveFavoriteOutput;
import com.connectcrew.teamone.userservice.favorite.domain.Favorite;
import com.connectcrew.teamone.api.userservice.favorite.FavoriteType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FavoritePersistenceAdapter implements SaveFavoriteOutput, FindFavoriteOutput, DeleteFavoriteOutput {

    private final FavoriteRepository favoriteRepository;

    @Override
    public Flux<Favorite> findAllByUserIdAndTypeAndTargets(Long userId, FavoriteType type, Long[] targets) {
        return favoriteRepository.findAllByProfileIdAndTypeAndTargetIn(userId, type.name(), Arrays.stream(targets).toList())
                .map(FavoriteEntity::toDomain);
    }

    @Override
    public Mono<Favorite> findByUserIdAndTypeAndTarget(Long userId, FavoriteType type, Long target) {
        return favoriteRepository.findByProfileIdAndTypeAndTarget(userId, type.name(), target)
                .map(FavoriteEntity::toDomain);
    }

    @Override
    public Mono<Boolean> existsByUserIdAndTypeAndTarget(Long userId, FavoriteType type, Long target) {
        return favoriteRepository.existsByProfileIdAndTypeAndTarget(userId, type.name(), target);
    }

    @Override
    public Mono<Favorite> save(Favorite favorite) {
        return favoriteRepository.save(FavoriteEntity.fromDomain(favorite))
                .map(FavoriteEntity::toDomain);
    }

    @Override
    public Mono<Favorite> delete(Favorite favorite) {
        return favoriteRepository.deleteByProfileIdAndTypeAndTarget(favorite.userId(), favorite.favoriteType().name(), favorite.target())
                .thenReturn(favorite);
    }
}
