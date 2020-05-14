import java.util.Random;

public class TrackPacman {
    private Ghost ghost;

    boolean availableDirectoryUp;
    boolean availableDirectoryDown;
    boolean availableDirectoryRight;
    boolean availableDirectoryLeft;


    public TrackPacman(Ghost ghost){
        this.ghost = ghost;
    }
    public void trackPacman() {
        int ghostNumber = ghost.ghostNumber;

        if (ghostNumber == 1) {
            decideDirection1();
        } else if (ghostNumber == 2) {
            decideDirection2();
        } else if (ghostNumber == 3) {
            decideDirection3();
        } else {
            decideDirection4();
        }

    }

    // red ghost
    public void decideDirection1() {
        if (System.nanoTime() - ghost.timeDecideDirection >= 0.75e9) {
            ghost.direction = ghost.directionFuture;
            Random rand = new Random();

            int tempDirection = rand.nextInt(4);
            if (tempDirection == 0) {
                ghost.directionFuture = LivingEntity.Direction.RIGHT;
                if (tempDirection == 1)
                    ghost.directionFuture = LivingEntity.Direction.UP;
                if (tempDirection == 2)
                    ghost.directionFuture = LivingEntity.Direction.DOWN;
                if (tempDirection == 3)
                    ghost.directionFuture = LivingEntity.Direction.LEFT;
            }
            ghost.timeDecideDirection = System.nanoTime();
        }
    }

    // pink ghost
    public void decideDirection2() {
        if (System.nanoTime() - ghost.timeDecideDirection >= 0.75e9) {
            ghost.directionFuture = ghost.pacmanDirectory;
            ghost.timeDecideDirection = System.nanoTime();
        }
    }

    // orange ghost
    public void decideDirection3() {
        if (System.nanoTime() - ghost.timeDecideDirection >= 0.75e9) {
            findAvailableDirectory();
            boolean isLeft;
            boolean isUp;
            int xDistanceFromPacman = ghost.x - ghost.pacmanX;
            int yDistanceFromPacman = ghost.y - ghost.pacmanY;
            if (xDistanceFromPacman > 0) {
                isLeft = true;
            } else {
                isLeft = false;
            }
            if (yDistanceFromPacman > 0) {
                isUp = true;
            } else {
                isUp = false;
            }
            if (donMoveToBase(xDistanceFromPacman)) {

            } else if ((xDistanceFromPacman * xDistanceFromPacman) > (yDistanceFromPacman * yDistanceFromPacman)) {
                if (isLeft) {
                    if (availableDirectoryLeft) {
                        ghost.direction = LivingEntity.Direction.LEFT;
                    } else {
                        moveDownorUp(isUp);
                    }
                } else {
                    if (availableDirectoryRight) {
                        ghost.direction = LivingEntity.Direction.RIGHT;
                    } else {
                        moveDownorUp(isUp);
                    }
                }
            } else {
                if (isUp) {
                    if (availableDirectoryUp) {
                        ghost.direction = LivingEntity.Direction.UP;
                    } else {
                        moveLeftorRight(isLeft);
                    }
                } else {
                    if (availableDirectoryDown) {
                        ghost.direction = LivingEntity.Direction.DOWN;
                    } else {
                        moveLeftorRight(isLeft);
                    }
                }

            }
            ghost.directionFuture = ghost.direction;
        }
    }

    private void moveLeftorRight(boolean isLeft){
        if (isLeft && availableDirectoryLeft) {
            ghost.direction = LivingEntity.Direction.LEFT;
        } else if (availableDirectoryRight) {
            ghost.direction = LivingEntity.Direction.RIGHT;
        }
    }
    private void moveDownorUp(boolean isUp){
        if (isUp && availableDirectoryUp) {
            ghost.direction = LivingEntity.Direction.UP;
        } else if (availableDirectoryDown) {
            ghost.direction = LivingEntity.Direction.DOWN;
        }
    }

    // yellow ghost
    public void decideDirection4() {
        if (System.nanoTime() - ghost.timeDecideDirection >= 0.75e9) {
            findAvailableDirectory();
            decideDirection3();
            int xDistanceFromPacman = ghost.x - ghost.pacmanX;
            int yDistanceFromPacman = ghost.y - ghost.pacmanY;

            if (donMoveToBase(xDistanceFromPacman)) {

            } else if ((xDistanceFromPacman * xDistanceFromPacman) > (yDistanceFromPacman * yDistanceFromPacman)) {
                if (xDistanceFromPacman > 0 && availableDirectoryLeft) {
                    ghost.direction = LivingEntity.Direction.LEFT;
                } else if (availableDirectoryRight) {
                    ghost.direction = LivingEntity.Direction.RIGHT;
                } else if (yDistanceFromPacman > 0 && availableDirectoryUp) {
                    ghost.direction = LivingEntity.Direction.UP;
                } else if (availableDirectoryDown) {
                    ghost.direction = LivingEntity.Direction.DOWN;
                }
            } else {
                if (yDistanceFromPacman > 0 && availableDirectoryUp) {
                    ghost.direction = LivingEntity.Direction.UP;
                } else if (availableDirectoryDown) {
                    ghost.direction = LivingEntity.Direction.DOWN;
                } else if (xDistanceFromPacman > 0 && availableDirectoryLeft) {
                    ghost.direction = LivingEntity.Direction.LEFT;
                } else if (availableDirectoryRight) {
                    ghost.direction = LivingEntity.Direction.RIGHT;
                }
            }
            ghost.directionFuture = ghost.direction;
        }


    }

    private boolean donMoveToBase(int xDistanceFromPacman) {
        if (ghost.x == 200 && ghost.y == 140) {
            if (xDistanceFromPacman > 0 && availableDirectoryLeft) {
                ghost.direction = LivingEntity.Direction.LEFT;
                return true;
            } else if (availableDirectoryRight) {
                ghost.direction = LivingEntity.Direction.RIGHT;
                return true;
            }
        }
        return false;
    }
    private void findAvailableDirectory() {
        availableDirectoryDown = availableThisDirectory(LivingEntity.Direction.DOWN);
        availableDirectoryLeft = availableThisDirectory(LivingEntity.Direction.LEFT);
        availableDirectoryRight = availableThisDirectory(LivingEntity.Direction.RIGHT);
        availableDirectoryUp = availableThisDirectory(LivingEntity.Direction.UP);

    }

    private boolean availableThisDirectory(LivingEntity.Direction direction) {
        if (ghost.canMoveThisDirection(direction)) {
            System.out.println("to directory jest avaible " + direction);
            return true;
        } else {
            return false;
        }
    }

}
