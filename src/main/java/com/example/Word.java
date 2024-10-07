package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/game")
public class Word {
    private static final Map<Integer, GameData> games = new HashMap<>();
    
    static {
        initializeGames();
    }

    private static void initializeGames() {
        games.clear();
        games.put(1, new GameData("apple", "fruit"));
        games.put(2, new GameData("java", "programming language"));
        games.put(3, new GameData("paris", "city"));
        games.put(4, new GameData("elephant", "animal"));
        games.put(5, new GameData("pizza", "food"));
        games.put(6, new GameData("guitar", "musical instrument"));
        games.put(7, new GameData("oxygen", "element"));
        games.put(8, new GameData("mars", "planet"));
        games.put(9, new GameData("soccer", "sport"));
        games.put(10, new GameData("shakespeare", "author"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, String>> getGameData(@PathVariable int id) {
        if (id < 1 || id > 10) {
            return ResponseEntity.badRequest().body(null);
        }
        GameData gameData = games.get(id);
        Map<String, String> response = new HashMap<>();
        response.put("firstLetter", String.valueOf(gameData.getWord().charAt(0)));
        response.put("wordLength", String.valueOf(gameData.getWord().length()));
        response.put("category", gameData.getCategory());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/guess")
    public ResponseEntity<Map<String, Object>> guessWord(@PathVariable int id, @RequestBody Map<String, String> guess) {
        if (id < 1 || id > 10) {
            return ResponseEntity.badRequest().body(null);
        }
        GameData gameData = games.get(id);
        String guessedWord = guess.get("word");
        gameData.addGuess(guessedWord);
        boolean isCorrect = gameData.getWord().equalsIgnoreCase(guessedWord);
        
        Map<String, Object> response = new HashMap<>();
        response.put("correct", isCorrect);
        response.put("message", isCorrect ? "Congratulations! You guessed correctly." : "Sorry, that's not correct. Try again!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/guesses")
    public ResponseEntity<List<String>> getGuesses(@PathVariable int id) {
        if (id < 1 || id > 10) {
            return ResponseEntity.badRequest().body(null);
        }
        GameData gameData = games.get(id);
        return ResponseEntity.ok(gameData.getGuesses());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, String>> updateWord(@PathVariable int id, @RequestBody GameData newGameData) {
        if (id < 1 || id > 10) {
            return ResponseEntity.badRequest().body(null);
        }
        games.put(id, newGameData);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Word updated successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteWord(@PathVariable int id) {
        if (id < 1 || id > 10) {
            return ResponseEntity.badRequest().body(null);
        }
        games.remove(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Word deleted successfully");
        return ResponseEntity.ok(response);
    }
    @PostMapping("/reset")
    public ResponseEntity<Map<String, String>> resetGame() {
        initializeGames();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All games have been reset successfully");
        return ResponseEntity.ok(response);
    }

    private static class GameData {
        private String word;
        private String category;
        private List<String> guesses;

        public GameData(String word, String category) {
            this.word = word;
            this.category = category;
            this.guesses = new ArrayList<>();
        }

        public String getWord() {
            return word;
        }

        public String getCategory() {
            return category;
        }

        public void addGuess(String guess) {
            guesses.add(guess);
        }

        public List<String> getGuesses() {
            return guesses;
        }
    }
}