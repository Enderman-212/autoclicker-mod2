package com.autoclicker.mod;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import org.lwjgl.glfw.GLFW;
import java.awt.Robot;
import java.awt.AWTException;
import java.awt.event.InputEvent;

public class AutoClickerClient implements ClientModInitializer {

    private boolean autoClickEnabled = false;
    private boolean capsLockWasPressed = false;
    private int tickCounter = 0;

    // 20 CPS = 1 click every 3 ticks (game runs at 20 TPS, but client ticks faster)
    // We'll use a timer approach: 20 clicks per second = click every 50ms
    // At 20 TPS (client tick), 1 tick = 50ms, so click every tick
    private static final int TICKS_PER_CLICK = 1; // 1 tick = 50ms = 20 CPS

    private Robot robot;

    @Override
    public void onInitializeClient() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.err.println("[AutoClicker] Failed to initialize Robot: " + e.getMessage());
        }

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
        System.out.println("[AutoClicker] AutoClicker mod loaded! Press CapsLock to toggle.");
    }

    private void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        long window = client.getWindow().getHandle();

        // Detect CapsLock key press (rising edge detection)
        int capsState = GLFW.glfwGetKey(window, GLFW.GLFW_KEY_CAPS_LOCK);
        boolean capsLockPressed = (capsState == GLFW.GLFW_PRESS);

        if (capsLockPressed && !capsLockWasPressed) {
            autoClickEnabled = !autoClickEnabled;
            String status = autoClickEnabled ? "§aENABLED §7(20 CPS)" : "§cDISABLED";
            client.player.sendMessage(Text.literal("§6[AutoClicker] §r" + status), true);
        }
        capsLockWasPressed = capsLockPressed;

        // Perform auto click
        if (autoClickEnabled && robot != null) {
            tickCounter++;
            if (tickCounter >= TICKS_PER_CLICK) {
                tickCounter = 0;
                performClick(client);
            }
        }
    }

    private void performClick(MinecraftClient client) {
        if (client.currentScreen != null) return; // Don't click when a GUI is open

        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }
}
