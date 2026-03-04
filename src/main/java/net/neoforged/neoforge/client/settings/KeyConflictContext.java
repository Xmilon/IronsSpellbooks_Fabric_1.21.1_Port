package net.neoforged.neoforge.client.settings;

public class KeyConflictContext implements IKeyConflictContext {
    public static final KeyConflictContext UNIVERSAL = new KeyConflictContext();
    public static final KeyConflictContext IN_GAME = new KeyConflictContext();
}
