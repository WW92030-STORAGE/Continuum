package com.notasmr.continuum.procedures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DataProvider implements ICapabilitySerializable<INBT> {
    private WorldData data = new WorldData();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (CapabilityWorldData.DATA == capability) return (LazyOptional<T>) LazyOptional.of(() -> data);
        return LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("continuum_data", CapabilityWorldData.DATA.writeNBT(data, null));
        return nbt;
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        if (nbt.getType() != CompoundNBT.TYPE) return;

        CompoundNBT cnbt = (CompoundNBT)nbt;
        INBT nbt2 = cnbt.get("continuum_data");
        CapabilityWorldData.DATA.readNBT(data, null, nbt2);

    }

}
