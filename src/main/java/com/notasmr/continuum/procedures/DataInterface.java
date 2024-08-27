package com.notasmr.continuum.procedures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface DataInterface extends INBTSerializable<CompoundNBT> {
    boolean getExists();
    void getExists(boolean value);

    int getX();
    void setX(int value);
    int getY();
    void setY(int value);
    int getZ();
    void setZ(int value);
}
