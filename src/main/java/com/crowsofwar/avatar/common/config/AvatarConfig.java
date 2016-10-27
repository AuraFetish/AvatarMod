package com.crowsofwar.avatar.common.config;

import com.crowsofwar.gorecore.config.ConfigLoader;
import com.crowsofwar.gorecore.config.Load;

/**
 * 
 * 
 * @author CrowsOfWar
 */
public class AvatarConfig {
	
	public static final AvatarConfig CONFIG = new AvatarConfig();
	
	@Load
	public AttackSettings floatingBlockSettings = new AttackSettings(0.25f, 1),
			ravineSettings = new AttackSettings(7, 0.25), //
			waveSettings = new AttackSettings(9, 6);
	
	public static void load() {
		
		ConfigLoader.load(CONFIG, "avatar/balance.cfg");
		
	}
	
	public static class AttackSettings {
		
		@Load
		public float damage;
		
		@Load
		public double push;
		
		public AttackSettings() {}
		
		private AttackSettings(float damage, double push) {
			this.damage = damage;
			this.push = push;
		}
		
	}
	
}
