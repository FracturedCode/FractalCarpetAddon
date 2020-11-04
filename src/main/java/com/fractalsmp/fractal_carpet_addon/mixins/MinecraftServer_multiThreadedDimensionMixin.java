package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.fractalsmp.fractal_carpet_addon.IDimensionThreadCache;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTask;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.DisableableProfiler;
import net.minecraft.util.thread.ReentrantThreadExecutor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServer_multiThreadedDimensionMixin extends ReentrantThreadExecutor<ServerTask> implements IDimensionThreadCache {
    @Shadow @Final private DisableableProfiler profiler;

    @Shadow public abstract Iterable<ServerWorld> getWorlds();

    public MinecraftServer_multiThreadedDimensionMixin(String name) {
        super(name);
    }

    @Redirect(
            method = "tickWorlds",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/world/ServerWorld;tick(Ljava/util/function/BooleanSupplier;)V"
            )
    )
    public void queueTickInDimensionThread(ServerWorld world, BooleanSupplier shouldKeepTicking) {
        if (FractalSimpleSettings.multithreadedDimensions) {
            worldThreadHashset.get(world).tick(shouldKeepTicking);
        } else {
            world.tick(shouldKeepTicking);
        }
    }

    @Inject(method="tickWorlds", at = @At("RETURN" ))
    protected void joinTicks(CallbackInfo ci) {
        if (FractalSimpleSettings.multithreadedDimensions) {
            while (true) {
                if (worldThreadHashset.values().stream().allMatch(w -> !w.needsTick())) {
                    break;
                }
            }
        }
    }
}
