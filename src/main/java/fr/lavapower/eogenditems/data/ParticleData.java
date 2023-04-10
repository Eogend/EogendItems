package fr.lavapower.eogenditems.data;

import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;

public class ParticleData {
    private final Particle type;
    private final int speed;

    public ParticleData(ConfigurationSection section) {
        type = Particle.valueOf(section.getString("type"));
        speed = section.getInt("speed");
    }

    public Particle getType() {
        return type;
    }

    public int getSpeed() {
        return speed;
    }
}
