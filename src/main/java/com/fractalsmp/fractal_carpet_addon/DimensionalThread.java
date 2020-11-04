package com.fractalsmp.fractal_carpet_addon;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;

import java.util.Random;
import java.util.function.BooleanSupplier;

public class DimensionalThread implements Runnable {
    private ServerWorld world;
    private boolean needsTick;
    private boolean stop;
    private BooleanSupplier shouldKeepTicking;
    private Thread t;

    public DimensionalThread(ServerWorld serverWorld) {
        world = serverWorld;
    }

    @Override
    public void run() {
        while(!stop) {//TODO how to handle switching the feature on and off
            if (needsTick) {
                try {
                    world.tick(shouldKeepTicking);
                } catch (Throwable var6) {
                    CrashReport crashReport = CrashReport.create(var6, "Exception ticking world");
                    world.addDetailsToCrashReport(crashReport);
                    throw new CrashException(crashReport);
                }
                needsTick = false;
                if (!shouldKeepTicking.getAsBoolean()) {
                    stop = true;
                }
            }
        }
    }

    public void start() {
        if (t == null) {
            t = new Thread(this, "serverWorld: " + world.dimension.getType().toString() + new Random().nextInt(9));
            t.start();
        }
    }

    public void stop() {
        stop = true;
    }

    public void tick(BooleanSupplier shouldKeep) {
        needsTick = true;
        shouldKeepTicking = shouldKeep;
    }

    public boolean needsTick() {
        return needsTick;
    }
}
