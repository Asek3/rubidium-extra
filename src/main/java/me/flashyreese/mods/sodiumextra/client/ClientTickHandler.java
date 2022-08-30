package me.flashyreese.mods.sodiumextra.client;

import me.flashyreese.mods.sodiumextra.common.util.EvictingQueue;
import me.flashyreese.mods.sodiumextra.mixin.gui.MinecraftClientAccessor;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;

import java.util.Comparator;
import java.util.Queue;

public class ClientTickHandler {
    private int averageFps, lowestFps, highestFps;
    private final Queue<Integer> fpsQueue = new EvictingQueue<>(200);

    public void onClientInitialize() {
        MinecraftForge.EVENT_BUS.addListener(this::onTick);
    }

    public void onTick(TickEvent.ClientTickEvent event) {
        int currentFPS = MinecraftClientAccessor.getCurrentFPS();
        this.fpsQueue.add(currentFPS);
        this.averageFps = (int) this.fpsQueue.stream().mapToInt(Integer::intValue).average().orElse(0);
        this.lowestFps = this.fpsQueue.stream().min(Comparator.comparingInt(e -> e)).orElse(0);
        this.highestFps = this.fpsQueue.stream().max(Comparator.comparingInt(e -> e)).orElse(0);
    }

    public int getAverageFps() {
        return this.averageFps;
    }

    public int getLowestFps() {
        return this.lowestFps;
    }

    public int getHighestFps() {
        return this.highestFps;
    }
}
