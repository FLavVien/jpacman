package nl.tudelft.jpacman.level;

import java.util.HashMap;
import java.util.Map;
import nl.tudelft.jpacman.board.Unit;

/**
 * A map of possible collisions and their handlers.
 */
public class CollisionInteractionMap implements CollisionMap {

    private final Map<Class<? extends Unit>, Map<Class<? extends Unit>, CollisionHandler<?, ?>>> handlers = new HashMap<>();

    public <C1 extends Unit, C2 extends Unit> void onCollision(
        Class<C1> collider, Class<C2> collidee, CollisionHandler<C1, C2> handler) {
        if (collider == null || collidee == null || handler == null) {
            throw new IllegalArgumentException("Collision parameters cannot be null");
        }
        handlers.computeIfAbsent(collider, k -> new HashMap<>()).put(collidee, handler);
        handlers.computeIfAbsent(collidee, k -> new HashMap<>())
            .put(collider, (c, co) -> handler.handleCollision((C1) co, (C2) c));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <C1 extends Unit, C2 extends Unit> void collide(C1 collider, C2 collidee) {
        if (collider == null || collidee == null) {
            throw new IllegalArgumentException("Colliding units cannot be null");
        }
        CollisionHandler<C1, C2> collisionHandler = (CollisionHandler<C1, C2>)
            handlers.getOrDefault(collider.getClass(), new HashMap<>())
                .getOrDefault(collidee.getClass(), null);

        if (collisionHandler != null) {
            collisionHandler.handleCollision((C1) collider, (C2) collidee);
        }
    }

    /**
     * Handles collision interactions.
     */
    public interface CollisionHandler<C1 extends Unit, C2 extends Unit> {
        void handleCollision(C1 collider, C2 collidee);
    }
}
