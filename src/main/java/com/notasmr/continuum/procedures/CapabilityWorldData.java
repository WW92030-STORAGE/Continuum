package com.notasmr.continuum.procedures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class CapabilityWorldData {
    @CapabilityInject(WorldData.class)
    public static Capability<WorldData> DATA = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(
                WorldData.class,
                new WorldData.WorldDataNBTStorage(),
                WorldData::construct
        );
    }
}
