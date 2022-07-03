package me.sevj6.pvp.util;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
import com.sk89q.worldedit.session.ClipboardHolder;
import me.sevj6.pvp.PVPServer;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Objects;

public class WorldEditUtil {

    public static File dataFolder = PVPServer.getInstance().getSchematicsDataFolder();

    public static void pasteSchematic(World world, String schematicName, Location pasteLocation) throws Throwable {
        File schemFile = Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).filter(file -> file.getName().endsWith(".schematic")).filter(file -> file.getName().split("\\.")[0].equals(schematicName)).findFirst().orElse(null);
        if (schemFile == null) return;
        if (!schemFile.exists()) return;
        ClipboardFormat format = ClipboardFormat.findByFile(schemFile);
        if (format == null) return;
        FileInputStream fis = new FileInputStream(schemFile);
        ClipboardReader reader = format.getReader(new FileInputStream(schemFile));
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, -1);
        Clipboard clipboard = reader.read(bukkitWorld.getWorldData());
        Operation operation = new ClipboardHolder(clipboard, bukkitWorld.getWorldData())
                .createPaste(session, bukkitWorld.getWorldData())
                .to(Vector.toBlockPoint(pasteLocation.getX(), pasteLocation.getY(), pasteLocation.getZ()))
                .build();
        Operations.complete(operation);
        session.flushQueue();
        fis.close();
    }

    public static void pasteSchematicMcEdit(World world, String schematicName, Location pasteLocation) throws Throwable {
        File schemFile = Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).filter(file -> file.getName().endsWith(".schematic")).filter(file -> file.getName().split("\\.")[0].equals(schematicName)).findFirst().orElse(null);
        if (schemFile == null) return;
        if (!schemFile.exists()) return;
        ClipboardFormat format = ClipboardFormat.findByFile(schemFile);
        if (format == null) return;
        BukkitWorld bukkitWorld = new BukkitWorld(world);
        EditSession session = WorldEdit.getInstance().getEditSessionFactory().getEditSession(bukkitWorld, -1);
        MCEditSchematicFormat.getFormat(schemFile).load(schemFile).paste(session, new Vector(pasteLocation.getX(), pasteLocation.getY(), pasteLocation.getZ()), false, false);
    }
}
