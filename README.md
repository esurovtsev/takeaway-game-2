# takeaway-game

## Goal

This project is a refactoring of my old tech challenge from 7 years ago ([original project](https://github.com/esurovtsev/takeaway-game)). While I see now better ways to implement the domain model, I decided to focus on a different challenge - improving how components talk to each other. This decision was made to show skills in refactoring existing apps rather than starting from scratch.

Main improvements:
- Replace old blocking API calls with event-based communication
- Add event sourcing to track game history
- Make app structure simpler by using events instead of direct API calls

The game itself is simple - two players take turns dividing a number by 3 until someone reaches 1 and wins. Each player runs independently and follows Domain Driven Design principles.

Note: The domain implementation could be improved (see Potential Improvements section), but due to time constraints and focus on event-driven architecture, I kept the core game logic mostly unchanged.

## Core Architecture

The app is built around two main services that communicate via events:

1. **GameSession** (`com.justeattakeaway.codechallenge.services.GameSession`) - The coordinator
   - Maintains game consistency through event sourcing
   - Tracks active games and their states
   - Coordinates game flow by handling move events and directing commands to proper players, with no direct calls between players

2. **Player** (`com.justeattakeaway.codechallenge.services.Player`) - The game participant
   - Receives move commands and reacts with "move made" events
   - Uses strategy pattern for move calculation
   - Completely independent - knows nothing about other players or game state

The event-based communication happens through:
- `MakeMoveCommand` (`com.justeattakeaway.codechallenge.messaging.MakeMoveCommand`) - tells a player it's their turn
- `MoveMadeEvent` (`com.justeattakeaway.codechallenge.messaging.MoveMadeEvent`) - records what move was made
- `GameEvent` (`com.justeattakeaway.codechallenge.domain.GameEvent`) - stores game history

## Dictionary
Here are the key domain concepts and terms used across the application:

- **GameSession** (`com.justeattakeaway.codechallenge.services.GameSession`) - Service that coordinates the game flow, maintains
consistency through event sourcing, and tracks active games.

- **Player** (`com.justeattakeaway.codechallenge.services.Player`) - Service that knows how to play the game and make moves
using a strategy pattern.

- **Move** (`com.justeattakeaway.codechallenge.domain.Move`) - Value Object that represents a single move in the game,
containing the result and the value added.

- **MoveStrategy** (`com.justeattakeaway.codechallenge.domain.MoveStrategy`) - Contains the core game logic for calculating
valid moves based on the current game state.

- **GameRules** (`com.justeattakeaway.codechallenge.domain.GameRules`) - Domain service that enforces game rules and validates moves.

- **GameEvent** (`com.justeattakeaway.codechallenge.domain.GameEvent`) - An Object representing something that happened
in the game (move made, game finished). Used for event sourcing.

- **GameResult** (`com.justeattakeaway.codechallenge.domain.GameResult`) - Value Object containing the final state of a game
(status, winning player).

- **Result** - Current value in the game at a certain time. Must be a whole number and is part of *Move* and *GameEvent*.

- **Added** - A value (-1, 0 or +1) that a player adds to the *Result* to make it divisible by 3. Part of *Move* and *GameEvent*.


## How to Run

### Requirements
- Java 21
- Maven
- Docker and Docker Compose (for MongoDB and Kafka)

### Building and Running the Game

The game is a web application where players communicate via events. You only need to run one server instance.

1. Start MongoDB and Kafka:
```
docker-compose up -d
```

2. Build and run the application:
```
mvn clean install
mvn spring-boot:run
```

The server will start on port 8080 by default.

### Playing the Game

To start a new game, open your web browser and use one of these URLs:

1. Start a game with a specific number:
```
http://localhost:8080/play/56
```

2. Start a game with a random number (between 2 and 1000):
```
http://localhost:8080/play
```

The game result will be shown in the browser in JSON format:

```json
{
  "status": "FINISHED",
  "winningPlayer": 1,
  "gameId": "de8e453b-1d70-48b8-9f2f-688d7a16bced"
}
```

The application uses event sourcing to store all game events. For demonstration purposes, these events are also shown in the application logs:

```
===  new game sesion: de8e453b-1d70-48b8-9f2f-688d7a16bced
  Player 1: (   0 +  0) / 3 = 56
  Player 2: (  56 +  1) / 3 = 19
  Player 1: (  19 + -1) / 3 = 6
  Player 2: (   6 +  0) / 3 = 2
  Player 1: (   2 +  1) / 3 = 1
==========================================================
```


## Potential Improvements

The current implementation focuses on event-driven architecture but could be improved from Domain-Driven Design perspective:

1. Introduce a Game aggregate as the main domain object that encapsulates game rules, state management, and coordinates player interactions.

2. Make domain objects richer by moving business logic from utility classes into the corresponding domain entities.

3. Define more specific domain events that better represent game flow and contain only relevant business data.

4. Replace primitive types with proper value objects to better express domain concepts and add type safety.

5. Improve ubiquitous language by renaming technical terms to match game terminology and business concepts.

These changes would make the code more maintainable and better aligned with domain-driven design principles.