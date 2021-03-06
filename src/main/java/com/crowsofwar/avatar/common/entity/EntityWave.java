/* 
  This file is part of AvatarMod.
    
  AvatarMod is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  
  AvatarMod is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  
  You should have received a copy of the GNU General Public License
  along with AvatarMod. If not, see <http://www.gnu.org/licenses/>.
*/

package com.crowsofwar.avatar.common.entity;

import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BattlePerformanceScore;
import com.crowsofwar.avatar.common.bending.water.AbilityCreateWave;
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

public class EntityWave extends AvatarEntity {

	private static final DataParameter<Float> SYNC_SIZE = EntityDataManager.createKey(EntityWave.class,
			DataSerializers.FLOAT);

	private float damageMult;
	private boolean createExplosion;
	private float Size;
	private Vector initialSpeed;
	private int groundTime;

	public EntityWave(World world) {
		super(world);
		this.Size = 2;
		setSize(Size, Size * 0.75F);
		damageMult = 1;
		this.putsOutFires = true;
		this.initialSpeed = this.velocity();
		this.groundTime = 0;
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(SYNC_SIZE, Size);
	}

	public void setDamageMultiplier(float damageMult) {
		this.damageMult = damageMult;
	}

	public float getWaveSize() {
		return dataManager.get(SYNC_SIZE);
	}

	public void setWaveSize(float size) {
		this.Size = size;
		dataManager.set(SYNC_SIZE, size);
	}

	@Override
	public boolean canBeCollidedWith() {
		return false;
	}

	@Override
	protected boolean canCollideWith(Entity entity) {
		return entity != this && entity != getOwner();
	}

	@Override
	public boolean canBePushed() {
		return false;
	}

	@Override
	public void onUpdate() {

		super.onUpdate();
		this.noClip = true;


		if (this.velocity() == Vector.ZERO || (this.velocity().magnitude() < (initialSpeed.magnitude() / 100))) {
			this.setDead();
		}

		if (getAbility() instanceof AbilityCreateWave && getOwner() != null && !world.isRemote) {
			BendingData data = BendingData.get(getOwner());
			AbilityData lvl = data.getAbilityData(getAbility().getName());

			if (lvl.isMasterPath(AbilityData.AbilityTreePath.FIRST)) {
				setSize(Size * 2, Size * 0.75F * 2);
			} else {
				setSize(Size, Size * 0.75F);
			}
		}

		if (!this.inWater) {
			this.setVelocity(velocity().dividedBy(40));
			groundTime++;
		}

		EntityLivingBase owner = getOwner();

		Vector move = velocity().dividedBy(20);
		Vector newPos = position().plus(move);
		setPosition(newPos.x(), newPos.y(), newPos.z());


		if (!world.isRemote) {
			WorldServer World = (WorldServer) world;
			World.spawnParticle(EnumParticleTypes.WATER_WAKE, posX, posY, posZ, 300, getWaveSize() / 2.5, getWaveSize() / 5, getWaveSize() / 2.5, 0);
			World.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, posX, posY + (Size * 0.75F), posZ, 1, getWaveSize() / 5, getWaveSize() / 20, getWaveSize() / 5, 0);
			World.spawnParticle(EnumParticleTypes.WATER_SPLASH, posX, posY + (Size * 0.75F), posZ, 30, getWaveSize() / 5, 0, getWaveSize() / 5, 0);

			List<Entity> collided = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(), entity -> entity != owner);
			for (Entity entity : collided) {
				Vector motion = velocity().dividedBy(20).times(STATS_CONFIG.waveSettings.push);
				motion = motion.withY(Size * 0.75 / 10);
				entity.addVelocity(motion.x(), motion.y(), motion.z());

				if (entity.attackEntityFrom(AvatarDamageSource.causeWaveDamage(entity, owner),
						STATS_CONFIG.waveSettings.damage * damageMult)) {

					AbilityData.get(owner, getAbility().getName()).addXp(SKILLS_CONFIG.waveHit);
					BattlePerformanceScore.addLargeScore(getOwner());

				}

				if (createExplosion) {
					world.createExplosion(null, posX, posY, posZ, 2, false);
				}

			}
		}

		if (groundTime >= 30) {
			this.setDead();
		}

		if (ticksExisted >= 250) {
			this.setDead();
		}
		//setSize(Size, Size * 0.75F);

	}


	@Override
	protected void onCollideWithEntity(Entity entity) {
		if (entity instanceof AvatarEntity) {
			((AvatarEntity) entity).onMajorWaterContact();
		}
	}

	@Override
	public boolean onCollideWithSolid() {
		return false;
	}

	@Override
	public boolean shouldRenderInPass(int pass) {
		return true;
	}

	public void setCreateExplosion(boolean createExplosion) {
		this.createExplosion = createExplosion;
	}

}
