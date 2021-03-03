package com.txomon.mindustry.clienthostpause;

import arc.Core;
import arc.Events;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.input.Binding;
import mindustry.mod.Mod;

import static arc.Core.scene;
import static mindustry.Vars.state;
import static mindustry.Vars.ui;

public class ClientHostPauseMod extends Mod {
    @Override
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyTap(Binding.pause) && !scene.hasDialog() && !scene.hasKeyboard() && !ui.restart.isShown() && (state.is(GameState.State.paused) || state.is(GameState.State.playing))) {
                Vars.state.serverPaused = state.is(GameState.State.playing);
            }
        });
    }
}
