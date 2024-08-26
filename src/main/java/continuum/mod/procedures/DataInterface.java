package continuum.mod.procedures;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface DataInterface extends INBTSerializable<CompoundTag> {
    boolean getExists();
    void getExists(boolean value);

    int getX();
    void setX(int value);
    int getY();
    void setY(int value);
    int getZ();
    void setZ(int value);
}
