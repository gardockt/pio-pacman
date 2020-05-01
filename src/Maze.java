import javax.swing.*;
import java.awt.*;

public class Maze extends JPanel {
    static int cellSize = 20; //rozmiar pojedynczej komórki na planszy
    boolean[][] grid = new boolean[19][19];
    boolean[][] dots = new boolean[19][19]; // to na pewno jest potrzebne?
    Ghost[] ghosts;
    Pacman pacman;
    private final CollectableEntity[][] yellowDots;

    private boolean time = true;

    public Maze() {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                grid[i][j] = true; //przed ustawieniem ścian można się poruszać po wszystkich komórkach
                dots[i][j] = true;
            }
        }
        deleteDotsFromCage();
        updateEntireMap();

        // nie chcemy kulek w klatce duchów
        dots[8][8] = false;
        dots[8][9] = false;
        dots[8][10] = false;

        ghosts = new Ghost[4];
        ghosts[0] = new Ghost(8, 8, 1); //8,8
        ghosts[1] = new Ghost(8, 8, 2); //8,8
        ghosts[2] = new Ghost(8, 8, 3); //8,8
        ghosts[3] = new Ghost(8, 8, 4); //8,8

        pacman = new Pacman(9, 10, 20, 1);

        yellowDots = new CollectableEntity[19][19];
        for (int i = 0; i < dots.length; i++) {
            for (int j = 0; j < dots[i].length; j++) {
                if (activeDots(i, j))
                    yellowDots[i][j] = new Dot(j, i);
            }
        }

        ghosts[0].grid = grid;
        ghosts[1].grid = grid;
        ghosts[2].grid = grid;
        ghosts[3].grid = grid;
        pacman.grid = grid;
        pacman.pushGhosts(ghosts);
        pacman.pushDots(yellowDots);
    }

    public boolean activeDots(int i, int j) {
        return dots[i][j];
    }

    public void update() {
        for(Ghost ghost : ghosts) {
            ghost.pushPacmanX(pacman.x);
            ghost.pushPacmanY(pacman.y);
            ghost.tick();
        }
        pacman.tick();
    }

    //Rysuje jedną całą klatkę gry
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        pacman.render(g);
        drawDots(g);
        for (Ghost ghost : ghosts) {
            ghost.render(g);
        }
    }

    public void deleteDotsFromCage() {
        dots[7][9] = false;
        dots[8][8] = false;
        dots[8][9] = false;
        dots[8][10] = false;
    }

    /*Aktualizuje fragment mapy - dla prostokąta przekazanego w argumencie (x, y - współrzędne
    górnego lewego wierzchołka, width, height - wymiary na prawo i do dołu) ustawia wartość
    elementu na false (ściana) - czyli po danej komórce nie można się poruszać ani ustawić w niej kropki*/
    public void updateMap(int x, int y, int width, int height) {
        for (int i = x / cellSize; i < x / cellSize + width / cellSize; i++) {
            for (int j = y / cellSize; j < y / cellSize + height / cellSize; j++) {
                grid[j - 1][i - 1] = false;
                dots[j - 1][i - 1] = false;
            }
        }
    }

    //Aktualizuje całą mapę ("generuje ściany")
    public void updateEntireMap() {
        updateMap(40, 40, 60, 20);
        updateMap(120, 40, 60, 20);
        updateMap(200, 20, 20, 40);
        updateMap(240, 40, 60, 20);
        updateMap(320, 40, 60, 20);
        updateMap(40, 80, 60, 20);
        updateMap(160, 80, 100, 20);
        updateMap(200, 80, 20, 60);
        updateMap(320, 80, 60, 20);
        updateMap(20, 120, 80, 60);
        updateMap(320, 120, 80, 60);
        updateMap(20, 200, 80, 60);
        updateMap(320, 200, 80, 60);
        updateMap(160, 160, 40, 20);
        updateMap(220, 160, 40, 20);
        updateMap(160, 180, 20, 20);
        updateMap(160, 200, 100, 20);
        updateMap(240, 180, 20, 20);
        updateMap(120, 120, 60, 20);
        updateMap(120, 80, 20, 100);
        updateMap(280, 80, 20, 100);
        updateMap(240, 120, 60, 20);
        updateMap(280, 200, 20, 60);
        updateMap(120, 200, 20, 60);
        updateMap(160, 240, 100, 20);
        updateMap(200, 260, 20, 40);
        updateMap(120, 280, 60, 20);
        updateMap(240, 280, 60, 20);
        updateMap(40, 280, 60, 20);
        updateMap(80, 280, 20, 60);
        updateMap(320, 280, 60, 20);
        updateMap(320, 280, 20, 60);
        updateMap(20, 320, 40, 20);
        updateMap(360, 320, 40, 20);
        updateMap(160, 320, 100, 20);
        updateMap(200, 320, 20, 60);
        updateMap(40, 360, 140, 20);
        updateMap(240, 360, 140, 20);
        updateMap(280, 320, 20, 60);
        updateMap(120, 320, 20, 60);
    }

    //Rysuje planszę na panelu
    public void drawBoard(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 440, 460);

        g.setColor(Color.WHITE);
        g.drawRect(19, 19, 382, 382);

        g.setColor(Color.BLUE);
        g.fillRect(40, 40, 60, 20);
        g.fillRect(120, 40, 60, 20);
        g.fillRect(200, 20, 20, 40);
        g.fillRect(240, 40, 60, 20);
        g.fillRect(320, 40, 60, 20);
        g.fillRect(40, 80, 60, 20);
        g.fillRect(160, 80, 100, 20);
        g.fillRect(200, 80, 20, 60);
        g.fillRect(320, 80, 60, 20);

        g.fillRect(20, 120, 80, 60);
        g.fillRect(320, 120, 80, 60);
        g.fillRect(20, 200, 80, 60);
        g.fillRect(320, 200, 80, 60);

        g.fillRect(160, 160, 40, 20);
        g.fillRect(220, 160, 40, 20);
        g.fillRect(160, 180, 20, 20);
        g.fillRect(160, 200, 100, 20);
        g.fillRect(240, 180, 20, 20);

        g.setColor(Color.BLUE);
        g.fillRect(120, 120, 60, 20);
        g.fillRect(120, 80, 20, 100);
        g.fillRect(280, 80, 20, 100);
        g.fillRect(240, 120, 60, 20);

        g.fillRect(280, 200, 20, 60);
        g.fillRect(120, 200, 20, 60);
        g.fillRect(160, 240, 100, 20);
        g.fillRect(200, 260, 20, 40);

        g.fillRect(120, 280, 60, 20);
        g.fillRect(240, 280, 60, 20);

        g.fillRect(40, 280, 60, 20);
        g.fillRect(80, 280, 20, 60);
        g.fillRect(320, 280, 60, 20);
        g.fillRect(320, 280, 20, 60);

        g.fillRect(20, 320, 40, 20);
        g.fillRect(360, 320, 40, 20);
        g.fillRect(160, 320, 100, 20);
        g.fillRect(200, 320, 20, 60);

        g.fillRect(40, 360, 140, 20);
        g.fillRect(240, 360, 140, 20);
        g.fillRect(280, 320, 20, 40);
        g.fillRect(120, 320, 20, 60);
    }

    public void drawDots(Graphics g) {
        for (int i = 0; i < dots.length; i++) {
            for (int j = 0; j < dots[i].length; j++) {
                if (yellowDots[i][j] != null && activeDots(i, j))
                    yellowDots[i][j].render(g);
            }
        }
    }
}
