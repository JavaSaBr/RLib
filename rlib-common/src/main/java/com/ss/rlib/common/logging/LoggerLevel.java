package com.ss.rlib.common.logging;

import org.jetbrains.annotations.NotNull;

/**
 * The list of logging levels.
 *
 * @author JavaSaBr
 */
public enum LoggerLevel {
    /**
     * Info logger level.
     */
    INFO("INFO", false, false),
    /**
     * Debug logger level.
     */
    DEBUG("DEBUG", false, false),
    /**
     * Warning logger level.
     */
    WARNING("WARNING", true, true),
    /**
     * Error logger level.
     */
    ERROR("ERROR", true, true);


    public static final int LENGTH = values().length;

    /**
     * The level title.
     */
    @NotNull
    private String title;

    /**
     * The flag of activity.
     */
    private boolean enabled;

    /**
     * The flag of force flushing.
     */
    private boolean forceFlush;

    LoggerLevel(@NotNull String title, boolean forceFlush, boolean enabled) {
        this.title = title;
        this.forceFlush = forceFlush;
        this.enabled = enabled;
    }

    /**
     * Get the level title.
     *
     * @return the level title.
     */
    public @NotNull String getTitle() {
        return title;
    }

    /**
     * Set the level title.
     *
     * @param title the level title.
     */
    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    /**
     * Return true if need force flush.
     *
     * @return true if need force flush.
     */
    public boolean isForceFlush() {
        return forceFlush;
    }

    /**
     * Return true if this level is enabled.
     *
     * @return true if this level is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Set true if this level should be enabled.
     *
     * @param enabled true if this level should be enabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return title;
    }
}
