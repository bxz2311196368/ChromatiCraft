/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2016
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.World.Dimension.Structure.ShiftMaze;

import java.util.Random;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Reika.ChromatiCraft.Block.Worldgen.BlockLootChest;
import Reika.ChromatiCraft.Items.Tools.ItemDoorKey;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.ChromatiCraft.Registry.ChromaItems;
import Reika.ChromatiCraft.World.Dimension.Structure.ShiftMazeGenerator;
import Reika.DragonAPI.Instantiable.Worldgen.ChunkSplicedGenerationCache;

public class MazePartGenerator {

	/* 5x5 space */
	public void generateExtras(ShiftMazeGenerator gen, ChunkSplicedGenerationCache world, int offsetX, int posY, int offsetZ, MazeGrid.MazeSegment segment) {
	}

	/* public static class ToggleDoorGenerator extends MazePartGenerator {
	 * 
	 * @Override public void generateExtras(ShiftMazeGenerator gen,
	 * ChunkSplicedGenerationCache world, int offsetX, int posY, int offsetZ,
	 * MazeGrid.MazeSegment segment) {
	 * 
	 * List<ShiftMazeState> state = segment.states; setDoor(gen, world, offsetX
	 * + 2, posY + 1, offsetZ + 2, state); setDoor(gen, world, offsetX + 2, posY
	 * + 2, offsetZ + 2, state); if (segment.isDoorHorizontal) { setDoor(gen,
	 * world, offsetX + 1, posY + 1, offsetZ + 2, state); setDoor(gen, world,
	 * offsetX + 1, posY + 2, offsetZ + 2, state); setDoor(gen, world, offsetX +
	 * 3, posY + 1, offsetZ + 2, state); setDoor(gen, world, offsetX + 3, posY +
	 * 2, offsetZ + 2, state); } else { setDoor(gen, world, offsetX + 2, posY +
	 * 1, offsetZ + 1, state); setDoor(gen, world, offsetX + 2, posY + 2,
	 * offsetZ + 1, state); setDoor(gen, world, offsetX + 2, posY + 1, offsetZ +
	 * 3, state); setDoor(gen, world, offsetX + 2, posY + 2, offsetZ + 3,
	 * state); }
	 * 
	 * }
	 * 
	 * private void setDoor(ShiftMazeGenerator gen, ChunkSplicedGenerationCache
	 * world, int x, int y, int z, List<ShiftMazeState> states) {
	 * world.setBlock(x, y, z, ChromaBlocks.SHIFTLOCK.getBlockInstance(),
	 * states.contains(gen.getActiveState()) ? 1 : 0); gen.addToggleDoor(x, y,
	 * z, states); }
	 * 
	 * } */

	public static class ChestGenerator extends MazePartGenerator {

		// The value used from this random is not necessarily too important, so
		// connecting it to some sort of seed is unnecessary..
		private static final Random RAND = new Random();

		@Override
		public void generateExtras(ShiftMazeGenerator gen, ChunkSplicedGenerationCache world, int offsetX, int posY, int offsetZ, MazeGrid.MazeSegment segment) {

			world.setTileEntity(offsetX + 2, posY + 1, offsetZ + 2, ChromaBlocks.LOOTCHEST.getBlockInstance(), 0, new LootChestCallback(segment.keyUUID, RAND));

		}

		private static class LootChestCallback implements ChunkSplicedGenerationCache.TileCallback {

			private final UUID uid;
			private final Random rand;

			private LootChestCallback(UUID id, Random r) {
				uid = id;
				rand = r;
			}

			@Override
			public void onTilePlaced(World world, int x, int y, int z, TileEntity te) {
				if (te instanceof BlockLootChest.TileEntityLootChest) {
					BlockLootChest.TileEntityLootChest tc = (BlockLootChest.TileEntityLootChest)te;
					ItemStack key = ChromaItems.KEY.getStackOf();
					((ItemDoorKey)ChromaItems.KEY.getItemInstance()).setID(key, uid);
					tc.setInventorySlotContents(rand.nextInt(tc.getSizeInventory()), key);
				}
			}

		}

	}

}
