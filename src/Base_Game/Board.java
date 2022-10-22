package Base_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Board {
    // Represents the overall game state

    private final int[][] grid;
    private final Boolean[][] hasCombined;
    private final GUI gameGUI;

    public Board() {

        grid = new int[4][4];
        hasCombined = new Boolean[4][4];

//        grid[0][0] = 1;
//        grid[0][1] = 2;
//        grid[0][2] = 3;
//        grid[0][3] = 4;
//        grid[1][0] = 5;
//        grid[1][1] = 6;
//        grid[1][2] = 7;
//        grid[1][3] = 8;
//        grid[2][0] = 9;
//        grid[2][1] = 10;
//        grid[2][2] = 11;
//        grid[2][3] = 12;
//        grid[3][0] = 2;
//        grid[3][1] = 2;
//        grid[3][2] = 15;
//        grid[3][3] = 16;

        gameGUI = new GUI(this);

        spawnTile();


    }

    void spawnTile() {
        // Spawns a new tile, either a 2 or (at a 10% chance) a 4, at a random unoccupied location

        if(!gridHasEmptyTiles()) return;
        // If there are no empty tiles, don't infinitely try to create a new tile

        Random r = new Random();
        boolean newTileIsFour = (r.nextInt(10) == 0);
        boolean tileHasSpawned = false;
        int x, y;

        while(!tileHasSpawned) {

            x = r.nextInt(4);
            y = r.nextInt(4);

            if(grid[x][y] == 0) {

                if(newTileIsFour) grid[x][y] = 4;
                else grid[x][y] = 2;
                tileHasSpawned = true;

            }

        }

    }

    boolean tileShift(boolean horizontal, int shift, int c1, int c2, Boolean[][] hasCombined) {
        // Shifts the current tile based on the values passed in
        // This function is called by all the move functions to avoid duplicate code

        int destinationX, destinationY, sourceX, sourceY;

        if(horizontal) {

            destinationX = shift;
            destinationY = c2;
            sourceX = c1;
            sourceY = c2;

        } else {

            destinationX = c2;
            destinationY = shift;
            sourceX = c2;
            sourceY = c1;

        }

        if(grid[destinationX][destinationY] == 0) grid[destinationX][destinationY] = grid[sourceX][sourceY];
        else if(!hasCombined[destinationX][destinationY]) {

            grid[destinationX][destinationY] *= 2;
            hasCombined[destinationX][destinationY] = true;

        }

        grid[sourceX][sourceY] = 0;

        return true;

    }

    void checkForGameOver() {

        if(gridHasEmptyTiles()) return;

        for(int x = 1; x < 3; x++) {

            for(int y = 1; y < 3; y++) if(checkTileEdgesForMoves(x, y)) return;

        }

        if(checkTileEdgesForMoves(0, 0) || checkTileEdgesForMoves(0, 3)
        || checkTileEdgesForMoves(3, 0) || checkTileEdgesForMoves(3, 3)) return;

        gameGUI.showGameOver();

    }

    private boolean gridHasEmptyTiles() {

        for(int x = 0; x < 4; x++) {

            for(int y = 0; y < 4; y++) if(getTile(x, y) == 0) return true;

        }

        return false;

    }

    boolean checkTileEdgesForMoves(int x, int y) {

        boolean lbCorner = (x == 0 && y == 0);
        boolean ltCorner = (x == 0 && y == 3);
        boolean rbCorner = (x == 3 && y == 0);
        boolean rtCorner = (x == 3 && y == 3);

        boolean checkUp = true;
        boolean checkDown = true;
        boolean checkRight = true;
        boolean checkLeft = true;

        if(lbCorner) {

            checkLeft = false;
            checkDown = false;

        } else if(ltCorner) {

            checkLeft = false;
            checkUp = false;

        } else if(rbCorner) {

            checkRight = false;
            checkDown = false;

        } else if(rtCorner) {

            checkRight = false;
            checkUp = false;

        }

        if(checkRight && getTile(x, y) == getTile(x + 1, y)) return true;
        else if(checkLeft && getTile(x, y) == getTile(x - 1, y)) return true;
        else if(checkUp && getTile(x, y) == getTile(x, y + 1)) return true;
        else if(checkDown && getTile(x, y) == getTile(x, y - 1)) return true;
        else return false;

    }

    void moveUp() {
        // Shifts all tiles up if applicable

        boolean hasMoved = false;

        for(int i = 0; i < 4; i++) {

            for(int j = 0; j < 4; j++)  hasCombined[i][j] = false;

        }
        // Clear previous move's merge flags

        for(int y = 2; y >= 0; y--) {
            // For every Y-coord, starting 1 from the top and going to the bottom

            for(int x = 0; x < 4; x++) {
                // For every X-coord, starting from the left and going to the right

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty (prevents unnecessary work)

                    int shift = y;
                    // Y-coord of new tile location
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

//                    int tileValue = grid[x][y];

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(shift == 3 || (grid[x][shift + 1] != 0 && grid[x][shift + 1] != grid[x][y])) doShift = true;
                        // If the shift has either reached the grid boundaries, or the next tile is not a 0 and not the same number, shift
                        else if(grid[x][shift + 1] == grid[x][y] && hasCombined[x][shift + 1]) doShift = true;
                        // If the next tile is the same number, but it has already been merged, shift using the current value
                        else shift++;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != y) hasMoved = tileShift(false, shift, y, x, hasCombined);
                        // If the shift is to be performed, update hasMoved to reflect as much
                        // This is written in an odd way to avoid a multi-line statement

                    }

//                    System.out.println("UP: For tile " + tileValue + " at (" + x + ", " + y + "): " + Arrays.toString(hasCombined));

                }

            }

        }

        if(hasMoved) {

            spawnTile();
            checkForGameOver();

        }

    }

    void moveDown() {
        // Shifts all tiles down if applicable

        boolean hasMoved = false;

        for(int i = 0; i < 4; i++) {

            for(int j = 0; j < 4; j++)  hasCombined[i][j] = false;

        }
        // Clear previous move's merge flags

        for(int y = 1; y < 4; y++) {
            // For every Y-coord, starting 1 from the bottom and going to the top

            for(int x = 0; x < 4; x++) {
                // For every X-coord, starting from the left and going to the right

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = y;
                    // Y-coord of new tile location
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

//                    int tileValue = grid[x][y];

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(shift == 0 || (grid[x][shift - 1] != 0 && grid[x][shift - 1] != grid[x][y])) doShift = true;
                        // If the shift has either reached the grid boundaries, or the next tile is not a 0 and not the same number, shift
                        else if(grid[x][shift - 1] == grid[x][y] && hasCombined[x][shift - 1]) doShift = true;
                        // If the next tile is the same number, but it has already been merged, shift using the current value
                        else shift--;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != y) hasMoved = tileShift(false, shift, y, x, hasCombined);
                        // If the shift is to be performed, update hasMoved to reflect as much
                        // This is written in an odd way to avoid a multi-line statement

                    }

//                    System.out.println("DOWN: For tile " + tileValue + " at (" + x + ", " + y + "): " + Arrays.toString(hasCombined));

                }

            }

        }

        if(hasMoved) {

            spawnTile();
            checkForGameOver();

        }

    }

    void moveRight() {
        // Shifts all tiles right if applicable

        boolean hasMoved = false;

        for(int i = 0; i < 4; i++) {

            for(int j = 0; j < 4; j++)  hasCombined[i][j] = false;

        }
        // Clear previous move's merge flags

        for(int x = 2; x >= 0; x--) {
            // For every X-coord, starting 1 from the right and going to the left

            for(int y = 0; y < 4; y++) {
                // For every Y-coord, starting from the bottom and going to the top

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = x;
                    // X-coord of new tile location
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

//                    int tileValue = grid[x][y];

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(shift == 3 || (grid[shift + 1][y] != 0 && grid[shift + 1][y] != grid[x][y])) doShift = true;
                        // If the shift has either reached the grid boundaries, or the next tile is not a 0 and not the same number, shift
                        else if(grid[shift + 1][y] == grid[x][y] && hasCombined[shift + 1][y]) doShift = true;
                        // If the next tile is the same number, but it has already been merged, shift using the current value
                        else shift++;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != x) hasMoved = tileShift(true, shift, x, y, hasCombined);
                        // If the shift is to be performed, update hasMoved to reflect as much
                        // This is written in an odd way to avoid a multi-line statement

                    }

//                    System.out.println("RIGHT: For tile " + tileValue + " at (" + x + ", " + y + "): " + Arrays.toString(hasCombined));

                }

            }

        }

        if(hasMoved) {

            spawnTile();
            checkForGameOver();

        }

    }

    void moveLeft() {
        // Shifts all tiles left if applicable

        boolean hasMoved = false;

        for(int i = 0; i < 4; i++) {

            for(int j = 0; j < 4; j++)  hasCombined[i][j] = false;

        }
        // Clear previous move's merge flags

        for(int x = 1; x < 4; x++) {
            // For every X-coord, starting 1 from the left and going to the right

            for(int y = 0; y < 4; y++) {
                // For every Y-coord, starting from the bottom and going to the top

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = x;
                    // X-coord of new tile location
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

//                    int tileValue = grid[x][y];

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(shift == 0 || (grid[shift - 1][y] != 0 && grid[shift - 1][y] != grid[x][y])) doShift = true;
                        // If the shift has either reached the grid boundaries, or the next tile is not a 0 and not the same number, shift
                        else if(grid[shift - 1][y] == grid[x][y] && hasCombined[shift - 1][y]) doShift = true;
                        // If the next tile is the same number, but it has already been merged, shift using the current value
                        else shift--;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != x) hasMoved = tileShift(true, shift, x, y, hasCombined);
                        // If the shift is to be performed, update hasMoved to reflect as much
                        // This is written in an odd way to avoid a multi-line statement

                    }

//                    System.out.println("LEFT: For tile " + tileValue + " at (" + x + ", " + y + "): " + Arrays.toString(hasCombined));

                }

            }

        }

        if(hasMoved) {

            spawnTile();
            checkForGameOver();

        }

    }

    int[][] getGrid() {

        return grid;

    }

    int getTile(int x, int y) {
        // Returns the tile at a coordinate

        return grid[x][y];

    }

}

