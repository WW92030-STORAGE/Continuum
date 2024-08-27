package com.notasmr.continuum.entities;

import com.notasmr.continuum.blocks.ModBlocks;
import com.notasmr.continuum.items.HousingItem;
import com.notasmr.continuum.items.ModItems;
import com.notasmr.continuum.procedures.CapabilityWorldData;
import com.notasmr.continuum.procedures.DataProvider;
import com.notasmr.continuum.util.Reference;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class NPCEntity extends CreatureEntity {
    public NPCEntity(EntityType<? extends NPCEntity> type, World level) {
        super(type, level);
        ((GroundPathNavigator)this.getNavigation()).setCanOpenDoors(true);
    }

    public static AttributeModifierMap.MutableAttribute registerAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, ModEntityTypes.SPEED)
                .add(Attributes.FOLLOW_RANGE, ModEntityTypes.RANGE)
                .add(Attributes.ATTACK_DAMAGE, 10)
                .add(Attributes.ARMOR, 10);
    }

    protected void registerGoals() {
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Blocks.GOLD_BLOCK)), false));
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Items.EMERALD)), false));
        this.targetSelector.addGoal(1, new TemptGoal(this, 1, Ingredient.of(new ItemStack(Blocks.EMERALD_BLOCK)), false));
        this.goalSelector.addGoal(2, new LookAtGoal(this, PlayerEntity.class, 4.0F));
        this.goalSelector.addGoal(3, new OpenDoorGoal(this, true));
        this.targetSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class, ModEntityTypes.RANGE));
        this.goalSelector.addGoal(5, new LookAtGoal(this, NPCEntity.class, ModEntityTypes.RANGE));
        this.goalSelector.addGoal(6, new RandomWalkingGoal(this, 1));
        this.goalSelector.addGoal(7, new SwimGoal(this));
        this.goalSelector.addGoal(8, new LeapAtTargetGoal(this, 0.8F));
        this.goalSelector.addGoal(9, new LookRandomlyGoal(this));
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

    public boolean hurt(DamageSource src, float dmg) {
        if (src == DamageSource.FALL) return false;
        if (src == DamageSource.CACTUS) return false;
        if (src == DamageSource.DROWN) return false;
        if (src == DamageSource.LIGHTNING_BOLT) return false;
        super.hurt(src, dmg);

        return true;
    }

    public ILivingEntityData finalizeSpawn(IServerWorld l, DifficultyInstance d, SpawnReason r, @Nullable ILivingEntityData i, @Nullable CompoundNBT n) {
        ILivingEntityData spawnData = super.finalizeSpawn(l, d, r, i, n);

        level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
            if (data.getNPC()) {
                this.remove();
                System.out.println("NPC ALREADY EXISTS");
            }
            else data.setNPC(true);
        });

        return spawnData;
    }

    protected void tickDeath() {
        System.out.println("NPC WAS SLAIN...");
        World level = this.level;
        level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
            data.setNPC(false);
        });
        this.remove();
    }

    private static final boolean DEBUG = false;

    public void x(String s, PlayerEntity player) {
        Reference.disp(s, player);
    }

    public String introDialogue[] = {"How can I help you?", "...", "Hi there.", "Oh, it's you.", "Hello!", "Hi!"};

    public ActionResultType mobInteract(PlayerEntity player, Hand hand) {
        if (level.isClientSide()) return ActionResultType.sidedSuccess(this.level.isClientSide);
        ItemStack itemstack = player.getItemInHand(hand);
        Item item = itemstack.getItem();
        try {
            if (itemstack.getItem().equals(Items.EMERALD_BLOCK)) {
                if (itemstack.getCount() >= 2) {
                    itemstack.shrink(2);
                    ItemEntity pick = player.drop(new ItemStack(ModItems.CONTINUUM_SHARD.get()), false);
                    pick.setNoPickUpDelay();
                    x("Thank you for your purchase.", player);
                }
                else x("You do not have enough emerald blocks.", player);
            }
            else if (item == ModItems.CONTINUUM_SHARD.get()) x("Open it up and see what's inside!", player);
            else if (item == Items.EMERALD) x("Sorry, I only take emerald blocks.", player);
            else if (item == ModItems.HOUSING_QUERY.get()) {
                level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
                    if (data.getY() >= 0) x("Thank you for the new home.", player);
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

        return ActionResultType.sidedSuccess(this.level.isClientSide);
    }

    static int floor(double d) {
        return (int)(Math.floor(d));
    }

    static ArrayList<Entity> aabb(Entity e, int x1, int y1, int z1, int x2, int y2, int z2) {
        BlockPos bp1 = new BlockPos(x1, y1, z1);
        BlockPos bp2 = new BlockPos(x2, y2, z2);
        AxisAlignedBB aabb = new AxisAlignedBB(bp1, bp2);
        ArrayList<Entity> things = (ArrayList<Entity>) e.level.getEntities(e, aabb);
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
            if (e instanceof PlayerEntity || e instanceof ServerPlayerEntity) return true;
        }
        return false;
    }

    static Long desperatemeasure = null;
    private final int rad = 8;
    private final int obs = 32;

    static Long time = null;
    static double cooldown = 4;
    static int radius = 16;
    static int duration = 2;

    public void tick() {

        super.tick();
        LivingEntity entity = this;
        World world = entity.level;
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

        level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
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
                    level.getCapability(CapabilityWorldData.DATA).ifPresent(data -> {
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
