package com.crowsofwar.avatar.common.entity.data;
import com.crowsofwar.avatar.common.AvatarDamageSource;
import com.crowsofwar.avatar.common.bending.BattlePerformanceScore;
import com.crowsofwar.avatar.common.data.Bender;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.entity.EntityBoulder;
import com.crowsofwar.avatar.common.entity.EntityCloudBall;
import com.crowsofwar.avatar.common.entity.EntityFloatingBlock;
import com.crowsofwar.gorecore.util.Vector;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public abstract class BoulderBehavior extends Behavior<EntityBoulder> {

	public static final DataSerializer<BoulderBehavior> DATA_SERIALIZER = new Behavior.BehaviorSerializer<>();

	public static int  ID_NOTHING, ID_PLAYER_CONTROL, ID_THROWN;

	public static void register() {
		DataSerializers.registerSerializer(DATA_SERIALIZER);
		ID_NOTHING = registerBehavior(Idle.class);
		ID_PLAYER_CONTROL = registerBehavior(PlayerControlled.class);
		ID_THROWN = registerBehavior(Thrown.class);
	}


	public static class Idle extends BoulderBehavior {

		@Override
		public BoulderBehavior onUpdate(EntityBoulder entity) {
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}

	public static class Thrown extends BoulderBehavior {
		int time = 0;

		@Override
		public BoulderBehavior onUpdate(EntityBoulder entity) {

			time++;

			if (entity.isCollided || (!entity.world.isRemote && time > 200)) {
				entity.setDead();
				entity.onCollideWithSolid();
			}

			entity.addVelocity(0, -1 / 120, 0);

			return this;

		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}


	public static class PlayerControlled extends BoulderBehavior {

		public PlayerControlled() {
		}

		@Override
		public BoulderBehavior onUpdate(EntityBoulder entity) {
			EntityLivingBase owner = entity.getOwner();

			if (owner == null) return this;

			/*float Angle = entity.getSpeed() % 360;
			double x = Math.cos(Math.toRadians(Angle)) * entity.getRadius();
			double z = Math.sin(Math.toRadians(Angle)) * entity.getRadius();
			entity.setSpeed(entity.getSpeed() + 1);**/
			//Need to make speed increase by whatever I set it too originally
			double yaw = Math.toRadians(owner.rotationYaw);
			double pitch = Math.toRadians(owner.rotationPitch);

			Vector forward = Vector.toRectangular(yaw, pitch);
			Vector eye = Vector.getEyePos(owner);
			Vector target = forward.times(2).plus(eye);
			Vector motion = target.minus(Vector.getEntityPos(entity)).times(6);
			entity.setVelocity(motion);
			return this;
		}

		@Override
		public void fromBytes(PacketBuffer buf) {
		}

		@Override
		public void toBytes(PacketBuffer buf) {
		}

		@Override
		public void load(NBTTagCompound nbt) {
		}

		@Override
		public void save(NBTTagCompound nbt) {
		}

	}

}



