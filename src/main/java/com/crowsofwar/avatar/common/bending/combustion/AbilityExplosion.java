package com.crowsofwar.avatar.common.bending.combustion;

import com.crowsofwar.avatar.common.bending.Ability;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.util.Raytrace;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

public class AbilityExplosion extends Ability {

	public AbilityExplosion() {
		super(Combustionbending.ID, "explosion");

	}

	@Override
	public void execute(AbilityContext ctx) {
		EntityLivingBase entity = ctx.getBenderEntity();
		BendingData data = ctx.getData();
		Bender bender = ctx.getBender();
		World world = ctx.getWorld();

		float xp = 3F;
		float chi = ctx.getLevel() > 0 ? STATS_CONFIG.chiExplosionUpgraded : STATS_CONFIG.chiExplosion;

		if (bender.consumeChi(chi)) {
			Raytrace.Result hit = Raytrace.getTargetBlock(entity, 20);
			float explosionSize = STATS_CONFIG.explosionSettings.explosionSize;
			explosionSize += ctx.getPowerRating() * 2.0 / 100;
			if (ctx.getLevel() == 1) {
				explosionSize = STATS_CONFIG.explosionSettings.explosionSize * 1.25F;
			}
			if (ctx.getLevel() == 2) {
				hit = Raytrace.getTargetBlock(entity, 30);
				explosionSize = STATS_CONFIG.explosionSettings.explosionSize * 1.5F;
			}
			if (ctx.isMasterLevel(AbilityData.AbilityTreePath.FIRST)) {
				hit = Raytrace.getTargetBlock(entity, 200);
				explosionSize = STATS_CONFIG.explosionSettings.explosionSize * 1.25F;
			}
			if (ctx.isMasterLevel(AbilityData.AbilityTreePath.SECOND)) {
				explosionSize = STATS_CONFIG.explosionSettings.explosionSize * 3F;
			}
			if (hit.hitSomething()) {
				Vector hitAt = hit.getPosPrecise();
				world.createExplosion(entity, hitAt.x(), hitAt.y(), hitAt.z(), explosionSize, false);
				if (ctx.getLevel() <= 0) {
					data.getAbilityData("explosion").addXp(xp);
				}
				if (ctx.getLevel() == 1) {
					data.getAbilityData("explosion").addXp(xp - 1F);
				}
				if (ctx.getLevel() == 2) {
					data.getAbilityData("explosion").addXp(xp - 2F);
				}
			}
		}
	}
}
