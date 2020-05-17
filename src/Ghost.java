import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Ghost extends LivingEntity {

    double fearTimeLeft;
    double deadTimeLeft;

    int points = 200;
    int ghostNumber; //potrzebne bo każdy duch ma inny kolor

    final int size = 20;
    long timeDecideDirection;

    int pacmanX; //aktualna pozycja Pacmana, żeby można było go śledzić
    int pacmanY;

    Direction pacmanDirection;
    Direction pacmanDirectionFuture;

    private final TrackPacman trackPacman;

    public Ghost(int x, int y, int ghostNumber) {
        this.ghostNumber = ghostNumber;
        this.startX = toPixels(x);
        this.startY = toPixels(y);
        this.x = toPixels(x);
        this.y = toPixels(y);
        this.width = size;
        this.height = size;
        this.speed = 1;
        this.direction = Direction.RIGHT;
        this.directionFuture = Direction.UP;
        this.timeDecideDirection = System.nanoTime();
        this.alive = true;
        trackPacman = new TrackPacman(this);
    }

    public void pushPacmanX(int x) {
        pacmanX = x;
    }

    public void pushPacmanY(int y) {
        pacmanY = y;
    }

    public void pushPacmanDirection(Direction direction) {
        pacmanDirection = direction;
    }

    public void pushPacmanDirectionFuture(Direction direction) {
        pacmanDirectionFuture = direction;
    }

    public boolean isBase() {
        if (this.y <= 180 && this.y > 140) {
            if ((180 <= this.x) && (200 > this.x)) {
                direction = Direction.RIGHT;
                return true;
            } else if ((this.x <= 220) && (200 < this.x)) {
                direction = Direction.LEFT;
                return true;
            } else if (this.x == 200) {
                direction = Direction.UP;
                return true;
            }
        }
        return false;
    }


    public boolean isFrightened() {
        return this.fearTimeLeft > 0;
    }

    public void die() {
        this.alive = false;
        this.deadTimeLeft = 10;
    }

    public void setFearTimeLeft() {
        this.fearTimeLeft = 15; //jeżeli 15 sekund trwa power-up
    }

    //TODO - co z tym komentarzem poniżej?
    public void tick() {
        if (deadTimeLeft > 0)
            teleport(toPixels(8), toPixels(8));
        if (!isBase()) {
            trackPacman.trackPacman();// dopracowania
            if (!teleport()) {
               /* if (canMoveDirectionFutureAndDirection()) {
                    direction = Direction.DOWN;
                    directionFuture = Direction.RIGHT;
                    if (canMoveDirectionFutureAndDirection()) {
                        direction = Direction.UP;
                        directionFuture = Direction.LEFT;
                    }
                }
                */
            }
        }
        if (!canMoveThisDirection(direction)) { //TODO - to jest raczej do usunięcia
            System.out.println("blad ghost num " + this.ghostNumber + "direction " + direction);
            System.out.println("trackPacman.availableDirectionLeft " + trackPacman.availableDirectionLeft);
            System.out.println("trackPacman.availableDirectionRi " + trackPacman.availableDirectionRight);
            System.out.println("trackPacman.availableDirectionUp " + trackPacman.availableDirectionUp);
            System.out.println("trackPacman.availableDirectionDown " + trackPacman.availableDirectionDown);
            System.exit(1);
            this.direction = null;
        }

        setSpeed(direction);

        if (fearTimeLeft > 0) {
            fearTimeLeft -= (double) 1 / 60;
        }

        if (deadTimeLeft > 0) {
            deadTimeLeft -= (double) 1 / 60;
            if (deadTimeLeft <= 0)
                alive = true;
        }

    }

    public void render(Graphics g) {
        String imgPath;

        if (alive) {
            if (isFrightened()) {
                imgPath = "Images/ghost_frightened.png";
            } else {
                imgPath = "Images/ghost" + ghostNumber + "_right" + ".png";
                if (direction == Direction.RIGHT || directionFuture == Direction.RIGHT)
                    imgPath = "Images/ghost" + ghostNumber + "_right" + ".png";
                if (direction == Direction.LEFT || directionFuture == Direction.LEFT)
                    imgPath = "Images/ghost" + ghostNumber + "_left" + ".png";
            }
        } else {
            imgPath = "Images/ghost_dead_right.png";
            if (direction == Direction.RIGHT || directionFuture == Direction.RIGHT)
                imgPath = "Images/ghost_dead_right.png";
            if (direction == Direction.LEFT || directionFuture == Direction.LEFT)
                imgPath = "Images/ghost_dead_left.png";
        }

        try {
            g.drawImage(ImageIO.read(new File(imgPath)), (int) ((this.x + Maze.deltaX) * Maze.scale), (int) ((this.y + Maze.deltaY) * Maze.scale),
                    (int) (this.width * Maze.scale), (int) (this.height * Maze.scale), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}