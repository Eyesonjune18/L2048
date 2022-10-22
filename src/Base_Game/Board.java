package Base_Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

import static java.awt.event.KeyEvent.*;

public class Board {

    int[][] grid;
    GUI gameGUI;

    public Board() {

        grid = new int[4][4];

        spawnTile();

        gameGUI = new GUI(this);

    }

    void spawnTile() {

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

//    void print() {
//
//        for(int y = 0; y < 4; y++) {
//
//            for(int x = 0; x < 4; x++) {
//
//                System.out.print(grid[x][3 - y] + "  ");
//
//            }
//
//            System.out.println();
//
//        }
//
//    }

    void moveUp() {

        boolean hasMoved = false;

        for(int y = 2; y >= 0; y--) {
            // For every Y-coord, starting 1 from the top and going to the bottom

            for(int x = 0; x < 4; x++) {
                // For every X-coord, starting from the left and going to the right

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty (prevents unnecessary work)

                    int shift = 0;
                    // Y-offset from the current coord
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(y + shift == 3 || (grid[x][y + shift + 1] != 0 && grid[x][y + shift + 1] != grid[x][y])) doShift = true;
                            // If the shift has either reached the grid boundaries, or the next number is not a 0 and not the same number, shift
                        else shift++;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != 0) {

                            hasMoved = true;

                            if(grid[x][y + shift] == 0) grid[x][y + shift] = grid[x][y];
                            else grid[x][y + shift] *= 2;

                            grid[x][y] = 0;

                        }

                    }

                }

            }

        }

        if(hasMoved) spawnTile();

    }

    void moveDown() {

        boolean hasMoved = false;

        for(int y = 1; y < 4; y++) {
            // For every Y-coord, starting 1 from the bottom and going to the top

            for(int x = 0; x < 4; x++) {
                // For every X-coord, starting from the left and going to the right

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = 0;
                    // Y-offset from the current coord
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(y - shift == 0 || (grid[x][y - shift - 1] != 0 && grid[x][y - shift - 1] != grid[x][y])) doShift = true;
                            // If the shift has either reached the grid boundaries, or the next number is not a 0, shift
                        else shift++;
                        // Otherwise, if the next number is a 0, keep incrementing shift

                        if(doShift && shift != 0) {

                            hasMoved = true;

                            if(grid[x][y - shift] == 0) grid[x][y - shift] = grid[x][y];
                            else grid[x][y - shift] *= 2;

                            grid[x][y] = 0;

                        }

                    }

                }

            }

        }

        if(hasMoved) spawnTile();

    }

    void moveRight() {

        boolean hasMoved = false;

        for(int x = 2; x >= 0; x--) {
            // For every X-coord, starting 1 from the right and going to the left

            for(int y = 0; y < 4; y++) {
                // For every Y-coord, starting from the bottom and going to the top

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = 0;
                    // Y-offset from the current coord
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(x + shift == 3 || (grid[x + shift + 1][y] != 0 && grid[x + shift + 1][y] != grid[x][y])) doShift = true;
                            // If the shift has either reached the grid boundaries, or the next number is not a 0 and not the same number, shift
                        else shift++;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != 0) {

                            hasMoved = true;

                            if(grid[x + shift][y] == 0) grid[x + shift][y] = grid[x][y];
                            else grid[x + shift][y] *= 2;

                            grid[x][y] = 0;

                        }

                    }

                }

            }

        }

        if(hasMoved) spawnTile();

    }

    void moveLeft() {

        boolean hasMoved = false;

        for(int x = 1; x < 4; x++) {
            // For every X-coord, starting 1 from the left and going to the right

            for(int y = 0; y < 4; y++) {
                // For every Y-coord, starting from the bottom and going to the top

                if(grid[x][y] != 0) {
                    // If the value at this coord is not empty

                    int shift = 0;
                    // Y-offset from the current coord
                    boolean doShift = false;
                    // Whether to perform the shift on each loop

                    while(!doShift) {
                        // Until the shift is performed, repeat

                        if(x - shift == 0 || (grid[x - shift - 1][y] != 0 && grid[x - shift - 1][y] != grid[x][y])) doShift = true;
                            // If the shift has either reached the grid boundaries, or the next number is not a 0 and not the same number, shift
                        else shift++;
                        // Otherwise, if the next number is a 0, or it is the same number, keep incrementing shift

                        if(doShift && shift != 0) {

                            hasMoved = true;

                            if(grid[x - shift][y] == 0) grid[x - shift][y] = grid[x][y];
                            else grid[x - shift][y] *= 2;

                            grid[x][y] = 0;

                        }

                    }

                }

            }

        }

        if(hasMoved) spawnTile();

    }

    int getTile(int x, int y) {

        return grid[x][y];

    }

}

class GUI extends JFrame {

    Board boardData;
    ArrayList<JLabel> labels;

    public GUI(Board boardData) {

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

    int textSizeFromDigits(int digits) {

        return 100 - (10 * (digits - 1));

    }

    int translateCoordToLabel(int x, int y) {

        return 4 * (3 - y) + x;

    }

    int amountOfDigits(int n) {

        int digits = 0;

        while(n != 0) {

            n /= 10;
            digits++;

        }

        return digits;

    }

}

class KeyboardInput extends KeyAdapter {

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