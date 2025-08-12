package me.zmaster.zgui.icon;

/**
 * Defines the priority levels for icon handlers.
 * <p>
 * Icons with higher priority are processed before those with lower priority.
 * This allows controlling the order in which icon-related methods are invoked.
 */
public enum IconPriority {
    LOWEST, LOW, MEDIUM, HIGH, HIGHEST;
}
