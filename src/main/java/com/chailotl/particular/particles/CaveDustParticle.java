package com.chailotl.particular.particles;

import com.chailotl.particular.Main;
import com.chailotl.particular.sushi_bar.owo.config.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

import java.awt.*;

public class CaveDustParticle extends AscendingParticle
{
	protected CaveDustParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, float scaleMultiplier, SpriteProvider spriteProvider)
	{
		super(world, x, y, z, 0, 0, 0, velocityX, velocityY, velocityZ, scaleMultiplier, spriteProvider, 0, Main.CONFIG.advancedSettings.caveDustSettings.baseMaxAge, 0, true);

		Color color = Color.ofArgb(Main.CONFIG.advancedSettings.caveDustSettings.color);
		red = color.red();
		green = color.green();
		blue = color.blue();

		gravityStrength = (random.nextFloat() - 0.5f) * Main.CONFIG.advancedSettings.caveDustSettings.maxAcceleration;
	}

	@Override
	public void tick()
	{
		super.tick();

		if (random.nextInt(Main.CONFIG.advancedSettings.caveDustSettings.accelChangeChance) == 0)
		{
			gravityStrength = (random.nextFloat() - 0.5f) * Main.CONFIG.advancedSettings.caveDustSettings.maxAcceleration;
		}

		int fadeDuration = Main.CONFIG.advancedSettings.caveDustSettings.fadeDuration;

		if (age <= fadeDuration)
		{
			alpha = age / (float)fadeDuration;
		}
		else if (age > maxAge - fadeDuration)
		{
			alpha = (maxAge - age) / (float)fadeDuration;
		}
	}

	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<SimpleParticleType>
	{
		private final SpriteProvider provider;

		public Factory(SpriteProvider provider)
		{
			this.provider = provider;
		}

		public Particle createParticle(SimpleParticleType SimpleParticleType, ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ)
		{
			return new CaveDustParticle(clientWorld, x, y, z, 0, 0, 0, 1.0F, provider);
		}
	}
}