package com.txomon.mindustry.clienthostpause;

import arc.*;
import arc.util.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.mod.*;

import java.io.*;

import static arc.Core.scene;
import static mindustry.Vars.*;

public class ClientHostPauseMod extends Mod {
    @Override
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            // This is something only the host should be able to do
            if (!Vars.net.server()) {
                return;
            }

            // If statement copied from https://github.com/Anuken/Mindustry/blob/d73cf9fcff026a581b06c0aaab89a9c3231d3310/core/src/mindustry/core/Control.java#L540
            if (Core.input.keyTap(Binding.pause) && !scene.hasDialog() && !scene.hasKeyboard() && !ui.restart.isShown() && (state.is(GameState.State.paused) || state.is(GameState.State.playing))){
                // Boolean flip
                state.serverPaused = !state.serverPaused;
                // Because GameState updates are not sent through (not even to the host), call the smallest function that replicates GameState
                for(Player player : Groups.player){
                    if(player.isLocal()){
                        continue;
                    }
                    try{
                        netServer.writeEntitySnapshot(player);
                    }catch(IOException e){
                        Log.err("Failed to notify player @ of server pause/resume", player);
                    }
                }
            }
        });
    }
}
