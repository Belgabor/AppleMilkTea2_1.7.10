package mods.defeatedcrow.common;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import mods.defeatedcrow.client.gui.*;
import mods.defeatedcrow.common.tile.*;
import mods.defeatedcrow.common.tile.appliance.*;
import mods.defeatedcrow.common.tile.energy.*;
import mods.defeatedcrow.handler.NetworkUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;


public class CommonProxy implements IGuiHandler{
	
	//TileEntityの登録
	//こちらはサーバー用クラスなので、レンダー関係は一切登録しない
    public void registerTileEntity()
    {
		GameRegistry.registerTileEntity(TileHasDirection.class, "TileHasDirection");
		GameRegistry.registerTileEntity(TileHasRemaining.class, "TileHasRemaining");
		GameRegistry.registerTileEntity(TileHasRemain2.class, "TileHasRemaining2");
		
		GameRegistry.registerTileEntity(TileTeppann.class, "TileTeppann");
		GameRegistry.registerTileEntity(TileCupHandle.class, "TileCupHandle");
		GameRegistry.registerTileEntity(TileBread.class, "TileBread");
		GameRegistry.registerTileEntity(TileDummy.class, "TileDummy");
		GameRegistry.registerTileEntity(TileJPBowl.class, "TileJPBowl");
		GameRegistry.registerTileEntity(TileChopsticksBox.class, "TileChopsticksBox");
		GameRegistry.registerTileEntity(TileEggs.class, "TileEggs");
		GameRegistry.registerTileEntity(TileSteak.class, "TileSteak");
		GameRegistry.registerTileEntity(TileMakerHandle.class, "TileMakerHandle");
		GameRegistry.registerTileEntity(TilePanHandle.class, "TilePanHandle");
		GameRegistry.registerTileEntity(TileChocoPan.class, "TileChocoPan");
		GameRegistry.registerTileEntity(TileMakerNext.class, "TileMakerNext");
		GameRegistry.registerTileEntity(TileAutoMaker.class, "TileAutoMaker");
		GameRegistry.registerTileEntity(TileWipeBox.class, "TileWipeBox");
		GameRegistry.registerTileEntity(TileIceMaker.class, "TileIceMaker");
		GameRegistry.registerTileEntity(TileIceCream.class, "TileIcecream");
		GameRegistry.registerTileEntity(TileWipeBox2.class, "TileWipeBox2");
		GameRegistry.registerTileEntity(TileRotaryDial.class, "TileRotaryDial");
		GameRegistry.registerTileEntity(TileCocktail.class, "TileCocktail");
		GameRegistry.registerTileEntity(TileCocktail2.class, "TileCocktail2");
		GameRegistry.registerTileEntity(TileLargeBottle.class, "TileLargeBottle");
		GameRegistry.registerTileEntity(TileEmptyBottle.class, "TileEmptyBottle");
		GameRegistry.registerTileEntity(TileCLamp.class, "TileChalcedonyLamp");
		GameRegistry.registerTileEntity(TileCordial.class, "TileCordial");
		GameRegistry.registerTileEntity(TileAlcoholCup.class, "TileAlcoholCup");
		GameRegistry.registerTileEntity(TileProsessor.class, "TileProsessor");
		GameRegistry.registerTileEntity(TileAdvProsessor.class, "TileAdvProsessor");
		GameRegistry.registerTileEntity(TileEvaporator.class, "TileEvaporator");
		GameRegistry.registerTileEntity(TileVegiBag.class, "TileVegiBag");
		GameRegistry.registerTileEntity(TileCardBoard.class, "TileCardboard");
		GameRegistry.registerTileEntity(TileCPanel.class, "TileChalcedonyPanel");
		GameRegistry.registerTileEntity(TileIncenseBase.class, "TileIncenseBase");
		GameRegistry.registerTileEntity(TilePanG.class, "TilePanG");
		GameRegistry.registerTileEntity(TileCanister.class, "TileCanister");
		GameRegistry.registerTileEntity(TileBrewingBarrel.class, "TileBarrel");
		GameRegistry.registerTileEntity(TileChargerBase.class, "TileChargerBase");
		GameRegistry.registerTileEntity(TileChargerDevice.class, "TileChargerDevice");
		GameRegistry.registerTileEntity(TileFlowerPot.class, "TileFlowerPot");
		GameRegistry.registerTileEntity(TileGelBat.class, "TileGelBattery");
		GameRegistry.registerTileEntity(TileTeppanII.class, "TileTeppanII");
		GameRegistry.registerTileEntity(TileCocktailSP.class, "TileCocktailSP");
		GameRegistry.registerTileEntity(TileHandleEngine.class, "TileEHandle");
	}
	
    //レンダーIDには-1を返す
	public int getRenderID()
	{
		return -1;
	}
	
	//レンダーの登録も何もしない
	public void registerRenderers()
	{
		
	}
	
	public int addArmor(String armor)
	{
		return 0;
	}

	//クライアント側のワールドではないのでnullを返す。
	public World getClientWorld() {
		
		return null;
	}
	
	//GUIの登録
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
 
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileAutoMaker) {
			return new ContainerAutoMaker(player.inventory, (TileAutoMaker) tileentity);
		}
		else if (tileentity instanceof TileIceMaker) {
			return new ContainerIceMaker(player, (TileIceMaker)tileentity);
		}
		else if (tileentity instanceof TileAdvProsessor) {
			return new ContainerAdvProsessor(player, (TileAdvProsessor)tileentity);
		}
		else if (tileentity instanceof TileProsessor) {
			return new ContainerProsessor(player, (TileProsessor)tileentity);
		}
		else if (tileentity instanceof TileEvaporator) {
			return new ContainerEvaporator(player, (TileEvaporator)tileentity);
		}
		else if (tileentity instanceof TileChargerBase) {
			return new ContainerBatBox(player, (TileChargerBase)tileentity);
		}
		
		return null;
	}
 
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if (!world.blockExists(x, y, z))
			return null;
 
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if (tileentity instanceof TileAutoMaker) {
			return new GuiAutoMaker(player.inventory, (TileAutoMaker) tileentity);
		}
		else if (tileentity instanceof TileIceMaker) {
			return new GuiIceMaker(player, (TileIceMaker)tileentity);
		}
		else if (tileentity instanceof TileAdvProsessor) {
			return new GuiAdvProsessor(player, (TileAdvProsessor)tileentity);
		}
		else if (tileentity instanceof TileProsessor) {
			return new GuiProsessor(player, (TileProsessor)tileentity);
		}
		else if (tileentity instanceof TileEvaporator) {
			return new GuiEvaporator(player, (TileEvaporator)tileentity);
		}
		else if (tileentity instanceof TileChargerBase) {
			return new GuiBatBox(player, (TileChargerBase)tileentity);
		}
		
		return null;
	}
	
	/**
	 * サーバサイドの場合、これが呼ばれるのは鯖の起動時、MODの読み込みなどとともに行われる。
	 * FMLServerHandlerを用いてサーバの環境を判定するため、クライアントでは行うことが出来ない。
	 * */
	public void serverStart() {
		NetworkUtil.initServer();
	}
	
	public void playerLogin(EntityPlayer player) {
		
	}

	public void init() {}
	
	public void registerTex() {}
	
	public void loadNEI() {}

	public void registerFluidTex() {}

}