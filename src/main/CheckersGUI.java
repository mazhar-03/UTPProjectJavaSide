package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class CheckersGUI extends JFrame {
    private final JPanel boardPanel;
    private final JButton[][] boardButtons = new JButton[8][8];  // 8x8 grid of buttons
    private Point selectedPiece = null;  // keeps track of selected piece for moving
    private final Point highlightedTile = new Point(0, 0);  // keep track of the currently highlighted tile

    public CheckersGUI() {
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);

        boardPanel = new JPanel(new GridLayout(8, 8));
        initializeBoard();

        add(boardPanel);
        setVisible(true);

        // Fetch the current board state and update the GUI on launch
        updateBoard();

        // Add a KeyListener to listen for arrow keys and Enter key
        addKeyListener(new BoardKeyListener());
        setFocusable(true);
        requestFocusInWindow();  // Request focus when the game starts
        highlightTile(0, 0);
    }


    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = new JButton();
                tile.setPreferredSize(new Dimension(80, 80));

                // Set tile colors based on checkerboard pattern
                if ((row + col) % 2 == 0) {
                    tile.setBackground(new Color(233, 224, 210));  // light tile
                } else {
                    tile.setBackground(new Color(150, 75, 0));  // dark tile
                    tile.addActionListener(new TileClickListener(row, col));  // Add action listener to dark tiles only
                }

                boardButtons[row][col] = tile;
                boardPanel.add(tile);
            }
        }
    }

    // highlighting the currently selected tile
    private void highlightTile(int row, int col) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton tile = boardButtons[i][j];
                if ((i + j) % 2 == 0) {
                    tile.setBackground(new Color(233, 224, 210));  // Reset light tile color
                } else {
                    tile.setBackground(new Color(150, 75, 0));  // Reset dark tile color
                }
            }
        }
        boardButtons[row][col].setBackground(Color.YELLOW);  // Highlight selected tile
        highlightedTile.setLocation(row, col);  // Update highlighted tile position
    }

    // after a move is made, check for the endgame status
    private void checkEndgameStatus() {
        int result = Main.checkEndgame();  // Call the JNI method to check the game status

        if (result == 1) {
            JOptionPane.showMessageDialog(this, "Player 1 Wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            closeGame();
        } else if (result == -1) {
            JOptionPane.showMessageDialog(this, "Player 2 Wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            closeGame();
        } else if (result == 2) {
            JOptionPane.showMessageDialog(this, "It's a draw!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            closeGame();
        }
    }

    private void closeGame() {
        System.exit(0);  // Exit the application
    }

    // update the GUI side on the current board state from the C++ backend (W JNI)
    private void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = boardButtons[row][col];
                int piece = Main.getTileState(row, col);  // Fetch the state of each individual tile

                // Clear the current icon first
                tile.setIcon(null);

                // Set the piece icons based on the tile state
                if (piece == 1) {
                    tile.setIcon(new ImageIcon("images/white.png"));  // White piece
                } else if (piece == -1) {
                    tile.setIcon(new ImageIcon("images/black.png"));  // Black piece
                } else if (piece == 2) {
                    tile.setIcon(new ImageIcon("images/white_king.png"));  // White king piece
                } else if (piece == -2) {
                    tile.setIcon(new ImageIcon("images/black_king.png"));  // Black king piece
                }
            }
        }
        // Check for the endgame status after updating the board
        checkEndgameStatus();
    }

    // handling the pieces' movements according to mouse clicks
    private class TileClickListener implements ActionListener {
        private final int row;
        private final int col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            processMove(row, col);
            // ensure keyboard focus is regained after a mouse click.
            // The game can be played with mouse and keyboard simultaneously
            requestFocusInWindow();
        }
    }

    // Handling the move when a tile is clicked or selected via keyboard
    // Handling the move when a tile is clicked or selected via keyboard
    private void processMove(int row, int col) {
        if (selectedPiece == null) {
            // First click: select the piece to move
            int piece = Main.getTileState(row, col);  // Query the tile state instead of the board
            if (piece != 0) {  // Ensure the tile contains a piece
                selectedPiece = new Point(row, col);  // Save the selected piece position
            }
        } else {
            // Second click: attempt to move the selected piece
            boolean moveSuccess = Main.movePiece(selectedPiece.x, selectedPiece.y, row, col);  // Send move to C++
            if (moveSuccess) {
                updateBoard();  // Re-fetch the board state and update the GUI
            }
            selectedPiece = null;  // Reset the selection after attempting the move
        }
    }


    // checking keyboard things. Since just gonna use keyPressed() extending KeyAdapter is okay
    private class BoardKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int row = highlightedTile.x;
            int col = highlightedTile.y;

            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if (row > 0) highlightTile(row - 1, col);
                    break;
                case KeyEvent.VK_DOWN:
                    if (row < 7) highlightTile(row + 1, col);
                    break;
                case KeyEvent.VK_LEFT:
                    if (col > 0) highlightTile(row, col - 1);
                    break;
                case KeyEvent.VK_RIGHT:
                    if (col < 7) highlightTile(row, col + 1);
                    break;
                case KeyEvent.VK_ENTER:
                    processMove(row, col);  // select or move the piece when "enter" is pressed
                    break;
            }
        }
    }
}
