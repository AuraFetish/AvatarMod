package com.crowsofwar.avatar.common.bending.fire;

import static com.crowsofwar.avatar.common.bending.BendingType.FIREBENDING;

import java.awt.Color;

import com.crowsofwar.avatar.common.bending.BendingAbility;
import com.crowsofwar.avatar.common.bending.BendingController;
import com.crowsofwar.avatar.common.bending.BendingType;
import com.crowsofwar.avatar.common.bending.IBendingState;
import com.crowsofwar.avatar.common.controls.AvatarControl;
import com.crowsofwar.avatar.common.data.AvatarPlayerData;
import com.crowsofwar.avatar.common.gui.BendingMenuInfo;
import com.crowsofwar.avatar.common.gui.MenuTheme;
import com.crowsofwar.avatar.common.gui.MenuTheme.ThemeColor;

import net.minecraft.nbt.NBTTagCompound;

public class Firebending extends BendingController {
	
	private final BendingMenuInfo menu;
	private final BendingAbility<FirebendingState> abilityLightFire, abilityFireArc, abilityFireThrow,
			abilityFlamethrower;
	
	public Firebending() {
		
		addAbility(this.abilityLightFire = new AbilityLightFire(this));
		addAbility(this.abilityFireArc = new AbilityFireArc(this));
		addAbility(this.abilityFireThrow = new AbilityFireThrow(this));
		addAbility(this.abilityFlamethrower = new AbilityFlamethrower(this));
		
		Color light = new Color(244, 240, 187);
		Color red = new Color(173, 64, 31);
		Color gray = new Color(40, 40, 40);
		ThemeColor background = new ThemeColor(light, red);
		ThemeColor edge = new ThemeColor(red, red);
		ThemeColor icon = new ThemeColor(gray, light);
		menu = new BendingMenuInfo(new MenuTheme(background, edge, icon), AvatarControl.KEY_FIREBENDING,
				abilityLightFire, abilityFireArc);
		
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		
	}
	
	@Override
	public BendingType getType() {
		return FIREBENDING;
	}
	
	@Override
	public IBendingState createState(AvatarPlayerData data) {
		return new FirebendingState(data);
	}
	
	@Override
	public BendingMenuInfo getRadialMenu() {
		return menu;
	}
	
	@Override
	public String getControllerName() {
		return "firebending";
	}
	
}
