package continuum.mod.procedures;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class WorldData {
    private boolean NPCExists;
    private int hx;
    private int hy = -9000;
    private int hz;

    public void setNPC(boolean b) {
        this.NPCExists = b;
        System.out.println("NPC exists is now = " + this.getNPC());
    }

    public void setHousePos(int x, int y, int z) {
        this.hx = x;
        this.hy = y;
        this.hz = z;
        System.out.println("NEW HOUSE POSITION = [" + this.hx + ", " + this.hy + ", " + this.hz + "]");
    }

    public void setHousePos(BlockPos bp) {
        this.setHousePos(bp.getX(), bp.getY(), bp.getZ());
    }

    public BlockPos getHousePos() {
        return new BlockPos(hx, hy, hz);
    }

    public void setX(int x) {
        this.hx = x;
    }

    public void setY(int y) {
        this.hy = y;
    }

    public void setZ(int z) {
        this.hz = z;
    }
    public boolean getNPC() {
        return this.NPCExists;
    }

    public int getX() {
        return this.hx;
    }

    public int getY() {
        return this.hy;
    }

    public int getZ() {
        return this.hz;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("hx", hx);
        nbt.putInt("hy", hy);
        nbt.putInt("hz", hz);
        nbt.putBoolean("NPCExists", NPCExists);
    }

    public void loadNBTData(CompoundTag nbt) {
        hx = nbt.getInt("hx");
        hy = nbt.getInt("hy");
        hz = nbt.getInt("hz");
        NPCExists = nbt.getBoolean("NPCExists");
    }
}
