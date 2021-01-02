package io.github.thebusybiscuit.electricspawners;

import java.util.logging.Level;

import org.bstats.bukkit.Metrics;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import io.github.thebusybiscuit.slimefun4.core.researching.Research;
import me.mrCookieSlime.Slimefun.Objects.Category;
import me.mrCookieSlime.Slimefun.cscorelib2.config.Config;
import me.mrCookieSlime.Slimefun.cscorelib2.item.CustomItem;
import me.mrCookieSlime.Slimefun.cscorelib2.skull.SkullItem;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.GitHubBuildsUpdater;
import me.mrCookieSlime.Slimefun.cscorelib2.updater.Updater;

public class ElectricSpawners extends JavaPlugin implements Listener, SlimefunAddon {

    @Override
    public void onEnable() {
        Config cfg = new Config(this);

        // Setting up bStats
        new Metrics(this, 6163);

        if (getDescription().getVersion().startsWith("DEV - ")) {
            Updater updater = new GitHubBuildsUpdater(this, getFile(), "TheBusyBiscuit/ElectricSpawners/master");

            // Only run the Updater if it has not been disabled
            if (cfg.getBoolean("options.auto-update")) {
                updater.start();
            }
        }

        Category category = new Category(new NamespacedKey(this, "electric_spawners"), new CustomItem(SkullItem.fromHash("db6bd9727abb55d5415265789d4f2984781a343c68dcaf57f554a5e9aa1cd"), "&9电动刷怪笼"));
        Research research = new Research(new NamespacedKey(this, "electric_spawners"), 4820, "动力刷怪笼", 30);

        for (String mob : cfg.getStringList("mobs")) {
            try {
                EntityType type = EntityType.valueOf(mob);
                new ElectricSpawner(category, mob, type, research).register(this);
            }
            catch (IllegalArgumentException x) {
                getLogger().log(Level.WARNING, "为（可能已过时或无效）实体类型添加动力刷怪笼时发生错误 \"{0}\"", mob);
            }
            catch (Exception x) {
                getLogger().log(Level.SEVERE, x, () -> "为EntityType添加动力刷怪笼时发生错误  \"" + mob + "\"");
            }
        }

        research.register();
    }

    @Override
    public JavaPlugin getJavaPlugin() {
        return this;
    }

    @Override
    public String getBugTrackerURL() {
        return "https://github.com/TheBusyBiscuit/ElectricSpawners/issues";
    }
}
