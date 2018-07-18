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
import com.crowsofwar.avatar.common.data.AbilityData;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.util.AvatarUtils;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

import static com.crowsofwar.avatar.common.config.ConfigSkills.SKILLS_CONFIG;
import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

/**
 * @author CrowsOfWar
 */
public class EntityEarthspike extends AvatarEntity {

	//private static final DataParameter<Float> SYNC_SIZE = EntityDataManager
	//		.createKey(EntityEarthspike.class, DataSerializers.FLOAT);

	private double damage;
	//private float Size = 1;

	public EntityEarthspike(World world) {
		super(world);
		setSize(1, 1);
		this.damage = STATS_CONFIG.earthspikeSettings.damage;
	}

	public void setDamage(double damage) {
		this.damage = damage;
	}

//	public void setSize (float size) {
	//	dataManager.set(SYNC_SIZE, size);
	//}

	//public float getSize() {
	//	return dataManager.get(SYNC_SIZE);
	//}

	@Override
	protected void entityInit() {
		super.entityInit();
		//dataManager.register(SYNC_SIZE, Size);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
	}

	@Override
	protected boolean canCollideWith(Entity entity) {
		if (entity instanceof AvatarEntity) {
			if (((AvatarEntity) entity).getOwner() == getOwner()) {
				return false;
			}
		}
		return entity != getOwner() && !(entity instanceof EntityItem) && !(entity == this) && entity instanceof EntityLivingBase && !(entity instanceof EntityEarthspikeSpawner);

	}

	@Override
	public void onEntityUpdate() {

		super.onEntityUpdate();

		setVelocity(Vector.ZERO);
		if (ticksExisted >= 15) {
			this.setDead();
		}

		// amount of entities which were successfully attacked
		int attacked = 0;

		// Push collided entities back
		if (!world.isRemote) {
			List<Entity> collided = world.getEntitiesInAABBexcluding(this, getEntityBoundingBox(),
					entity -> entity != getOwner());
			if (!collided.isEmpty()) {
				for (Entity entity : collided) {
					System.out.println(entity);
					onCollideWithEntity(entity);
					attacked++;

				}
			}
			if (getOwner() != null) {
				BendingData data = BendingData.get(getOwner());
				if (data != null && !world.isRemote) {
					data.getAbilityData(getAbility().getName()).addXp(SKILLS_CONFIG.earthspikeHit * attacked);
				}
			}
		}
	}

	@Override
	protected void onCollideWithEntity(Entity entity) {
		if (!world.isRemote) {
			pushEntity(entity);
//			AvatarUtils.afterVelocityAdded(entity);
//			damageEntity((EntityLivingBase) entity);
			if (getOwner() != null && !world.isRemote) {
				BattlePerformanceScore.addMediumScore(getOwner());
			}
			System.out.println(entity);
		}
	}


	private void damageEntity(EntityLivingBase entity) {

		if (world.isRemote) {
			return;
		}

		DamageSource damageSource = AvatarDamageSource.causeWaterCannonDamage(entity, getOwner());
		if (entity.attackEntityFrom(damageSource, (float) damage)) {

			BattlePerformanceScore.addMediumScore(getOwner());

			if (getOwner() != null) {
				BendingData data = BendingData.get(getOwner());
				AbilityData abilityData = data.getAbilityData(getAbility().getName());
				abilityData.addXp(SKILLS_CONFIG.earthspikeHit);
			}
		}

	}


	private void pushEntity(Entity entity) {
		Vector entityPos = Vector.getEntityPos(entity);
		Vector direction = entityPos.minus(this.position());
		Vector velocity = direction.times(STATS_CONFIG.earthspikeSettings.push);
		entity.addVelocity(velocity.x() / 5, velocity.y(), velocity.z() / 5);
	}

	@Override
	public boolean isProjectile() {
		return true;
	}
}
