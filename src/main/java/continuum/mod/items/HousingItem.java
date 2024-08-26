package continuum.mod.items;

import continuum.mod.procedures.DataProvider;
import continuum.mod.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class HousingItem extends Item {
    public static final int HOUSE_MAX_VOLUME = 16 * 16 * 16;
    public static final int HOUSE_MIN_VOLUME = 3 * 3 * 3;

    public HousingItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        BlockPos bp = context.getClickedPos();
        if (level.isClientSide()) return super.useOn(context);
        if (hand != InteractionHand.MAIN_HAND) return super.useOn(context);

        int x = bp.getX();
        int y = bp.getY();
        int z = bp.getZ();

        BlockPos hbp = new BlockPos(x, y + 1, z);

        if (query(level, x, y, z)) {
            level.getCapability(DataProvider.DATA).ifPresent(data -> {
                data.setHousePos(hbp);
                player.sendSystemMessage(Component.literal("NEW HOUSE POS " + hbp.getX() + " " + hbp.getY() + " " + hbp.getZ()));
            });
        }
        else if (level.getBlockState(hbp).getMaterial().isSolid()) player.sendSystemMessage(Component.literal("This is a solid block!"));
        else player.sendSystemMessage(Component.literal("This is not suitable housing."));


        return super.useOn(context);
    }

    public static BlockPos b(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    public static boolean inBounds(BlockPos p) {
        return p.getY() >= -64 && p.getY() < 256;
    }

    public static int bfs(Level world, int sx, int sy, int sz) {
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
        while (!q.isEmpty() && set.size() < HOUSE_MAX_VOLUME + 10) {
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

        if (set.size() <= HOUSE_MAX_VOLUME) return set.size();
        return -1;
    }

    public static boolean inbfs(Level world, int sx, int sy, int sz, HashSet<Block> target) {
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
        while (!q.isEmpty() && set.size() < HOUSE_MAX_VOLUME + 10) {
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

    public static boolean outbfs(Level world, int sx, int sy, int sz, HashSet<Block> target) {
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
        while (!q.isEmpty() && set.size() < HOUSE_MAX_VOLUME + 10) {
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

    public static boolean posbfs(Level world, int sx, int sy, int sz, BlockPos end, boolean debug) {
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
        while (!q.isEmpty() && set.size() < HOUSE_MAX_VOLUME * 2) {
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

    public static boolean query(Level world, int x, int y, int z) {
        if (y < -64 || y >= 256) return false;
        System.out.println(x + " " + y + " " + z);
        int volume = bfs(world, x, y + 1, z);
        System.out.println("VOLUME " + volume);

        boolean hasDoor = outbfs(world, x, y + 1, z, Reference.DOORS);
        System.out.println("HOUSE HAS DOOR = " + hasDoor);
        boolean hasLightSource = inbfs(world, x, y + 1, z, Reference.LIGHTS) || outbfs(world, x, y + 1, z, Reference.LIGHTS);
        System.out.println("HOUSE HAS LIGHT SOURCE = " + hasLightSource);

        System.out.println("WORLD HEX CODE = " + world.toString());

        return (volume >= HOUSE_MIN_VOLUME && volume <= HOUSE_MAX_VOLUME && hasDoor && hasLightSource);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        AtomicInteger hx = new AtomicInteger(0);
        AtomicInteger hy = new AtomicInteger(-9000);
        AtomicInteger hz = new AtomicInteger(0);

        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            hx.set(data.getX());
            hy.set(data.getY());
            hz.set(data.getZ());
        });

        level.addParticle(ParticleTypes.END_ROD, hx.get() + 0.5, hy.get() + 0.5, hz.get() + 0.5, 0, 0, 0);
        for (int i = -64; i < 256; i++) {
            if (i == hy.get()) continue;
            level.addParticle(ParticleTypes.FLAME, hx.get() + 0.5, i + 0.5, hz.get() + 0.5, 0, 0, 0);
        }

        System.out.println("[" + hx + " " + hy + " " + hz + "]");

        return super.use(level, player, hand);
    }
}