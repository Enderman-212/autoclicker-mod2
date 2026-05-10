package com.autoclicker.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

public class AutoClickerClient implements ClientModInitializer {

    private boolean autoClickEnabled = false;
    private boolean capsLockWasPressed = false;
    private int tickCounter = 0;
    private static final int TICKS_PER_CLICK = 1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        System.out.println("[AutoClicker] Loaded! Press CapsLock to toggle 20 CPS.");
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        long window = client.getWindow().getHandle();

        int capsState = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_CAPS_LOCK);
        boolean capsLockPressed = (capsState == GLFW.GLFW_PRESS);

        if (capsLockPressed && !capsLockWasPressed) {
            autoClickEnabled = !autoClickEnabled;
            String status = autoClickEnabled ? "§aENABLED §7(20 CPS)" : "§cDISABLED";
            client.player.sendMessage(Text.literal("§6[AutoClicker] §r" + status), true);
        }
        capsLockWasPressed = capsLockPressed;

        if (autoClickEnabled && client.currentScreen == null) {
            tickCounter++;
            if (tickCounter >= TICKS_PER_CLICK) {
                tickCounter = 0;
                ClientPlayerInteractionManager manager = client.interactionManager;
                if (manager != null && client.crosshairTarget != null) {
                    client.options.attackKey.setPressed(true);
                    if (client.targetedEntity != null) {
                        manager.attackEntity(client.player, client.targetedEntity);
                    }
                    client.player.swingHand(Hand.MAIN_HAND);
                    client.options.attackKey.setPressed(false);
                }
            }
        }
    }
}
