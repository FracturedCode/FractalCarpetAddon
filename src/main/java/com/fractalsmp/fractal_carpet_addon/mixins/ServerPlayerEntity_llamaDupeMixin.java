package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntity_llamaDupeMixin extends PlayerEntity {
    public ServerPlayerEntity_llamaDupeMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }
    @Redirect(method="onDisconnect", at = @At(value = "INVOKE", target="Lnet/minecraft/server/network/ServerPlayerEntity;removeAllPassengers()V"))
    private void llamaDupeSwitch(ServerPlayerEntity serverPlayerEntity) {
        if (!FractalSimpleSettings.llamaDupeExploit) {
            serverPlayerEntity.removeAllPassengers();
        }
    }
}
