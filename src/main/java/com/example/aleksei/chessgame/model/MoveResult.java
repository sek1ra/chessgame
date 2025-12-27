package com.example.aleksei.chessgame.model;

public record MoveResult(boolean moved, GameStatus status, Boolean winnerIsWhite) {}