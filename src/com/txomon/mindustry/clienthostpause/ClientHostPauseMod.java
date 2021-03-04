package com.txomon.mindustry.clienthostpause;

import arc.Core;
import arc.Events;
import arc.util.Time;
import arc.util.Log;
import mindustry.Vars;
import mindustry.core.GameState;
import mindustry.game.EventType;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.input.Binding;
import mindustry.mod.Mod;

import static arc.Core.scene;
import static mindustry.Vars.state;
import static mindustry.Vars.ui;

public class ClientHostPauseMod extends Mod {
    private long lastForceSync = 0L;
    @Override
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            // This is something only the host should be able to do
            if (!Vars.net.server()) {
                return;
            }

            // If statement copied from https://github.com/Anuken/Mindustry/blob/d73cf9fcff026a581b06c0aaab89a9c3231d3310/core/src/mindustry/core/Control.java#L540
            if (Core.input.keyTap(Binding.pause) && !scene.hasDialog() && !scene.hasKeyboard() && !ui.restart.isShown() && (state.is(GameState.State.paused) || state.is(GameState.State.playing))) {
                // Boolean flip
                state.serverPaused = !state.serverPaused;
            }

            // If server is playing, no need to push changes, as sendWorldData() will reset the view
            if (!state.serverPaused) {
                return;
            }

            // Don't update always
            if (Time.timeSinceMillis(lastForceSync) < 2000L) {
                return;
            }
            lastForceSync = Time.millis();

            Groups.player.update();
            for (Player player : Groups.player){
                if (player.isLocal()) {
                    continue;
                }
                try {
                    player.getInfo().lastSyncTime = Time.millis();
                } catch (IllegalArgumentException e) {
                    Log.err("Programming error: Failed to set lastSyncTime of player @ on pause/resume", player);
                }
                Vars.netServer.sendWorldData(player);
            }
        });
    }
}
