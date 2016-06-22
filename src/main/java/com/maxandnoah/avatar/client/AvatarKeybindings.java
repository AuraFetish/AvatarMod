package com.maxandnoah.avatar.client;

import java.util.HashMap;
import static com.maxandnoah.avatar.common.AvatarControlList.*;
import java.util.Map;

import org.lwjgl.input.Keyboard;

import com.maxandnoah.avatar.AvatarLog;
import com.maxandnoah.avatar.common.IKeybindingManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.settings.KeyBinding;

@SideOnly(Side.CLIENT)
public class AvatarKeybindings implements IKeybindingManager {
	
	private Map<String, KeyBinding> keybindings;
	
	public AvatarKeybindings() {
		keybindings = new HashMap();
		
		addKeybinding(CONTROL_BENDING_LIST, Keyboard.KEY_Z, "main");
		addKeybinding(CONTROL_CHEAT_EARTHBENDING, Keyboard.KEY_X, "main");
		addKeybinding(CONTROL_TOGGLE_BENDING, Keyboard.KEY_B, "main");
		addKeybinding(CONTROL_THROW_BLOCK, Keyboard.KEY_N, "main");
		
	}

	private KeyBinding addKeybinding(String langRef, int key, String cat) {
		KeyBinding kb = new KeyBinding("avatar." + langRef, key,
				"avatar.category." + cat);
		keybindings.put(langRef, kb);
		ClientRegistry.registerKeyBinding(kb);
		return kb;
		
	}
	
	@Override
	public boolean isKeyPressed(String keyName) {
		KeyBinding kb = keybindings.get(keyName);
		if (kb == null) AvatarLog.warn("Key control '" + keyName + "' is undefined");
		return kb == null ? false : kb.isPressed();
	}
	
}
