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
        onCollision(collider, collidee, true, handler);
    }

    /**
     * Adds a collision interaction to this collection.
     *
     * @param <C1>
     *            The collider type.
     * @param <C2>
     *            The collidee (unit that was moved into) type.
     *
     * @param collider
     *            The collider type.
     * @param collidee
     *            The collidee type.
     * @param symetric
     *            <code>true</code> if this collision is used for both
     *            C1 against C2 and vice versa;
     *            <code>false</code> if only for C1 against C2.
     * @param handler
     *            The handler that handles the collision.
     */
    public <C1 extends Unit, C2 extends Unit> void onCollision(
        Class<C1> collider, Class<C2> collidee, boolean symetric,
        CollisionHandler<C1, C2> handler) {
        addHandler(collider, collidee, handler);
        if (symetric) {
            addHandler(collider, collidee, new InverseCollisionHandler<>(handler));
        }
    }

    /**
     * Adds the collision interaction..
     *
     * @param collider
     *            The collider type.
     * @param collidee
     *            The collidee type.
     * @param handler
     *            The handler that handles the collision.
     */
    private void addHandler(Class<? extends Unit> collider,
                            Class<? extends Unit> collidee, CollisionHandler<?, ?> handler) {
        if (!handlers.containsKey(collider)) {
            handlers.put(collider, new HashMap<>());

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
