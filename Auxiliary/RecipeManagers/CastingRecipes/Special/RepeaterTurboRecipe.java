/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2016
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipes.Special;

import java.util.Collection;
import java.util.Random;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Auxiliary.ProgressionManager.ProgressStage;
import Reika.ChromatiCraft.Auxiliary.Interfaces.EnergyLinkingRecipe;
import Reika.ChromatiCraft.Auxiliary.RecipeManagers.CastingRecipe.PylonRecipe;
import Reika.ChromatiCraft.Registry.ChromaIcons;
import Reika.ChromatiCraft.Registry.ChromaSounds;
import Reika.ChromatiCraft.Registry.ChromaTiles;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.ChromatiCraft.Render.Particle.EntityBlurFX;
import Reika.ChromatiCraft.Render.Particle.EntityFloatingSeedsFX;
import Reika.ChromatiCraft.TileEntity.Recipe.TileEntityCastingAuto;
import Reika.ChromatiCraft.TileEntity.Recipe.TileEntityCastingTable;
import Reika.DragonAPI.Libraries.IO.ReikaSoundHelper;
import Reika.DragonAPI.Libraries.Java.ReikaRandomHelper;
import Reika.DragonAPI.Libraries.Registry.ReikaParticleHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RepeaterTurboRecipe extends PylonRecipe implements EnergyLinkingRecipe {

	public RepeaterTurboRecipe(ChromaTiles rpt, int baseAura) {
		super(getOutputItem(rpt), rpt.getCraftedProduct());

		this.addAuraRequirement(CrystalElement.BLACK, 5*baseAura);
		this.addAuraRequirement(CrystalElement.WHITE, 4*baseAura);
		this.addAuraRequirement(CrystalElement.PURPLE, 8*baseAura);
		this.addAuraRequirement(CrystalElement.BLUE, 10*baseAura);
		this.addAuraRequirement(CrystalElement.YELLOW, 15*baseAura);
		this.addAuraRequirement(CrystalElement.GRAY, 2*baseAura);

		this.addAuxItem(ChromaStacks.glowbeans, -2, -2);
		this.addAuxItem(ChromaStacks.glowbeans, 2, -2);
		this.addAuxItem(ChromaStacks.glowbeans, -2, 2);
		this.addAuxItem(ChromaStacks.glowbeans, 2, 2);

		this.addAuxItem(ChromaStacks.boostroot, 0, 2);
		this.addAuxItem(ChromaStacks.boostroot, 0, -2);
		this.addAuxItem(ChromaStacks.boostroot, 2, 0);
		this.addAuxItem(ChromaStacks.boostroot, -2, 0);

		this.addAuxItem(ChromaStacks.beaconDust, -4, -4);
		this.addAuxItem(ChromaStacks.beaconDust, 4, 4);
		this.addAuxItem(ChromaStacks.beaconDust, -4, 4);
		this.addAuxItem(ChromaStacks.beaconDust, 4, -4);

		this.addAuxItem(ChromaStacks.focusDust, 0, -4);
		this.addAuxItem(ChromaStacks.focusDust, 4, 0);
		this.addAuxItem(ChromaStacks.focusDust, 0, 4);
		this.addAuxItem(ChromaStacks.focusDust, -4, 0);

		this.addAuxItem(Items.glowstone_dust, -2, -4);
		this.addAuxItem(Items.glowstone_dust, 2, -4);
		this.addAuxItem(Items.glowstone_dust, 4, -2);
		this.addAuxItem(Items.glowstone_dust, 4, 2);
		this.addAuxItem(Items.glowstone_dust, 2, 4);
		this.addAuxItem(Items.glowstone_dust, -2, 4);
		this.addAuxItem(Items.glowstone_dust, -4, 2);
		this.addAuxItem(Items.glowstone_dust, -4, -2);
	}

	private static ItemStack getOutputItem(ChromaTiles rpt) {
		ItemStack is = rpt.getCraftedProduct();
		is.stackTagCompound = new NBTTagCompound();
		is.stackTagCompound.setBoolean("boosted", true);
		return is;
	}

	@Override
	public boolean isIndexed() {
		return false;
	}

	@Override
	protected void getRequiredProgress(Collection<ProgressStage> c) {
		c.add(ProgressStage.CTM);
	}

	@Override
	public int getExperience() {
		return super.getExperience()*20;
	}

	@Override
	public int getDuration() {
		return super.getDuration()*8;
	}

	@Override
	public ChromaSounds getSoundOverride(TileEntityCastingTable te, int soundTimer) {
		return soundTimer >= te.getSoundLength() ? ChromaSounds.POWERCRAFT : null;
	}

	@Override
	public void onRecipeTick(TileEntityCastingTable te) {
		int tick = te.getCraftingTick();
		if (te.worldObj.isRemote) {
			this.doParticleFX(te, tick);
		}
		else {

		}
	}

	@Override
	public void onCrafted(TileEntityCastingTable te, EntityPlayer ep) {
		if (te.worldObj.isRemote) {
			for (int i = 0; i < 16; i++) {
				int x = ReikaRandomHelper.getRandomPlusMinus(te.xCoord, 8);
				int y = ReikaRandomHelper.getRandomPlusMinus(te.yCoord, 2);
				int z = ReikaRandomHelper.getRandomPlusMinus(te.zCoord, 8);
				ReikaParticleHelper.EXPLODE.spawnAroundBlock(te.worldObj, x, y, z, 4);
				ReikaSoundHelper.playClientSound(ChromaSounds.TRAP, te.xCoord+0.5, te.yCoord+0.5, te.zCoord+0.5, 2, 2F);
				ReikaSoundHelper.playClientSound(ChromaSounds.TRAP, te.xCoord+0.5, te.yCoord+0.5, te.zCoord+0.5, 2, 1F);
				ReikaSoundHelper.playClientSound(ChromaSounds.TRAP, te.xCoord+0.5, te.yCoord+0.5, te.zCoord+0.5, 2, 0.5F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	private static void doParticleFX(TileEntityCastingTable te, int tick) {
		//ReikaJavaLibrary.pConsole("P"+var);
		//EffectType.list[effect].doEffect(te);
		for (int i = 0; i < 3; i++) {
			double x = te.xCoord+0.5;
			double y = te.yCoord+0.5;
			double z = te.zCoord+0.5;
			Random rand = te.getRandom();
			int l = 10+rand.nextInt(10);
			float s = 2+rand.nextFloat()*3;
			EntityFloatingSeedsFX fx1 = (EntityFloatingSeedsFX)new EntityFloatingSeedsFX(te.worldObj, x, y, z, rand.nextDouble()*360, rand.nextDouble()*360).setScale(s).setColor(CrystalElement.randomElement().getColor()).setLife(l).setIcon(ChromaIcons.FADE_GENTLE);
			fx1.particleVelocity *= 2;
			EntityBlurFX fx2 = new EntityBlurFX(te.worldObj, x, y, z).setIcon(ChromaIcons.FLARE).setScale(s*0.75F).lockTo(fx1).setLife(l);
			Minecraft.getMinecraft().effectRenderer.addEffect(fx1);
			Minecraft.getMinecraft().effectRenderer.addEffect(fx2);
		}
	}

	@SideOnly(Side.CLIENT)
	public static void onClientSideRandomTick(TileEntityCastingTable te, int effect) {
		//ReikaJavaLibrary.pConsole("P"+var);
		//EffectType.list[effect].doEffect(te);
	}

	@Override
	public float getAutomationCostFactor(TileEntityCastingAuto ae, TileEntityCastingTable te, ItemStack is) {
		return 24;
	}

}
