package com.fractalsmp.fractal_carpet_addon;

import com.google.common.collect.Maps;
import net.minecraft.server.world.ServerWorld;

import java.util.Map;

public interface IDimensionThreadCache {
    Map<ServerWorld, DimensionalThread> worldThreadHashset = Maps.newIdentityHashMap();

    default void newServerWorld(Object uncastWorld) {
        ServerWorld world = (ServerWorld)uncastWorld; // Is all of this safe??? Who knows
        DimensionalThread dimensionalThread;
        if ((dimensionalThread = worldThreadHashset.get(world)) != null) {
            dimensionalThread.stop();
        }
        dimensionalThread = new DimensionalThread(world);
        worldThreadHashset.put(world, dimensionalThread);
        dimensionalThread.start();
    }
}
