package continuum.mod.entities;

import continuum.mod.blocks.ModBlocks;
import continuum.mod.items.HousingItem;
import continuum.mod.items.ModItems;
import continuum.mod.procedures.DataProvider;
import continuum.mod.util.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NPCEntity extends PathfinderMob {
    private static final boolean DEBUG = false;
    private final int rad = 8;
    private final int obs = 32;

    static Long time = null;
    static double cooldown = 4;
    static int radius = 16;
    static int duration = 2;
    public NPCEntity(EntityType<? extends NPCEntity> type, Level level) {
        super(type, level);
        ((GroundPathNavigation)this.getNavigation()).setCanPassDoors(true);
        ((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(true);
    }

    public void checkDespawn() {
    }

    public static AttributeSupplier.Builder registerAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 400)
                .add(Attributes.MOVEMENT_SPEED, ModEntityTypes.SPEED)
                .add(Attributes.FOLLOW_RANGE, ModEntityTypes.RANGE)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ARMOR, 10);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Blocks.GOLD_BLOCK)), false));
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Items.EMERALD)), false));
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Blocks.EMERALD_BLOCK)), false));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 4.0F));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, ModEntityTypes.RANGE));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, NPCEntity.class, ModEntityTypes.RANGE));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(7, new FloatGoal(this));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.8F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    protected SoundEvent getAmbientSound() {
        return new SoundEvent(new ResourceLocation(""));
    }

    protected SoundEvent getHurtSound(DamageSource src) {
        return new SoundEvent(new ResourceLocation(""));
    }

    protected SoundEvent getDeathSound() {
        return new SoundEvent(new ResourceLocation(""));
    }

    boolean lol = false;

    public boolean hurt(DamageSource src, float dmg) {
        if (src == DamageSource.FALL) return false;
        if (src == DamageSource.CACTUS) return false;
        if (src == DamageSource.DROWN) return false;
        if (src == DamageSource.LIGHTNING_BOLT) return false;

        super.hurt(src, dmg);

        return true;
    }

    public SpawnGroupData finalizeSpawn(ServerLevelAccessor sla, DifficultyInstance di, MobSpawnType mst, @Nullable SpawnGroupData sgd, @Nullable CompoundTag ct) {
        SpawnGroupData sd = super.finalizeSpawn(sla, di, mst, sgd, ct);
        LivingEntity e = this;
        Level level = e.getLevel();
        if (e.getLevel().dimension() != Level.OVERWORLD) e.remove(RemovalReason.DISCARDED);
        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            if (data.getNPC()) {
                e.remove(RemovalReason.DISCARDED);
                System.out.println("NPC ALREADY EXISTS");
            }
            else data.setNPC(true);
        });

        return sd;
    }

    protected void tickDeath() {
        System.out.println("NPC WAS SLAIN...");
        Level level = this.getLevel();
        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            data.setNPC(false);
        });
        this.remove(RemovalReason.KILLED);
    }

    protected boolean shouldDespawnInPeaceful() {
        return false;
    }

    public void x(String s, Player player) {
        player.sendSystemMessage(Component.literal(s));
    }

    public String introDialogue[] = {"How can I help you?", "...", "Hi there.", "Oh, it's you.", "Hello!", "Hi!"};

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.sidedSuccess(this.level.isClientSide);
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        try {
            if (itemstack.is(Items.EMERALD_BLOCK)) {
                if (itemstack.getCount() >= 2) {
                    itemstack.shrink(2);
                    ItemEntity pick = player.drop(new ItemStack(ModItems.CONTINUUM.get()), false);
                    pick.setNoPickUpDelay();
                    x("Thank you for your purchase.", player);
                }
                else x("You do not have enough emerald blocks.", player);
            }
            else if (item == ModItems.CONTINUUM.get()) x("Open it up and see what's inside!", player);
            else if (item == Items.EMERALD) x("Sorry, I only take emerald blocks.", player);
            else if (item == ModItems.HOUSING.get()) {
                level.getCapability(DataProvider.DATA).ifPresent(data -> {
                    if (data.getY() >= -64) x("Thank you for the new home.", player);
                    else x("A home would be appreciated.", player);
                });
            }
            else if (item == Reference.i(ModBlocks.CONTINUUM.get())) x("...", player);
            else {
                x(introDialogue[(int)(Math.random() * introDialogue.length)], player);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }

        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    static int floor(double d) {
        return (int)(Math.floor(d));
    }

    static ArrayList<Entity> aabb(Entity e, int x1, int y1, int z1, int x2, int y2, int z2) {
        BlockPos bp1 = new BlockPos(x1, y1, z1);
        BlockPos bp2 = new BlockPos(x2, y2, z2);
        AABB aabb = new AABB(bp1, bp2);
        ArrayList<Entity> things = (ArrayList<Entity>) e.getLevel().getEntities(e, aabb);
        for (int i = 0; i < things.size(); i++) {
            if (things.get(i).equals(e)) {
                things.remove(i);
                break;
            }
        }
        if (Math.random() < 0.008 && DEBUG) System.out.println(things);
        return things;
    }

    static boolean containsPlayer(ArrayList<Entity> list) {
        for (Entity e : list) {
            if (e instanceof Player || e instanceof ServerPlayer) return true;
        }
        return false;
    }

    static Long desperatemeasure = null;

    public void tick() {

        super.tick();
        LivingEntity entity = this;
        Level world = entity.getLevel();
        int x = floor(entity.getX());
        int y = floor(entity.getY());
        int z = floor(entity.getZ());
        long worldTime = (world.getDayTime() + 48000) % 24000;
        if (Math.random() < 0.01 && DEBUG) System.out.println(worldTime);
        boolean rain = world.isRaining();


        if ((Math.random() < 0.001)) entity.setHealth(entity.getHealth() + 2);
        long currentTime = System.nanoTime();
        if (time == null || currentTime - time >= cooldown * 1000000000.0) {
            time = currentTime;
            System.out.println("RESET /// " + time);
            try {
                ArrayList<Entity> list = aabb(entity, x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
                for (Entity e : list) {
                    if (!(e instanceof LivingEntity)) continue;
                    LivingEntity en = (LivingEntity) e;
                    if (Reference.MOB_BLACKLIST.contains(e.getType().toString())) continue;
                    if (Reference.HOSTILE_ENTRIES.contains(e.getType())) {
                        // System.out.println("LOL" + ex);
                        if (en.getHealth() > 4) en.setHealth(en.getHealth() - 4);
                    }
                    else en.setHealth(en.getHealth() + 1);
                }
            }
            catch (Exception e) {
                //	System.out.println(e);
            }
        }

        long numChecks = 2;
        long modulo = 24000 / numChecks;

        AtomicInteger hx = new AtomicInteger(0);
        AtomicInteger hy = new AtomicInteger(-9000);
        AtomicInteger hz = new AtomicInteger(0);

        level.getCapability(DataProvider.DATA).ifPresent(data -> {
            hx.set(data.getX());
            hy.set(data.getY());
            hz.set(data.getZ());
        });

        BlockPos hbp = new BlockPos(hx.get(), hy.get(), hz.get());

        if ((worldTime + 48000) % modulo >= 0 && (worldTime + 48000) % modulo <= 2) {
            Long desperado = System.nanoTime();
            if (desperatemeasure == null || desperado - desperatemeasure > 0.4 * 1000000000) {
                desperatemeasure = desperado;
                if (DEBUG) System.out.println("TIME TO DO THE HOUSING CHECK - " + worldTime);
                boolean isHouse = HousingItem.query(world, hx.get(), hy.get(), hz.get());
                if (isHouse) {
                    System.out.println("HOUSING CHECK PASSED");
                }
                else {
                    System.out.println("HOUSING CHECK FAILED");
                    level.getCapability(DataProvider.DATA).ifPresent(data -> {
                        data.setHousePos(0, -9000, 0);
                    });
                }
            }
        }

        if (hy.get() < -64) return;

        boolean inHouse = HousingItem.posbfs(world, x, y, z, hbp, false);
        if (Math.random() < 0.02 && DEBUG) System.out.println("NPC IN HOUSE? " + inHouse);

        ArrayList<Entity> nearest = aabb(this, x - rad, y - rad, z - rad, x + rad, y + rad, z + rad);
        ArrayList<Entity> vicinity = aabb(this, x - obs, y - obs, z - obs, x + obs, y + obs, z + obs);

        boolean isClose = containsPlayer(nearest);
        boolean isNear = containsPlayer(vicinity);
        boolean houseCondition = (worldTime > Reference.DUSK && worldTime < 24000) || rain;
        if (Math.random() < 0.02 && DEBUG) System.out.println("NPC SHOULD HEAD HOME? " + houseCondition);
        if (Math.random() < 0.02 && DEBUG) System.out.println("isClose - " + isClose + " | isNear - " + isNear);

        if (houseCondition && !inHouse) {
            if (isClose) return;
            else if (isNear) {
                if (DEBUG && Math.random() < 0.02) System.out.println("PLAYER IN VICINITY");
                this.getNavigation().moveTo(hx.get(), hy.get(), hz.get(), 1);
            }
            else {
                this.setPos(hx.get(), hy.get(), hz.get());
            }
        }
    }
}
