package continuum.mod.entities;

import java.util.ArrayList;

import continuum.mod.data_housing.WorldData;
import continuum.mod.init.BlockInit;
import continuum.mod.init.EntityInit;
import continuum.mod.init.ItemInit;
import continuum.mod.items.ItemHousing;
import continuum.mod.util.Reference;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.EntityEntry;

public class EntityNPC extends EntityCreature /* implements IRangedAttackMob */ {
//	EntityMob test = new EntityVillager();
//  EntityMob test2 = new EntityZombie();
	private boolean exists = false;
	private static final boolean DEBUG = false;
	private final int rad = 8;
	private final int obs = 32;
	
	static long time = -1;
	static double cooldown = 4;
	static int radius = 16;
	static int duration = 2;
	
	public EntityNPC(World worldIn) {
		super(worldIn);
		if (EntityInit.PERSISTENCE) enablePersistence();
		((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
	}
	
	@Override
	protected void initEntityAI()
    {
		this.tasks.addTask(1, new EntityAITempt(this, 1, new ItemStack(Blocks.GOLD_BLOCK, 1).getItem(), false));
		this.tasks.addTask(1, new EntityAITempt(this, 1, Items.EMERALD, false));
		this.tasks.addTask(1, new EntityAITempt(this, 1, new ItemStack(Blocks.EMERALD_BLOCK, 1).getItem(), false));
	//	for (Class Hostile : Reference.HOSTILE_MOBS) this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, Hostile, true, false));
	//	this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityEndermite.class, true, false));
		this.tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 4.0F));
		this.tasks.addTask(3, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));	
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityPlayer.class, EntityInit.RANGE));
		this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityNPC.class, 4.0F));
		this.tasks.addTask(6, new EntityAIWander(this, 1));
		this.tasks.addTask(7, new EntityAISwimming(this));
		this.tasks.addTask(8, new EntityAILeapAtTarget(this, 0.8f));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		
	//	this.tasks.addTask(1, new EntityAIAttackRanged(this, 1.25D, 20, 10.0F));
		
    }
	
	@Override
	public void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400.0);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(EntityInit.SPEED);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(10);
		if (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) 
			this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(EntityInit.RANGE);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(""));
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource src) {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(""));
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return (net.minecraft.util.SoundEvent) net.minecraft.util.SoundEvent.REGISTRY.getObject(new ResourceLocation(""));
	}
	
	@Override
	protected boolean canDespawn() {
		return !EntityInit.PERSISTENCE;
	}
	
	@Override
	protected Item getDropItem() {
		return new ItemStack(Blocks.DRAGON_EGG, 1).getItem();
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (source == DamageSource.FALL)
			return false;
		if (source == DamageSource.CACTUS)
			return false;
		if (source == DamageSource.DROWN)
			return false;
		if (source == DamageSource.LIGHTNING_BOLT)
			return false;
		return super.attackEntityFrom(source, amount);
	}
	
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		IEntityLivingData res = super.onInitialSpawn(difficulty, livingdata);
		
		WorldData wd = WorldData.get(world);
		
		if (wd.getNPC()) world.removeEntity(this);
		else {
			exists = true;
			wd.setNPC(true);
		}
		
		return res;
	}
	
	public void onDeath(DamageSource source) {
		exists = false;
		WorldData.get(world).setNPC(false);
	}
	
	public static String[] introDialogue = {"How can I help you?", "...", "Hi there.", "Oh, it's you.", "Hello!"};
	
	@Override
	public boolean processInteract(EntityPlayer entity, EnumHand eh) {
		ItemStack is = entity.getHeldItemMainhand();
		Item item = is.getItem();
		try {
			if (!world.isRemote) {
				if (item == Reference.i(Blocks.EMERALD_BLOCK)) {
					if (is.getCount() >= 2) {
						is.shrink(2);
						EntityItem pick = entity.dropItem(ItemInit.CONTINUUM, 1);
						pick.setNoPickupDelay();
						pick.setOwner(entity.getName());
						
						x("Thank you for your purchase.", entity);
					}
					else x("You do not have enough emerald blocks.", entity);
				}
				else if (item == ItemInit.CONTINUUM) x("Open it up and see what's inside!", entity);
				else if (item == ItemInit.TEST) x("...How did you get that?", entity);
				else if (item == Items.EMERALD) x("Sorry, I only take emerald blocks.", entity);
				else if (item == ItemInit.HOUSING) {
					if (WorldData.get(world).getY() >= 0) x("Thank you for the new home.", entity);
					else x("A home would be appreciated.", entity);
				}
				else if (item == Reference.i(BlockInit.CONTINUUMBLOCK)) x("...", entity);
				else {
					x(introDialogue[(int)(Math.random() * introDialogue.length)], entity);
				}
				
				double prob = 1.0 / 64.0;
				if (Math.random() < prob) {
					Reference.open("https://www.youtube.com/watch?v=dQw4w9WgXcQ");
				}
			}
		}
		catch (Exception e) {
			System.out.println("!!!!");
			System.out.println(e);
			x(introDialogue[(int)(Math.random() * introDialogue.length)], entity);
		}
		return true;
	}
	
	static int floor(double d) {
		return (int)(Math.floor(d));
	}
	
	static ArrayList<Entity> aabb(Entity e, int x1, int y1, int z1, int x2, int y2, int z2) {
		BlockPos bp1 = new BlockPos(x1, y1, z1);
		BlockPos bp2 = new BlockPos(x2, y2, z2);
		AxisAlignedBB aabb = new AxisAlignedBB(bp1, bp2);
		ArrayList<Entity> things = (ArrayList<Entity>) e.world.getEntitiesWithinAABBExcludingEntity(e, aabb);
		return things;
	}
	
	static boolean containsPlayer(ArrayList<Entity> list) {
		for (Entity e : list) {
			if (e instanceof EntityPlayer) return true;
		}
		return false;
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		EntityLiving entity = this;
		if ((Math.random() < 0.001)) entity.setHealth(entity.getHealth() + 2);
		
		long currentTime = System.nanoTime();
		if (currentTime - time >= cooldown * 1000000000.0) {
			time = currentTime;
		//	System.out.println("RESET /// " + time);
			for (EntityEntry hostile : Reference.HOSTILE_ENTRIES) {
				try {
					String mob = hostile.getRegistryName().toString();
					if (Reference.MOB_BLACKLIST.contains(mob)) continue;
					String command = "/effect @e[r=" + radius + ",type=" + mob + "] poison " + duration + " 1 true";
					exec(command, entity);
				}
				catch (Exception e) {
				//	System.out.println(e);
				}
			}
		}
		
		// Handle housing + migration
		
		long time = (world.getWorldTime() + 48000) % 24000;
		boolean rain = world.isRaining();
		WorldData wd = WorldData.get(world);
		
		long numChecks = 2;
		long modulo = 24000 / numChecks;
		
		if ((time + 48000) % modulo >= 0 && (time + 48000) % modulo <= 2) {
			boolean isHouse = ItemHousing.query(world, wd.getX(), wd.getY(), wd.getZ());
			if (isHouse) System.out.println("HOUSING CHECK PASSED");
			else {
				System.out.println("HOUSING CHECK FAILED");
				wd.setHousePos(0, -1, 0);
			}
		}
		
		if (wd.getY() < 0) return; // No House
		
		int x = floor(entity.posX);
		int y = floor(entity.posY);
		int z = floor(entity.posZ);
		
		boolean inHouse = ItemHousing.posbfs(world, x, y, z, new BlockPos(wd.getHousePos()), false);
		if (Math.random() < 0.02 && DEBUG) System.out.println("NPC IN HOUSE? " + inHouse);
		
		ArrayList<Entity> nearest = aabb(this, x - rad, y - rad, z - rad, x + rad, y + rad, z + rad);
		ArrayList<Entity> vicinity = aabb(this, x - obs, y - obs, z - obs, x + obs, y + obs, z + obs);
		
		boolean isClose = containsPlayer(nearest);
		boolean isNear = containsPlayer(vicinity);
		
		boolean houseCondition = (time > Reference.DUSK && time < 24000) || rain;
		
		if (houseCondition && !inHouse) {
			if (isClose) return;
			else if (isNear) {
				if (DEBUG && Math.random() < 0.02) System.out.println("PLAYER IN VICINITY");
				this.getNavigator().tryMoveToXYZ(wd.getX(), wd.getY(), wd.getZ(), 1);
			}
			else {
				this.setPosition(wd.getX(), wd.getY(), wd.getZ());
			}
		}
		
	}
	
	public void exec(String comm, Entity e) {
		try {
			ICommandSender ics = new ICommandSender() {
				@Override
				public String getName() {
					return "";
				}

				@Override
				public boolean canUseCommand(int permission, String command) {
					return true;
				}

				@Override
				public World getEntityWorld() {
					return world;
				}

				@Override
				public MinecraftServer getServer() {
					return world.getMinecraftServer();
				}

				@Override
				public boolean sendCommandFeedback() {
					return false;
				}

				@Override
				public BlockPos getPosition() {
					return new BlockPos((int) (e.posX), (int) (e.posY), (int) (e.posZ));
				}

				@Override
				public Vec3d getPositionVector() {
					return new Vec3d((e.posX), (e.posY), (e.posZ));
				}
			};

			e.world.getMinecraftServer().getCommandManager().executeCommand(ics, comm);
		}
		catch (Exception exc) {
		//	System.out.println(exc);
		}
	}
	
	public static void x(String s, EntityPlayer e) {
		e.sendStatusMessage(new TextComponentString("[Darkness] " + s), (false));
	}
}

