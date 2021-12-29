package continuum.mod.data_housing;

import continuum.mod.util.Reference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class WorldData extends WorldSavedData {
	private static final String DATA_NAME = Reference.MODID + "_Data";
	private boolean NPCExists;
	private int hx;
	private int hy;
	private int hz;
	
	private final static boolean DEBUG = false;
	
	// Required constructors
	public WorldData() {
		this(DATA_NAME);
	}
	
	public WorldData(String s) {
		super(s);
		this.setY(-1);
	}

	public static WorldData get(World world) {
		MapStorage storage = world.getMapStorage();
		WorldData instance;
		instance = (WorldData) storage.getOrLoadData(WorldData.class, DATA_NAME);
	//	instance = (WorldData) world.loadData(WorldData.class, DATA_NAME);
        
		if (instance == null) {
			System.out.println("NO DATA FOUND - CREATING NEW");
			instance = new WorldData();
			storage.setData(DATA_NAME, instance);
		}
		else if (DEBUG) {
			System.out.println("DATA FOUND FOR CURRENT WORLD");
			System.out.println("NPC EXISTS = " + instance.getNPC());
			System.out.println("HOUSE POSITION = " + instance.getX() + " " + instance.getY() + " " + instance.getZ());
		}
		return instance;
	}
	
	@Override
    public void readFromNBT(NBTTagCompound nbt) {
        boolean tempnpc = nbt.getBoolean("NPCExists");
        int tempx = nbt.getInteger("HouseX");
        int tempy = nbt.getInteger("HouseY");
        int tempz = nbt.getInteger("HouseZ");
        NPCExists = tempnpc;
        hx = tempx;
        hy = tempy;
        hz = tempz;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
    	compound.setBoolean("NPCExists", NPCExists);
    	compound.setInteger("HouseX", hx);
    	compound.setInteger("HouseY", hy);
    	compound.setInteger("HouseZ", hz);
        return compound;
    }

    public void setNPC(boolean b) {
        this.NPCExists = b;
        markDirty();
        System.out.println("NPC exists is now = " + this.getNPC());
    }
    
    public void setHousePos(int x, int y, int z) {
    	this.hx = x;
    	this.hy = y;
    	this.hz = z;
    	markDirty();
    	System.out.println("NEW HOUSE POSITION = [" + this.hx + ", " + this.hy + ", " + this.hz + "]");
    }
    
    public BlockPos getHousePos() {
    	return new BlockPos(hx, hy, hz);
    }
    
    public void setX(int x) {
    	this.hx = x;
    	markDirty();
    }
    
    public void setY(int y) {
    	this.hy = y;
    	markDirty();
    }
    
    public void setZ(int z) {
    	this.hz = z;
    	markDirty();
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
}
