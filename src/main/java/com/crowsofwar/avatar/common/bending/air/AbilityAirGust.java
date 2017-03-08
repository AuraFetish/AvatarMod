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

package com.crowsofwar.avatar.common.bending.air;

import static com.crowsofwar.avatar.common.config.ConfigStats.STATS_CONFIG;

import com.crowsofwar.avatar.common.data.AbilityContext;
import com.crowsofwar.avatar.common.entity.EntityAirGust;
import com.crowsofwar.gorecore.util.Vector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirGust extends AirAbility {
	
	/**
	 * @param controller
	 */
	public AbilityAirGust() {
		super("air_gust");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		
		EntityPlayer player = ctx.getPlayerEntity();
		World world = ctx.getWorld();
		
		if (!ctx.consumeChi(STATS_CONFIG.chiAirGust)) return;
		
		Vector look = Vector.toRectangular(Math.toRadians(player.rotationYaw),
				Math.toRadians(player.rotationPitch));
		Vector pos = Vector.getEyePos(player);
		
		EntityAirGust gust = new EntityAirGust(world);
		gust.velocity().set(look.times(25));
		gust.setPosition(pos.x(), pos.y(), pos.z());
		gust.setOwner(player);
		
		world.spawnEntityInWorld(gust);
	}
	
}
