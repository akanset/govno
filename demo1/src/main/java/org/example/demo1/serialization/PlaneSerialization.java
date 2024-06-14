package org.example.demo1.serialization;

import javafx.scene.Group;
import org.example.demo1.CruiserPlane;
import org.example.demo1.FighterPlane;
import org.example.demo1.LightPlane;

import java.io.Serializable;

public class PlaneSerialization implements Serializable {

    private double spd;
    private double hp;
    private int dmg;
    private String name;
    private String[] systemStats = new String[4];
    private boolean isActive;
    private int x = 0;
    private int y = 0;
    private PlaneLevel level;

    public PlaneSerialization(LightPlane plane) {
        this.spd = plane.getSpeed();
        this.hp = plane.getHP();
        this.dmg = plane.getDamage();
        this.name = plane.getName();
        this.x = plane.getX();
        this.y = plane.getY();
        this.isActive = plane.isActive();
        this.systemStats = plane.getSystemStats();

        if(plane instanceof CruiserPlane) {
            level = PlaneLevel.CRUISER;
        }
        else if(plane instanceof FighterPlane) {
            level = PlaneLevel.FIGHTER;
        }
        else {
            level = PlaneLevel.LIGHT;
        }
    }

    public LightPlane toPlane() {
        LightPlane plane = switch (level) {
            case LIGHT -> new LightPlane();
            case FIGHTER -> new FighterPlane();
            case CRUISER -> new CruiserPlane();
        };

        plane.setX(x);
        plane.setY(y);
        plane.setSpeed(spd);
        plane.setName(name);
        plane.setSystemStats(systemStats);
        plane.setActive(isActive);
        plane.setHP(hp);
        plane.setDamage(dmg);

        return plane;
    }
}

