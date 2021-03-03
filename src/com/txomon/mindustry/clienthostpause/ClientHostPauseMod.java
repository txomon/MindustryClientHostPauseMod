package com.txomon.mindustry.clienthostpause;

import arc.Events;
import arc.util.Log;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class ClientHostPauseMod extends Mod {
    @Override
    public void init() {
        Events.run(EventType.ResetEvent.class, this::patchGameState);
    }
    public void patchGameState() {
        if (!(Vars.state instanceof PausableGameState)) {
            PausableGameState newState = PausableGameState.copyFrom(Vars.state);
            Log.info("Trying to replace object @ with @", Vars.state, newState);
            Vars.state = newState;
        }
    }
}
