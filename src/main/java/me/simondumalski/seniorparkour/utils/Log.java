package me.simondumalski.seniorparkour.utils;

public enum Log {

    DATA_INITIALIZING_FILE("&aInitializing parkour-data.yml..."),
    DATA_CREATING_FILE("&aCreating parkour-data.yml..."),
    DATA_INITIALIZED_FILE("&aInitialized parkour-data.yml!"),
    DATA_ERROR_INITIALIZING_FILE("&cError initializing parkour-data.yml!"),

    DATA_LOADING_PARKOURS("&aLoading parkour courses from file..."),
    DATA_LOADED_PARKOURS("&aLoaded %args0% parkour courses!"),
    DATA_NO_PARKOURS("&aNo parkour courses to load!"),
    DATA_ERROR_LOADING_PARKOURS("&cError loading parkour courses from file!"),

    DATA_SAVING_PARKOURS("&aSaving parkour courses to file..."),
    DATA_NO_END_LOCATION("&cParkour %args0% has no end point and will not be saved!"),
    DATA_SAVED_PARKOURS("&aSaved %args0% parkour courses!"),
    DATA_ERROR_SAVING_PARKOURS("&cError saving parkour courses to file!"),

    DATABASE_CONNECTED("&aConnected to mysql database!"),
    DATABASE_DISCONNECTED("&aDisconnected from mysql database!"),
    DATABASE_ERROR_CONNECTING("&cError connecting to mysql database! Player data will not be saved."),
    DATABASE_ERROR_DISCONNECTING("&cError disconnecting from mysql database!");

    private final String message;

    Log(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
