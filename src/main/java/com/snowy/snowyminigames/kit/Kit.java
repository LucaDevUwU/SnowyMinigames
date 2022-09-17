package com.snowy.snowyminigames.kit;

import java.util.UUID;

public abstract class Kit {

    private KitType type;
    private UUID uuid;

    public Kit(KitType type, UUID uuid) {
        this.type = type;
        this.uuid = uuid;
    }

    public UUID getUUID() { return uuid; }
    public KitType getType() { return type; }

    public abstract void onStart();
}
