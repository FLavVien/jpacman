package nl.tudelft.jpacman.board;

import nl.tudelft.jpacman.sprite.Sprite;

/**
 * A unit that can be placed on a {@link Square}.
 */
public abstract class Unit {

    private Square square;
    private Direction direction;

    protected Unit() {
        this.direction = Direction.EAST;
    }

    public void setDirection(Direction newDirection) {
        if (newDirection == null) {
            throw new IllegalArgumentException("Direction cannot be null");
        }
        this.direction = newDirection;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public Square getSquare() {
        if (square == null) {
            throw new IllegalStateException("Unit is not placed on any square");
        }
        return square;
    }

    public boolean hasSquare() {
        return square != null;
    }

    public void occupy(Square target) {
        if (target == null) {
            throw new IllegalArgumentException("Target square cannot be null");
        }
        leaveSquare();
        square = target;
        target.put(this);
    }

    public void leaveSquare() {
        if (square != null) {
            square.remove(this);
            square = null;
        }
    }

    protected boolean invariant() {
        return square == null || square.getOccupants().contains(this);
    }

    public abstract Sprite getSprite();

    public Square squaresAheadOf(int amountToLookAhead) {
        if (amountToLookAhead < 0) {
            throw new IllegalArgumentException("Look-ahead amount cannot be negative");
        }
        Square destination = this.getSquare();
        for (int i = 0; i < amountToLookAhead; i++) {
            destination = destination.getSquareAt(this.getDirection());
        }
        return destination;
    }
}
