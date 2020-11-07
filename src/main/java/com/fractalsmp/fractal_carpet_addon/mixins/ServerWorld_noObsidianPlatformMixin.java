package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorld_noObsidianPlatformMixin extends World {

    protected ServerWorld_noObsidianPlatformMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DimensionType dimensionType, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed) {
        super(properties, registryRef, dimensionType, profiler, isClient, debugWorld, seed);
    }

    @Inject(method="createEndSpawnPlatform", at = @At("HEAD"), cancellable = true)
    private static void cancelEndPlatformCreate(ServerWorld world, CallbackInfo ci) {
        if (FractalSimpleSettings.noObsidianPlatform) {
            ci.cancel();
        }
    }
}
