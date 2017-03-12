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

import com.crowsofwar.avatar.common.bending.StatusControl;
import com.crowsofwar.avatar.common.data.BendingData;
import com.crowsofwar.avatar.common.data.ctx.AbilityContext;
import com.crowsofwar.avatar.common.entity.EntityAirBubble;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AbilityAirBubble extends AirAbility {
	
	public AbilityAirBubble() {
		super("air_bubble");
	}
	
	@Override
	public void execute(AbilityContext ctx) {
		EntityLivingBase bender = ctx.getBenderEntity();
		World world = ctx.getWorld();
		BendingData data = ctx.getData();
		
		if (!data.hasStatusControl(StatusControl.BUBBLE_CONTRACT)) {
			
			if (!ctx.consumeChi(STATS_CONFIG.chiAirBubble)) return;
			
			float xp = data.getAbilityData(this).getTotalXp();
			
			EntityAirBubble bubble = new EntityAirBubble(world);
			bubble.setOwner(bender);
			bubble.setPosition(bender.posX, bender.posY, bender.posZ);
			bubble.setHealth(15 + xp / 10f);
			world.spawnEntityInWorld(bubble);
			
			data.addStatusControl(StatusControl.BUBBLE_EXPAND);
			data.addStatusControl(StatusControl.BUBBLE_CONTRACT);
		}
		
	}
	
}
