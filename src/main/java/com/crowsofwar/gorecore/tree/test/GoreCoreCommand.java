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
package com.crowsofwar.gorecore.tree.test;

import com.crowsofwar.gorecore.format.FormattedMessage;
import com.crowsofwar.gorecore.tree.*;
import com.crowsofwar.gorecore.util.AccountUUIDs;

import static com.crowsofwar.gorecore.tree.test.GoreCoreChatMessages.*;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class GoreCoreCommand extends TreeCommand {
	
	@Override
	public String getName() {
		return "gorecore";
	}
	
	@Override
	protected ICommandNode[] addCommands() {
		
		NodeFunctional reloadId = new NodeBuilder("fixid")//
				.addArgument(new ArgumentPlayerName("player"))//
				.addArgumentDirect("confirm", ITypeConverter.CONVERTER_STRING, "")//
				.build(popper -> {
					
					String username = popper.get();
					String confirm = popper.get();
					
					if (!confirm.equals("destroy-data")) {
						MSG_FIXID_CONFIRM.send(popper.from(), username);
						MSG_FIXID_CONFIRM2.send(popper.from(), username);
						return;
					}
					
					FormattedMessage msg;
					if (AccountUUIDs.getId(username).isTemporary()) {
						msg = AccountUUIDs.tryFixId(username) ? MSG_FIXID_SUCCESS : MSG_FIXID_FAILURE;
					} else {
						msg = MSG_FIXID_ONLINE;
					}
					msg.send(popper.from(), username);
					
				});
		
		return new ICommandNode[] { reloadId };
	}
	
}
