package fastercrystals.fastercrystals;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class FasterCrystals extends JavaPlugin implements @NotNull Listener {
  private ProtocolManager protocolManager;

  @Override
  public void onEnable() {
    // Plugin startup logic
    getServer().getPluginManager().registerEvents(this, this);
    protocolManager = ProtocolLibrary.getProtocolManager();
    getLogger().info("FasterCrystals Enabled");
    getConfig().addDefault("ping", "150");
    saveDefaultConfig();
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    getLogger().info("FasterCrystals Disabled");
  }

  @EventHandler(priority = EventPriority.HIGHEST)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    int pingConfig = getConfig().getInt("ping");
    if (event.getDamager() instanceof Player player && event.getEntity() instanceof EnderCrystal &&
        player.getPing() >= pingConfig) {
      PacketContainer destroyEntityPacket =
          new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);

      List<Integer> crystalIDs = new ArrayList<>();
      crystalIDs.add(event.getEntity().getEntityId());
      destroyEntityPacket.getIntLists().write(0, crystalIDs);

      protocolManager.sendServerPacket(player, destroyEntityPacket);
    }
  }
}
