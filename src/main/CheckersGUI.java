package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CheckersGUI extends JFrame {
    private JPanel boardPanel;
    private JButton[][] boardButtons = new JButton[8][8];  // 8x8 grid of buttons
    private Point selectedPiece = null;  // Keeps track of selected piece for moving

    public CheckersGUI() {
        setTitle("Checkers Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);
        setLocationRelativeTo(null);  // Center the window on screen

        // Initialize the board panel with a grid layout
        boardPanel = new JPanel(new GridLayout(8, 8));
        initializeBoard();

        // Add the board panel to the frame
        add(boardPanel);
        setVisible(true);

        // Fetch the current board state and update the GUI on launch
        updateBoard();
    }

    // Initialize the board layout with clickable tiles
    private void initializeBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = new JButton();
                tile.setPreferredSize(new Dimension(80, 80));

                // Set tile colors based on checkerboard pattern
                if ((row + col) % 2 == 0) {
                    tile.setBackground(new Color(233, 224, 210));  // Light tile
                } else {
                    tile.setBackground(new Color(150, 75, 0));  // Dark tile
                    tile.addActionListener(new TileClickListener(row, col));  // Add action listener to dark tiles only
                }

                boardButtons[row][col] = tile;
                boardPanel.add(tile);
            }
        }
    }

    // Update the GUI based on the current board state from the C++ backend
    private void updateBoard() {
        int[][] boardState = Main.getBoardState();  // Fetch the current board state from C++

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = boardButtons[row][col];
                int piece = boardState[row][col];

                // Clear the current icon first
                tile.setIcon(null);

                // Set the piece icons based on the board state
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
    }

    // Handle tile clicks and manage piece movement
    private class TileClickListener implements ActionListener {
        private int row;
        private int col;

        public TileClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedPiece == null) {
                // First click: select the piece to move
                int[][] boardState = Main.getBoardState();  // Fetch the current board state from C++
                if (boardState[row][col] != 0) {  // Ensure the tile contains a piece
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
    }
}
