package com.txomon.mindustry.clienthostpause;

import arc.Events;
import arc.util.Log;
import mindustry.core.GameState;
import mindustry.game.EventType;

import java.lang.reflect.Field;

public class PausableGameState extends GameState {

    static public PausableGameState copyFrom(GameState gameState) {
        PausableGameState pgs = new PausableGameState();
        pgs.wave = gameState.wave;
        pgs.wavetime = gameState.wavetime;
        pgs.gameOver = gameState.gameOver;
        pgs.serverPaused = gameState.serverPaused;
        pgs.wasTimeout = gameState.wasTimeout;
        pgs.map = gameState.map;
        pgs.rules = gameState.rules;
        pgs.stats = gameState.stats;
        pgs.envAttrs = gameState.envAttrs;
        pgs.teams = gameState.teams;
        pgs.enemies = gameState.enemies;
        pgs.setState(gameState.getState());
        return pgs;
    }

    protected void setState(State newState) {
        try {
            Field field = GameState.class.getDeclaredField("state");
            field.setAccessible(true);
            field.set(this, newState);
        } catch (IllegalAccessException e) {
            Log.err("Failed to update private state of GameState");
        } catch (NoSuchFieldException e) {
            Log.err("Failed to find field state inside @", this);
        }

    }


    @Override
    public void set(State newState) {
        Log.info("Changing state from @ to @", getState(), newState);
        Events.fire(new EventType.StateChangeEvent(getState(), newState));
        setState(newState);
    }
}
