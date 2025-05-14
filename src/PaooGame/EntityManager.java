package PaooGame;

import PaooGame.Entity;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EntityManager {
    private List<Entity> entities = new ArrayList<>();

    public void addEntity(Entity e) {
        entities.add(e);
    }

    public void updateAll(GameMap gameMap) {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.update(gameMap);
            }
        }
    }

    public void renderAll(Graphics g, Camera camera) {
        for (Entity e : entities) {
            if (e.isActive()) {
                e.render(g, camera);
            }
        }
    }
}