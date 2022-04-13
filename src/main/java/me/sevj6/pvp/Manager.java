package me.sevj6.pvp;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
@Getter
public abstract class Manager {

    @NonNull
    private final String name;

    protected File dataFolder;

    public abstract void init(PVPServer plugin);

    public abstract void destruct(PVPServer plugin);

    public abstract void reload(PVPServer plugin);
}