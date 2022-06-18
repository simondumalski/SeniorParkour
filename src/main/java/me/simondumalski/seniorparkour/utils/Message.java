package me.simondumalski.seniorparkour.utils;

public enum Message {

    PLUGIN_PREFIX("messages.prefix"),

    COMMAND_CHECKPOINT("messages.commands.checkpoint"),
    COMMAND_CREATE("messages.commands.create"),
    COMMAND_DELETE("messages.commands.delete"),
    COMMAND_END("messages.commands.end"),
    COMMAND_TELEPORT("messages.commands.teleport"),

    ERROR_UNKNOWN_COMMAND("messages.errors.unknown-command"),
    ERROR_INSUFFICIENT_PERMISSIONS("messages.errors.insufficient-permissions"),
    ERROR_PLAYERS_ONLY("messages.errors.players-only"),
    ERROR_INVALID_USAGE("messages.errors.invalid-usage"),
    ERROR_INVALID_PARKOUR("messages.errors.invalid-parkour"),
    ERROR_INVALID_CHECKPOINT("messages.errors.invalid-checkpoint"),
    ERROR_PARKOUR_NAME_TAKEN("messages.errors.parkour-name-taken"),
    ERROR_ALREADY_IN_PARKOUR("messages.errors.already-in-parkour"),
    ERROR_MUST_LEAVE_PARKOUR("messages.errors.must-leave-parkour"),
    ERROR_ALREADY_PASSED_CHECKPOINT("messages.errors.already-passed-checkpoint"),
    ERROR_MUST_PASS_PREVIOUS_CHECKPOINT("messages.errors.must-pass-previous-checkpoint"),
    ERROR_NOT_IN_PARKOUR("messages.errors.not-in-parkour"),
    ERROR_PARKOUR_TIMEOUT("messages.errors.parkour-timeout"),
    ERROR_NO_END_POINT("messages.errors.no-end-point"),
    ERROR_INVALID_PLAYER("messages.errors.invalid-player"),
    ERROR_NO_PLAYER_DATA_TO_SHOW("messages.errors.no-player-data-to-show"),
    ERROR_NO_PARKOUR_DATA_TO_SHOW("messages.errors.no-parkour-data-to-show"),

    SUCCESS_PARKOUR_CREATED("messages.success.parkour-created"),
    SUCCESS_CHECKPOINT_CREATED("messages.success.checkpoint-created"),
    SUCCESS_END_POINT_SET("messages.success.end-point-set"),
    SUCCESS_PARKOUR_DELETED("messages.success.parkour-deleted"),
    SUCCESS_CHECKPOINT_DELETED("messages.success.checkpoint-deleted"),
    SUCCESS_PARKOUR_TELEPORTED("messages.success.parkour-teleported"),
    SUCCESS_CHECKPOINT_TELEPORTED("messages.success.checkpoint-teleported"),
    SUCCESS_STARTED_PARKOUR("messages.success.started-parkour"),
    SUCCESS_PASSED_CHECKPOINT("messages.success.passed-checkpoint"),
    SUCCESS_FINISHED_PARKOUR("messages.success.finished-parkour"),
    SUCCESS_CONFIG_RELOADED("messages.success.config-reloaded");

    private final String configValue;

    Message(String configValue) {
        this.configValue = configValue;
    }

    /**
     * Returns the config value of the message
     * @return Config value
     */
    public String getConfigValue() {
        return configValue;
    }

}
