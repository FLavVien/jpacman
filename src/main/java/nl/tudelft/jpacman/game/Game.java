package nl.tudelft.jpacman.game;

import java.util.List;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.level.Level;
import nl.tudelft.jpacman.level.Level.LevelObserver;
import nl.tudelft.jpacman.level.Player;
import nl.tudelft.jpacman.points.PointCalculator;

/**
 * A basic implementation of a Pac-Man game.
 */
public abstract class Game implements LevelObserver {

    private boolean inProgress;
    private final Object progressLock = new Object();
    private final PointCalculator pointCalculator;

    protected Game(PointCalculator pointCalculator) {
        if (pointCalculator == null) {
            throw new IllegalArgumentException("PointCalculator cannot be null");
        }
        this.pointCalculator = pointCalculator;
        inProgress = false;
    }

    public void start() {
        synchronized (progressLock) {
            if (isInProgress() || !canStartGame()) {
                return;
            }
            inProgress = true;
            getLevel().addObserver(this);
            getLevel().start();
        }
    }

    public void stop() {
        synchronized (progressLock) {
            if (!isInProgress()) {
                return;
            }
            inProgress = false;
            getLevel().stop();
        }
    }

    public boolean isInProgress() {
        return inProgress;
    }

    private boolean canStartGame() {
        return getLevel().isAnyPlayerAlive() && getLevel().remainingPellets() > 0;
    }

    public abstract List<Player> getPlayers();
    public abstract Level getLevel();

    public void move(Player player, Direction direction) {
        if (player == null || direction == null) {
            throw new IllegalArgumentException("Player and direction cannot be null");
        }
        if (isInProgress()) {
            getLevel().move(player, direction);
            pointCalculator.pacmanMoved(player, direction);
        }
    }

    @Override
    public void levelWon() {
        stop();
    }

    @Override
    public void levelLost() {
        stop();
    }
}
