import javax.swing.*;
import java.awt.*;

public class CheckersGUI extends JFrame {
    private JPanel boardPanel;
    private JButton[][] boardButtons = new JButton[8][8];
    private Point selectedPiece = null;

    public CheckersGUI() {
        setTitle("Checkers Game");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        boardPanel = new JPanel(new GridLayout(8, 8));
        add(boardPanel, BorderLayout.CENTER);
        setFocusable(true);
        setVisible(true);

        // Display the initial board state
        updateBoard();
    }

    private void initializeBoard() {
        // Fetch the current board state from C++ through JNI
        int[][] boardState = Main.getBoardState();  // Assume this fetches the initial setup from C++

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = new JButton();
                tile.setPreferredSize(new Dimension(80, 80));  // Adjust size if needed

                // Set board colors: light tiles and dark tiles
                if ((row + col) % 2 == 0) {
                    tile.setBackground(new Color(233, 224, 210));  // Light tile color
                } else {
                    tile.setBackground(new Color(150, 75, 0));  // Dark tile color

                    // Add pieces to the dark squares based on the boardState from C++
                    if (boardState[row][col] == 1) {
                        tile.setIcon(new ImageIcon("C:\\Users\\mazha\\OneDrive\\Pulpit\\PROJECT\\Checkers\\images\\white.png"));  // Player 1's piece (White)
                    } else if (boardState[row][col] == -1) {
                        tile.setIcon(new ImageIcon("C:\\Users\\mazha\\OneDrive\\Pulpit\\PROJECT\\Checkers\\images\\black.png"));  // Player 2's piece (Black)
                    }
                }

                // Set up mouse click event for player interactions
                int finalRow = row;
                int finalCol = col;
                tile.addActionListener(e -> handleTileClick(finalRow, finalCol));  // Handle clicks for real-time interaction

                // Add the tile to the board
                boardButtons[row][col] = tile;
                boardPanel.add(tile);
            }
        }
    }


    private void updateBoard() {
        int[][] boardState = Main.getBoardState(); // Fetch current board state from C++
        boardPanel.removeAll(); // Clear old board display

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton tile = new JButton();
                if ((row + col) % 2 == 0) {
                    tile.setBackground(new Color(233,224,210));
                } else {
                    tile.setBackground(new Color(150,75,0));
                    int piece = boardState[row][col];
                    if (piece == 1) tile.setText("P"); // Regular piece
                    if (piece == 2) tile.setText("K"); // King piece
                }
                int finalRow = row;
                int finalCol = col;
                tile.addActionListener(e -> handleTileClick(finalRow, finalCol)); // Handle move
                boardButtons[row][col] = tile;
                boardPanel.add(tile);
            }
        }
        boardPanel.revalidate(); // Refresh GUI
    }

    private void handleTileClick(int row, int col) {
        // Check if the current piece selection is null (i.e., no piece selected yet)
        if (selectedPiece == null) {
            // Select the piece if there is one on the clicked tile
            if (isPieceSelectable(row, col)) {
                selectedPiece = new Point(row, col); // Initialize selectedPiece with the current piece's position
                boardButtons[row][col].setBackground(Color.YELLOW); // Highlight the selected tile
            }
        } else {
            // Now, selectedPiece is initialized and we can attempt a move
            boolean moveSuccess = Main.movePiece(selectedPiece.x, selectedPiece.y, row, col);
            if (moveSuccess) {
                updateBoard(); // Re-fetch and update the board if the move is valid
            }
            // Reset the selection and revert the background color
            boardButtons[selectedPiece.x][selectedPiece.y].setBackground(Color.DARK_GRAY);
            selectedPiece = null; // Reset the selectedPiece to allow for the next move
        }
    }
    private boolean isPieceSelectable(int row, int col) {
        // Assume 0 means no piece, and any other value means a piece is present
        int[][] boardState = Main.getBoardState(); // Fetch current board state via JNI
        return boardState[row][col] != 0; // Return true if there's a piece on this tile
    }
}