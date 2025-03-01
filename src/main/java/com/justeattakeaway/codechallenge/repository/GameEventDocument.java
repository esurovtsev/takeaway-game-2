package com.justeattakeaway.codechallenge.repository;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.justeattakeaway.codechallenge.domain.GameEvent;

import lombok.Data;
import lombok.NonNull;

@Document(collection = "game_events")
@Data
class GameEventDocument {
    @Id
    private String id;
    private String gameId;
    private LocalDateTime timestamp;
    @Field("event_data")
    private GameEvent event;
    
    public static GameEventDocument fromGameEvent(@NonNull GameEvent event) {
        GameEventDocument doc = new GameEventDocument();
        doc.gameId = event.gameId();
        doc.timestamp = event.timestamp();
        doc.event = event;
        return doc;
    }
}