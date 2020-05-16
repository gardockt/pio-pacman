import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class Ghost extends LivingEntity {

    double fearTimeLeft;
    double deadTimeLeft;

    int points = 200;     //tymczasowo
    int ghostNumber;//jeżeli każdy duch ma inny kolor

    final int size = 20;
    long timeDecideDirection;

    int pacmanX; //aktualna pozycja Pacmana, żeby można było go śledzić
    int pacmanY;

    Direction pacmanDirectory;
    Direction pacmanDirectoryFuture;

    private TrackPacman trackPacman;

    public Ghost(int x, int y, int ghostNumber) {
        this.ghostNumber = ghostNumber;
        this.startX = toPixelsX(x);
        this.startY = toPixelsY(y);
        this.x = toPixelsX(x);
        this.y = toPixelsY(y);
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
        pacmanDirectory = direction;
    }

    public void pushPacmanDirectorFuture(Direction direction) {
        pacmanDirectoryFuture = direction;
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


    public void tick() {
        if (fearTimeLeft > 0) {
            trackPacman.escapeFromPacman();
            fearTimeLeft -= (double) 1 / 60;
        }else if (deadTimeLeft > 0) {
            trackPacman.escapeFromPacman();
            deadTimeLeft -= (double) 1 / 60;
            if (deadTimeLeft <= 0)
                alive = true;
        }else {
            if (!isBase()) {
                if (!teleportGhost()) {
                    trackPacman.trackPacman();
                }
            }

        }
        if (!canMoveThisDirection(direction)) { // do testow
            System.out.println("blad ghost num " + this.ghostNumber + "director " + direction);
            System.out.println("trackPacman.availableDirectoryLeft " + trackPacman.availableDirectoryLeft);
            System.out.println("trackPacman.availableDirectoryRi " + trackPacman.availableDirectoryRight);
            System.out.println("trackPacman.availableDirectoryUp " + trackPacman.availableDirectoryUp);
            System.out.println("trackPacman.availableDirectoryDown " + trackPacman.availableDirectoryDown);
            System.exit(1);
            this.direction = null;
        }
        super.setSpeed(direction);

    }
    private boolean teleportGhost(){
        if (this.y == 180) {
            if (this.x < 80) {
                if(direction != Direction.RIGHT) {
                    direction = Direction.LEFT;
                    if (this.x == 20) {
                        teleport(toPixelsX(18), toPixelsY(8));
                    }
                    return true;
                }
            } else if (toCellsX(this.x) > 14) {
                if(direction != Direction.LEFT) {
                    direction = Direction.RIGHT;
                    if (toCellsX((this.x)) == 18) {
                        teleport(toPixelsX(0), toPixelsY(8));
                    }
                }
                return true;
            }
        }
        return false;
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
            g.drawImage(ImageIO.read(new File(imgPath)), this.x + Maze.deltaX, this.y + Maze.deltaY, this.width, this.height, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}