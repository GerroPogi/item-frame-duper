package dev.beronicous.addon;

import com.mojang.logging.LogUtils;
import dev.beronicous.addon.modules.ItemFrameDupe;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class Addon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();


    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        Modules.get().add(new ItemFrameDupe());
    }

    @Override
    public String getPackage() {
        return "dev.beronicous.addon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("GerroPogi", "item-frame-dupe");
    }
}
