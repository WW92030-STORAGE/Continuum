package continuum.mod.items;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.TreeSet;

import continuum.mod.Main;
import continuum.mod.data_housing.WorldData;
import continuum.mod.init.ItemInit;
import continuum.mod.util.IModel;
import continuum.mod.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemHousing extends Item implements IModel {
	public static final int HOUSE_MAX_VOLUME = 16 * 16 * 16;
	public static final int HOUSE_MIN_VOLUME = 3 * 3 * 3;
	
	private int hx, hz;
	private int hy = -1;
	
	public ItemHousing(String name) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(CreativeTabs.MISC);
		
		ItemInit.ITEMS.add(this);
	}
	
	@Override
	public void registerModels() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
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
		
		IBlockState ib = world.getBlockState(b(sx, sy, sz));
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
				IBlockState ibs = world.getBlockState(next);
				Block b = ibs.getBlock();
				if (!b.getMaterial(ibs).isSolid()) {
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
	
	public static boolean inbfs(World world, int sx, int sy, int sz, HashSet<Block> target) {
		System.out.println("BEGIN DETECTION BFS FROM " + sx + " " + sy + " " + sz);
		
		TreeSet<BlockPos> set = new TreeSet<BlockPos>();
		Queue<BlockPos> q = new LinkedList<BlockPos>();
		int x, y, z, xp, yp, zp;
		
		int[] dx = {01, 00, 00, -1, 00, 00};
		int[] dy = {00, 01, 00, 00, -1, 00};
		int[] dz = {00, 00, 01, 00, 00, -1};
		
		IBlockState ib = world.getBlockState(b(sx, sy, sz));
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
				IBlockState ibs = world.getBlockState(next);
				Block b = ibs.getBlock();
				if (!b.getMaterial(ibs).isSolid()) {
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
		
		IBlockState ib = world.getBlockState(b(sx, sy, sz));
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
				IBlockState ibs = world.getBlockState(next);
				Block b = ibs.getBlock();
				if (!b.getMaterial(ibs).isSolid()) {
					if (!set.contains(next)) {
						set.add(next);
						q.add(next);
					}
				}
				else perimeter.add(next);
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
		
		IBlockState ib = world.getBlockState(b(sx, sy, sz));
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
				IBlockState ibs = world.getBlockState(next);
				Block b = ibs.getBlock();
				if (!b.getMaterial(ibs).isSolid()) {
					if (!set.contains(next)) {
						set.add(next);
						q.add(next);
					}
				}
			}
		}
		
		return set.contains(end);
	}
	
	static boolean unused = false;
	
	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int slot, boolean b) {
		WorldData wd = WorldData.get(world);
		
		if (entity instanceof EntityLivingBase && ((EntityLivingBase) entity).getHeldItemMainhand().equals(itemstack)) {
			world.spawnParticle(EnumParticleTypes.END_ROD, wd.getX() + 0.5, wd.getY() + 0.5, wd.getZ() + 0.5, 0, 0, 0);
			for (int i = 0; i < 256; i++) {
				if (i == wd.getY()) continue;
				world.spawnParticle(EnumParticleTypes.REDSTONE, wd.getX() + 0.5, i + 0.5, wd.getZ() + 0.5, 0, 0, 0);
			}
		}
	}
	
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer entity, World world, BlockPos pos, EnumFacing direction, float hitX, float hitY,
			float hitZ, EnumHand hand) {
		EnumActionResult res = super.onItemUseFirst(entity, world, pos, direction, hitX, hitY, hitZ, hand);
		ItemStack itemstack = entity.getHeldItem(hand);
		
		if (unused) return res;
		if (entity.dimension != 0) {
			x("This dimension is not suitable for housing!", entity);
			return res;
		}
		
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		
		if (query(world, x, y, z)) {
			WorldData.get(world).setHousePos(x, y + 1, z);
			hx = x;
			hy = y + 1;
			hz = z;
			x("[" + hx + ", " + hy + ", " + hz + "]", entity);
		}
		else {
			if (world.getBlockState(new BlockPos(x, y + 1, z)).getMaterial().isSolid()) x("This is a solid block!", entity);
			else x("This is not suitable housing.", entity);
		}
		
		return res;
	}
	
	public static boolean query(World world, int x, int y, int z) {
		if (y < 0 || y >= 256) return false;
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
	
	public static void x(String s, EntityPlayer e) {
		e.sendStatusMessage(new TextComponentString("" + s), (false));
	}
}
