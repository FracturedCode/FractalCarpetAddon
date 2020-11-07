package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntity_noObsidianPlatformMixin extends PlayerEntity {

    public ServerPlayerEntity_noObsidianPlatformMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method="createEndSpawnPlatform", at = @At("HEAD"), cancellable = true)
    private void cancelEndPlatformCreate(ServerWorld world, BlockPos centerPos, CallbackInfo ci) {
        if (FractalSimpleSettings.noObsidianPlatform) {
            ci.cancel();
        }
    }
}