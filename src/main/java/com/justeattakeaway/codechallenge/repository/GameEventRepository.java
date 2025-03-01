package com.justeattakeaway.codechallenge.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
interface GameEventRepository extends MongoRepository<GameEventDocument, String> {
    List<GameEventDocument> findByGameIdOrderByTimestampAsc(String gameId);
    Optional<GameEventDocument> findFirstByGameIdOrderByTimestampDesc(String gameId);
}