package com.notasmr.continuum.items;

import com.notasmr.continuum.procedures.CapabilityWorldData;
import com.notasmr.continuum.procedures.DataProvider;
import com.notasmr.continuum.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

public class HousingItem extends Item {
    public HousingItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        World level = context.getLevel();
        PlayerEntity player = context.getPlayer();
        Hand hand = context.getHand();
        if(level.isClientSide() || hand != Hand.MAIN_HAND) return super.useOn(context);

        BlockPos bp = context.getClickedPos();
        if (level.isClientSide()) return super.useOn(context);
        if (hand != Hand.MAIN_HAND) return super.useOn(context);

        int x = bp.getX();
        int y = bp.getY();
        int z = bp.getZ();

        BlockPos hbp = new BlockPos(x, y + 1, z);

        if (query(level, x, y, z)) {
            level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
                data.setHousePos(hbp);
                Reference.disp("NEW HOUSE POS " + hbp.getX() + " " + hbp.getY() + " " + hbp.getZ(), player);
            });
        }
        else if (level.getBlockState(hbp).getMaterial().isSolid()) Reference.disp("This is a solid block!", player);
        else Reference.disp("This is not suitable housing.", player);

        return super.useOn(context);
    }

    public static BlockPos b(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public static boolean inBounds(BlockPos p) {
        return p.getY() >= 0 && p.getY() < 256;
    }

    public static int bfs(World world, int sx, int sy, int sz) {
        System.out.println("BEGIN VOLUME BFS FROM " + sx + " " + sy + " " + sz);

        TreeSet<BlockPos> set = new TreeSet<BlockPos>();
        Queue<BlockPos> q = new LinkedList<BlockPos>();
        int x, y, z, xp, yp, zp;

        int[] dx = {01, 00, 00, -1, 00, 00};
        int[] dy = {00, 01, 00, 00, -1, 00};
        int[] dz = {00, 00, 01, 00, 00, -1};

        BlockState ib = world.getBlockState(b(sx, sy, sz));
        if (ib.getMaterial().isSolid()) return -1;

        q.add(b(sx, sy, sz));
        while (!q.isEmpty() && set.size() < Reference.HOUSE_MAX_VOLUME + 10) {
            BlockPos pos = q.poll();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            //	System.out.println(x + " " + y + " " + z + " = " + q);
            for (int i = 0; i < 6; i++) {
                xp = x + dx[i];
                yp = y + dy[i];
                zp = z + dz[i];
                BlockPos next = b(xp, yp, zp);
                if (!inBounds(next)) continue;
                BlockState ibs = world.getBlockState(next);
                if (!ibs.getMaterial().isSolid()) {
                    if (!set.contains(next)) {
                        set.add(next);
                        q.add(next);
                    }
                }
            }
        }

        if (set.size() <= Reference.HOUSE_MAX_VOLUME) return set.size();
        return -1;
    }

    public static boolean inbfs(World world, int sx, int sy, int sz, HashSet<Block> target) {
        System.out.println("BEGIN DETECTION BFS FROM " + sx + " " + sy + " " + sz);

        TreeSet<BlockPos> set = new TreeSet<BlockPos>();
        Queue<BlockPos> q = new LinkedList<BlockPos>();
        int x, y, z, xp, yp, zp;

        int[] dx = {01, 00, 00, -1, 00, 00};
        int[] dy = {00, 01, 00, 00, -1, 00};
        int[] dz = {00, 00, 01, 00, 00, -1};

        BlockState ib = world.getBlockState(b(sx, sy, sz));
        if (ib.getMaterial().isSolid()) return false;

        q.add(b(sx, sy, sz));
        while (!q.isEmpty() && set.size() < Reference.HOUSE_MAX_VOLUME + 10) {
            BlockPos pos = q.poll();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            //	System.out.println(x + " " + y + " " + z + " = " + q);
            for (int i = 0; i < 6; i++) {
                xp = x + dx[i];
                yp = y + dy[i];
                zp = z + dz[i];
                BlockPos next = b(xp, yp, zp);
                if (!inBounds(next)) continue;
                BlockState ibs = world.getBlockState(next);
                if (!ibs.getMaterial().isSolid()) {
                    if (!set.contains(next)) {
                        set.add(next);
                        q.add(next);
                    }
                }
            }
        }

        for (BlockPos pos : set) {
            Block b = world.getBlockState(pos).getBlock();
            if (target.contains(b)) return true;
        }
        return false;
    }

    public static boolean outbfs(World world, int sx, int sy, int sz, HashSet<Block> target) {
        System.out.println("BEGIN PERIMETER BFS FROM " + sx + " " + sy + " " + sz);

        TreeSet<BlockPos> set = new TreeSet<BlockPos>();
        TreeSet<BlockPos> perimeter = new TreeSet<BlockPos>();
        Queue<BlockPos> q = new LinkedList<BlockPos>();
        int x, y, z, xp, yp, zp;

        int[] dx = {01, 00, 00, -1, 00, 00};
        int[] dy = {00, 01, 00, 00, -1, 00};
        int[] dz = {00, 00, 01, 00, 00, -1};

        BlockState ib = world.getBlockState(b(sx, sy, sz));
        if (ib.getMaterial().isSolid()) return false;

        q.add(b(sx, sy, sz));
        while (!q.isEmpty() && set.size() < Reference.HOUSE_MAX_VOLUME + 10) {
            BlockPos pos = q.poll();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            //	System.out.println(x + " " + y + " " + z + " = " + perimeter);
            for (int i = 0; i < 6; i++) {
                xp = x + dx[i];
                yp = y + dy[i];
                zp = z + dz[i];
                BlockPos next = b(xp, yp, zp);
                if (!inBounds(next)) continue;
                BlockState ibs = world.getBlockState(next);
                if (!ibs.getMaterial().isSolid()) {
                    if (!set.contains(next)) {
                        set.add(next);
                        q.add(next);
                    }
                } else perimeter.add(next);
            }
        }

        for (BlockPos pos : perimeter) {
            Block b = world.getBlockState(pos).getBlock();
            if (target.contains(b)) return true;
        }
        return false;
    }

    public static boolean posbfs(World world, int sx, int sy, int sz, BlockPos end, boolean debug) {
        if (debug) System.out.println("BEGIN DETECTION BFS FROM " + sx + " " + sy + " " + sz);

        TreeSet<BlockPos> set = new TreeSet<BlockPos>();
        Queue<BlockPos> q = new LinkedList<BlockPos>();
        int x, y, z, xp, yp, zp;

        int[] dx = {01, 00, 00, -1, 00, 00};
        int[] dy = {00, 01, 00, 00, -1, 00};
        int[] dz = {00, 00, 01, 00, 00, -1};

        BlockState ib = world.getBlockState(b(sx, sy, sz));
        if (ib.getMaterial().isSolid()) return false;

        q.add(b(sx, sy, sz));
        while (!q.isEmpty() && set.size() < Reference.HOUSE_MAX_VOLUME * 2) {
            BlockPos pos = q.poll();
            x = pos.getX();
            y = pos.getY();
            z = pos.getZ();
            //	System.out.println(x + " " + y + " " + z + " = " + q);
            for (int i = 0; i < 6; i++) {
                xp = x + dx[i];
                yp = y + dy[i];
                zp = z + dz[i];
                BlockPos next = b(xp, yp, zp);
                if (!inBounds(next)) continue;
                BlockState ibs = world.getBlockState(next);
                if (!ibs.getMaterial().isSolid()) {
                    if (!set.contains(next)) {
                        set.add(next);
                        q.add(next);
                    }
                }
            }
        }

        return set.contains(end);
    }

    public static boolean query(World world, int x, int y, int z) {
        if (y < -64 || y >= 256) return false;
        System.out.println(x + " " + y + " " + z);
        int volume = bfs(world, x, y + 1, z);
        System.out.println("VOLUME " + volume);

        boolean hasDoor = outbfs(world, x, y + 1, z, Reference.DOORS);
        System.out.println("HOUSE HAS DOOR = " + hasDoor);
        boolean hasLightSource = inbfs(world, x, y + 1, z, Reference.LIGHTS) || outbfs(world, x, y + 1, z, Reference.LIGHTS);
        System.out.println("HOUSE HAS LIGHT SOURCE = " + hasLightSource);

        System.out.println("WORLD HEX CODE = " + world.toString());

        return (volume >= Reference.HOUSE_MIN_VOLUME && volume <= Reference.HOUSE_MAX_VOLUME && hasDoor && hasLightSource);
    }
}
