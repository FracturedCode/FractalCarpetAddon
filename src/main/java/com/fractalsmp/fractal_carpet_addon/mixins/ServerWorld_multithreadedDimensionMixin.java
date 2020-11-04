package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.DimensionalThread;
import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.fractalsmp.fractal_carpet_addon.IDimensionThreadCache;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.level.LevelProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorld_multithreadedDimensionMixin extends World {
    @Shadow @Final private MinecraftServer server;

    protected ServerWorld_multithreadedDimensionMixin(LevelProperties levelProperties, DimensionType dimensionType, BiFunction<World, Dimension, ChunkManager> chunkManagerProvider, Profiler profiler, boolean isClient) {
        super(levelProperties, dimensionType, chunkManagerProvider, profiler, isClient);
    }
    @Inject(method="<init>*", at = @At("RETURN"))
    private void addWorldToThreadStuff(MinecraftServer server, Executor workerExecutor, WorldSaveHandler worldSaveHandler, LevelProperties properties, DimensionType dimensionType, Profiler profiler, WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
        // we COULD inject where MinecraftServer creates a ServerWorld but idk if that's the only things that create these
        // and in the interest in compatibility, by injecting the constructor we should catch all cases.
        // It just required a little extra cast and interface work.
        ((IDimensionThreadCache)server).newServerWorld(this);

    }
    /*@Inject(method="tick", at = @At("HEAD"), cancellable = true)
    private void queueThreadTick(BooleanSupplier shouldKeepTicking, CallbackInfo ci) {
        DimensionalThread dt;
        if (FractalSimpleSettings.multithreadedDimensions
                && !(dt = ((IDimensionThreadCache)server).worldThreadHashset.get(this)).needsTick()) {
            dt.tick(shouldKeepTicking);
            ci.cancel();
        }
    }*/
}
