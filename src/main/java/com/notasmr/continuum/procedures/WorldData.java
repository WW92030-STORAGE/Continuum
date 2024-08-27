package com.notasmr.continuum.procedures;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;

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

    public static WorldData construct() {
        return new WorldData();
    }

    public static class WorldDataNBTStorage implements Capability.IStorage<WorldData> {
        @Override
        public INBT writeNBT(Capability<WorldData> capability, WorldData ins, Direction side) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putInt("hx", ins.hx);
            nbt.putInt("hy", ins.hy);
            nbt.putInt("hz", ins.hz);
            nbt.putBoolean("NPCExists", ins.NPCExists);
            return nbt;
        }

        @Override
        public void readNBT(Capability<WorldData> capability, WorldData instance, Direction side, INBT nbt) {
            int hx = 0;
            int hy = -9000;
            int hz = 0;
            boolean exists = false;
            if (nbt.getType() == CompoundNBT.TYPE) {
                hx = ((CompoundNBT)nbt).getInt("hx");
                hy = ((CompoundNBT)nbt).getInt("hy");
                hz = ((CompoundNBT)nbt).getInt("hz");
                exists = ((CompoundNBT)nbt).getBoolean("NPCExists");
            }

            instance.setHousePos(hx, hy, hz);
            instance.setNPC(exists);
        }
    }

    public void saveNBTData(CompoundNBT nbt) {
        nbt.putInt("hx", hx);
        nbt.putInt("hy", hy);
        nbt.putInt("hz", hz);
        nbt.putBoolean("NPCExists", NPCExists);
    }

    public void loadNBTData(CompoundNBT nbt) {
        hx = nbt.getInt("hx");
        hy = nbt.getInt("hy");
        hz = nbt.getInt("hz");
        NPCExists = nbt.getBoolean("NPCExists");
    }
}
