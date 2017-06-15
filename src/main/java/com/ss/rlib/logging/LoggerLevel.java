package com.ss.rlib.logging;

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
    INFO("INFO", false),
    /**
     * Debug logger level.
     */
    DEBUG("DEBUG", false),
    /**
     * Warning logger level.
     */
    WARNING("WARNING", true),
    /**
     * Error logger level.
     */
    ERROR("ERROR", true);

    /**
     * The constant LENGTH.
     */
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

    LoggerLevel(final String title, final boolean forceFlush) {
        this.title = title;
        this.forceFlush = forceFlush;
    }

    /**
     * Gets title.
     *
     * @return the level title.
     */
    @NotNull
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the level title.
     */
    @NotNull
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * Is force flush boolean.
     *
     * @return true if need force flush.
     */
    public boolean isForceFlush() {
        return forceFlush;
    }

    /**
     * Is enabled boolean.
     *
     * @return true if this level is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets enabled.
     *
     * @param enabled the flag of activity.
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return title;
    }
}
