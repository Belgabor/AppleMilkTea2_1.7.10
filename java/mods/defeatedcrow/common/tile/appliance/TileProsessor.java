package mods.defeatedcrow.common.tile.appliance;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mods.defeatedcrow.api.recipe.IProsessorRecipe;
import mods.defeatedcrow.api.recipe.RecipeRegisterManager;
import mods.defeatedcrow.common.AMTLogger;
import mods.defeatedcrow.recipe.ProsessorRecipeRegister.ProsessorRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.oredict.OreDictionary;

public class TileProsessor extends MachineBase{

	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
	}
	
	@Override
	public Packet getDescriptionPacket() {
        return super.getDescriptionPacket();
	}
 
	@Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
    }

	@Override
	public boolean canSmelt() {
		boolean flag1 = false;
		boolean flag2 = false;
		
		List<ItemStack> items = new ArrayList<ItemStack>(this.getCurrentContains());
		List<IProsessorRecipe> recipes = new ArrayList<IProsessorRecipe>((List<IProsessorRecipe>) RecipeRegisterManager.prosessorRecipe.getRecipes());
		if (recipes == null || recipes.isEmpty()) return false;
		
		ItemStack output = null;
		ItemStack sec = null;
		
		for(IProsessorRecipe recipe : recipes)
		{
			if (recipe.matches(items))
			{
				output = recipe.getOutput();
				sec = recipe.getSecondary();
				flag1 = recipe.isFoodRecipe();
				break;
			}
		}
		
		if (output == null) return false;
		
		if (this.itemstacks[11] == null)
		{
			flag2 = true;
		}
		else
		{
			if (this.itemstacks[11].isItemEqual(output))
			{
				int result = this.itemstacks[11].stackSize + output.stackSize;
				flag2 = (result <= this.getInventoryStackLimit() && result <= output.getMaxStackSize());
			}
		}
		
		if (flag2 && sec != null)
		{
			if (this.itemstacks[12] == null)
			{
				flag2 = true;
			}
			else
			{
				if (this.itemstacks[12].isItemEqual(sec))
				{
					int result = this.itemstacks[12].stackSize + sec.stackSize;
					flag2 = (result <= this.getInventoryStackLimit() && result <= sec.getMaxStackSize());
				}
			}
		}
		
		return flag1 && flag2;
	}
	
	private List<ItemStack> getCurrentContains()
	{
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (int i = 2; i < 11; i++)
		{
			if (this.itemstacks[i] != null)
			{
				items.add(this.itemstacks[i].copy());
			}
		}
		return items;
	}

	@Override
	public void onProgress() {
		//結局canSmelt()と同じことをしていて無駄な感じはする
		List<ItemStack> items = new ArrayList<ItemStack>(this.getCurrentContains());
		List<IProsessorRecipe> recipes = new ArrayList<IProsessorRecipe>((List<IProsessorRecipe>) RecipeRegisterManager.prosessorRecipe.getRecipes());
		if (recipes == null || recipes.isEmpty()) return;
		
		IProsessorRecipe activeRecipe = null;
		boolean flag = false;
		
		for(IProsessorRecipe recipe : recipes)
		{
			if (recipe.matches(items))
			{
				activeRecipe = recipe;
				flag = true;
			}
		}
		
		if (flag && activeRecipe != null)
		{
			//まずは材料を減らす
			List<Object> required = new ArrayList<Object>(activeRecipe.getProcessedInput());
			ItemStack output = activeRecipe.getOutput();
			ItemStack sec = activeRecipe.getSecondary();
			
			for (int i = 2; i < 11; i++)
			{
				ItemStack slot = this.itemstacks[i];

	            if (slot != null)
	            {
	                boolean inRecipe = false;
	                Iterator<Object> req = required.iterator();

	                //9スロットについて、要求材料の数だけ回す
	                while (req.hasNext())
	                {
	                    boolean match = false;
	                    Object next = req.next();
	                    int count = 1;

	                    if (next instanceof ItemStack)
	                    {
	                    	count = ((ItemStack)next).stackSize;
	                    	
	                        match = OreDictionary.itemMatches((ItemStack)next, slot, false)
	                        		&& slot.stackSize >= count;
	                    }
	                    else if (next instanceof ArrayList)
	                    {
	                        ArrayList<ItemStack> list = new ArrayList<ItemStack>((ArrayList<ItemStack>)next);
	                        count = 1;
	                        if (list != null && !list.isEmpty())
	                        {
	                        	for (ItemStack item : list)
		                        {
		                            match = OreDictionary.itemMatches(item, slot, false)
		                            		&& slot.stackSize > 0;
		                        }
	                        }
	                    }

	                    if (match)
	                    {
	                        inRecipe = true;
	                        required.remove(next);
	                        this.itemstacks[i].stackSize -= count;;
	                        if (this.itemstacks[i].stackSize < 1) this.itemstacks[i] = null;
	                        this.markDirty();
	                        break;
	                    }
	                }

	                if (!inRecipe)
	                {
	                    return;//中断
	                }
	            }
			}
			
			AMTLogger.debugInfo("current recipe : " + output.toString());
			
			//次に完成品を完成品スロットへ
			if (this.itemstacks[11] == null)
			{
				this.itemstacks[11] = output.copy();
			}
			else if (this.itemstacks[11].isItemEqual(output))
			{
				this.itemstacks[11].stackSize += output.stackSize;
			}
			
			if (sec != null)
			{
				if (this.itemstacks[12] == null)
				{
					this.itemstacks[12] =sec.copy();
				}
				else if (this.itemstacks[12].isItemEqual(sec))
				{
					this.itemstacks[12].stackSize += sec.stackSize;
				}
			}
			
			this.markDirty();
		}
	}
	
	/*====== 以下、インベントリ関係 ======*/
	
	/*
	 * フードプロセッサーの場合
	 * 燃料スロット：0
	 * 燃料空容器の排出スロット：1
	 * 材料スロット：2～10
	 * 完成品スロット：11,12
	 * */
	
	@Override
	public int getSizeInventory() {
		return 13;
	}

	@Override
	protected int[] slotsTop() {
		return new int[]{0,2,3,4,5,6,7,8,9,10};
	}

	@Override
	protected int[] slotsBottom() {
		return new int[]{1,11,12};
	}

	@Override
	protected int[] slotsSides() {
		return new int[]{0,1,2,3,4,5,6,7,8,9,10,11,12};
	}
	
	@Override
	public String getInventoryName() {
		return "Food Prosessor";
	}

}
