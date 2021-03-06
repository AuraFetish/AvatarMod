package com.crowsofwar.avatar.common.bending.fire;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;

import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.data.*;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;
import static net.minecraft.init.MobEffects.*;

public class AbilityPurify extends Ability {

	public AbilityPurify() {
		super(Firebending.ID, "purify");
	}

	@Override
	public boolean isBuff() {
		return true;
	}

	@Override
	public void execute(AbilityContext ctx) {

		BendingData data = ctx.getData();
		EntityLivingBase entity = ctx.getBenderEntity();
		Bender bender = ctx.getBender();
		AbilityData abilityData = data.getAbilityData(this);

		float chi = STATS_CONFIG.chiBuff;
		if (abilityData.getLevel() == 1) {
			chi = STATS_CONFIG.chiBuffLvl2;
		} else if (abilityData.getLevel() == 2) {
			chi = STATS_CONFIG.chiBuffLvl3;
		} else if (abilityData.getLevel() == 3) {
			chi = STATS_CONFIG.chiBuffLvl4;
		}

		if (bender.consumeChi(chi)) {

			// 3s base + 2s per level
			int duration = 60 + 40 * abilityData.getLevel();
			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
				duration = 200;
			}

			int effectLevel = abilityData.getLevel() >= 2 ? 1 : 0;
			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.SECOND)) {
				effectLevel = 2;
			}

			entity.addPotionEffect(new PotionEffect(STRENGTH, duration, effectLevel + 1));

			if (abilityData.getLevel() < 2) {
				entity.setFire(1);
			}

			if (abilityData.getLevel() >= 1) {
				entity.addPotionEffect(new PotionEffect(SPEED, duration, effectLevel));
			}

			if (abilityData.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
				entity.addPotionEffect(new PotionEffect(HEALTH_BOOST, duration, effectLevel));
			}

			if (data.hasBendingId(getBendingId())) {

				PurifyPowerModifier modifier = new PurifyPowerModifier();
				modifier.setTicks(duration);

				// Ignore warning; we know manager != null if they have the bending style
				data.getPowerRatingManager(getBendingId()).addModifier(modifier, ctx);

			}

			abilityData.addXp(SKILLS_CONFIG.buffUsed);

		}
	}

	@Override
	public int getCooldown(AbilityContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();

		int coolDown = 200;

		if (entity instanceof EntityPlayer && ((EntityPlayer) entity).isCreative()) {
			coolDown = 0;
		}

		if (ctx.getLevel() == 1) {
			coolDown = 180;
		}
		if (ctx.getLevel() == 2) {
			coolDown = 160;
		}
		if (ctx.isMasterLevel(AbilityData.AbilityTreePath.FIRST)) {
			coolDown = 120;
		}
		if (ctx.isMasterLevel(AbilityData.AbilityTreePath.SECOND)) {
			coolDown = 120;
		}
		return coolDown;
	}
}