class GUI extends JFrame {
    // The basic grid GUI for the game

    Board boardData;
    ArrayList<JLabel> labels;

    public GUI(Board boardData) {
        // Creates a new GUI

        this.boardData = boardData;
        labels = new ArrayList<>();

        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 4));

        addKeyListener(new KeyboardInput(this));

        for (int i = 0; i < 16; i++) {

            labels.add(new JLabel());
            JLabel l = labels.get(i);
            l.setHorizontalAlignment(JLabel.CENTER);
            l.setFont(new Font(l.getFont().getName(), Font.BOLD, 100));
            l.setOpaque(true);
            add(l);

        }

        update();
        setVisible(true);

    }

    void update() {
        // Updates the GUI to reflect the game state

        int tileValue;
        Color tileColor;

        for(int y = 3; y >= 0; y--) {

            for(int x = 0; x < 4; x++) {

                tileValue = boardData.getTile(x, y);

                String tileNumber = String.valueOf(tileValue);
                if(Integer.parseInt(tileNumber) == 0) tileNumber = "";

                JLabel l = labels.get(translateCoordToLabel(x, y));
                l.setText(tileNumber);

                tileColor = switch (tileValue) {

                        case 0 -> new Color(204, 193, 180);
                        case 2 -> new Color(238, 228, 218);
                        case 4 -> new Color(238, 225, 201);
                        case 8 -> new Color(243, 178, 122);
                        case 16 -> new Color(246, 150, 100);
                        case 32 -> new Color(247, 124, 95);
                        case 64 -> new Color(247, 95, 59);
                        case 128 -> new Color(237, 208, 115);
                        case 256 -> new Color(237, 204, 98);
                        case 512 -> new Color(237, 201, 80);
                        case 1024 -> new Color(237, 197, 63);
                        case 2048 -> new Color(237, 194, 46);
                        default -> Color.BLACK;

                };

                l.setFont(new Font(l.getFont().getName(), Font.BOLD, textSizeFromDigits(amountOfDigits(tileValue))));

                if(tileValue >= 8) l.setForeground(new Color(249, 246, 242));
                else l.setForeground(new Color(119, 110, 101));
                l.setBackground(tileColor);

            }

        }

    }

    void showGameOver() {

        update();
        // TODO: Fix the update bug where GUI does not update before game over

        getContentPane().removeAll();
        JLabel l = new JLabel("GAME OVER!");
        l.setFont(new Font(l.getFont().getName(), Font.BOLD, 100));
        l.setHorizontalAlignment(JLabel.CENTER);
        l.setForeground(Color.RED);
        add(l);
        getContentPane().validate();

    }

    int textSizeFromDigits(int digits) {
        // Finds an appropriate font size based on the amount of digits in a number

        return 100 - (10 * (digits - 1));

    }

    int translateCoordToLabel(int x, int y) {
        // Translates an (x, y) coordinate into a "labels" index value

        return 4 * (3 - y) + x;

    }

    int amountOfDigits(int n) {
        // Finds the amount of decimal digits in a number

        int digits = 0;

        while(n != 0) {

            n /= 10;
            digits++;

        }

        return digits;

    }

}

class KeyboardInput extends KeyAdapter {
    // Keyboard listener for arrow buttons

    GUI gameGUI;

    public KeyboardInput(GUI gameGUI) {

        this.gameGUI = gameGUI;

    }

    @Override
    public void keyPressed(KeyEvent event) {

        if(event.getKeyCode() == VK_UP) {

            gameGUI.boardData.moveUp();
            gameGUI.update();

        }

        if(event.getKeyCode() == VK_DOWN) {

            gameGUI.boardData.moveDown();
            gameGUI.update();

        }

        if(event.getKeyCode() == VK_RIGHT) {

            gameGUI.boardData.moveRight();
            gameGUI.update();

        }

        if(event.getKeyCode() == VK_LEFT) {

            gameGUI.boardData.moveLeft();
            gameGUI.update();

        }

    }

}
