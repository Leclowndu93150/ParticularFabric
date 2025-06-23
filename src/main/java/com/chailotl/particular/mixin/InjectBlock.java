package com.chailotl.particular.mixin;

import com.chailotl.particular.Main;
import com.chailotl.particular.Particles;
import com.chailotl.particular.sushi_bar.owo.config.ConfigManager;
import net.minecraft.block.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Block.class)
public class InjectBlock
{
	@Unique
	private static boolean isValidBiome(RegistryEntry<Biome> biome)
	{
		var key = biome.getKey();
		return key.map(biomeRegistryKey -> !Main.CONFIG.advancedSettings.caveDustSettings.excludeBiomes.contains(biomeRegistryKey.getValue())).orElse(true);
	}

	@Inject(at = @At("TAIL"), method = "randomDisplayTick")
	public void spawnParticles(BlockState state, World world, BlockPos pos, Random random, CallbackInfo ci)
	{
		Block block = state.getBlock();

		if (Main.CONFIG.enabledEffects.fireflies)
		{
			// Fireflies
			double val = random.nextDouble();
			if ((block == Blocks.GRASS_BLOCK && val < ConfigManager.getFireflyGrassChance()) ||
				(block == Blocks.TALL_GRASS && val < ConfigManager.getFireflyTallGrassChance()) ||
				(block instanceof FlowerBlock && val < ConfigManager.getFireflyTallFlowersChance()) ||
				(block instanceof TallFlowerBlock && val < ConfigManager.getFireflyTallFlowersChance()))
			{
				Main.spawnFirefly(world, pos, random);
				return;
			}
		}

		if (Main.CONFIG.enabledEffects.caveDust)
		{
			// Cave dust
			if (block == Blocks.AIR || block == Blocks.CAVE_AIR)
			{
				if (random.nextInt(Main.CONFIG.advancedSettings.caveDustSettings.spawnChance) == 0 && pos.getY() < world.getSeaLevel() && isValidBiome(world.getBiome(pos)))
				{
					float lightChance = 1f - Math.min(8, world.getLightLevel(LightType.SKY, pos)) / 8f;
					float depthChance = Math.min(1f, (world.getSeaLevel() - pos.getY()) / 96f);

					if (random.nextFloat() < lightChance * depthChance)
					{
						double x = (double)pos.getX() + random.nextDouble();
						double y = (double)pos.getY() + random.nextDouble();
						double z = (double)pos.getZ() + random.nextDouble();
						world.addParticleClient(Particles.CAVE_DUST, x, y, z, 0.0, 0.0, 0.0);
					}
				}
			}
		}
	}
}