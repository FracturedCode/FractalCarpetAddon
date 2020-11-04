package com.fractalsmp.fractal_carpet_addon.mixins;

import com.fractalsmp.fractal_carpet_addon.FractalSimpleSettings;
import com.mojang.datafixers.util.Either;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.thread.ThreadExecutor;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkManager;
import net.minecraft.world.chunk.ChunkStatus;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerChunkManager.class)
public abstract class ServerChunkManager_multithreadedDimensionMixin extends ChunkManager {
    @Shadow @Final private Thread serverThread;

    @Redirect(
            method="getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/server/world/ServerChunkManager;serverThread:Ljava/lang/Thread;",
                    opcode = Opcodes.GETFIELD
            )
    )
    private Thread forceServerThread(ServerChunkManager serverChunkManager) {
        if (FractalSimpleSettings.multithreadedDimensions) {
            return Thread.currentThread();
        } else {
            return serverThread;
        }
    }
}
