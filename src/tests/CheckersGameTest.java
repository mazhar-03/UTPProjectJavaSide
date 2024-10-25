package tests;

import main.Main;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CheckersGameTest {

    @Test
    public void testCaptureMove() {
        // Set up a scenario where a capture is possible.
        // Player 1 piece at (2, 1) and Player 2 piece at (3, 2)
        int[][] board = Main.getBoardState();
        board[2][1] = 1;  // Player 1's piece
        board[3][2] = -1; // Player 2's piece

        // Attempt to capture the opponent's piece by jumping from (2, 1) to (4, 3)
        boolean isValidMove = Main.movePiece(2, 1, 4, 3);

        // Verify that the move was successful
        assertTrue(isValidMove, "Capture move from (2, 1) to (4, 3) should be valid");

        // Fetch the updated board state after the move
        board = Main.getBoardState();

        // Check that the opponent's piece at (3, 2) was captured
        assertEquals(0, board[3][2], "The opponent's piece at (3, 2) should have been captured");

        // Check that Player 1's piece is now at (4, 3)
        assertEquals(1, board[4][3], "Player 1's piece should have moved to (4, 3)");
    }

    @Test
    public void testInvalidMove() {
        // Set up a scenario with an invalid move attempt
        int[][] board = Main.getBoardState();
        board[2][1] = 1;  // Player 1's piece
        board[4][3] = 0;  // Ensure the destination is empty

        // Attempt an invalid move without capturing: (2, 1) to (4, 3) without jumping
        boolean isValidMove = Main.movePiece(2, 1, 4, 3);

        // Verify that the move was not successful
        assertFalse(isValidMove, "The move from (2, 1) to (4, 3) should be invalid without capture");
    }

    @Test
    public void testKingPromotion() {
        // Set up a scenario where Player 1 promotes a piece to a king
        int[][] board = Main.getBoardState();
        board[6][1] = 1;  // Player 1's piece near the end of the board

        // Move the piece to the last row to promote it to a king
        boolean isValidMove = Main.movePiece(6, 1, 7, 0);

        // Verify that the move was successful
        assertTrue(isValidMove, "The move from (6, 1) to (7, 0) should be valid for promotion");

        // Fetch the updated board state
        board = Main.getBoardState();

        // Check that the piece was promoted to a king (represented by '2')
        assertEquals(2, board[7][0], "Player 1's piece should have been promoted to a king");
    }

    @Test
    public void testEndgameCheck() {
        // Set up a scenario where Player 1 wins
        int[][] board = Main.getBoardState();
        board[0][1] = 1;  // Player 1's piece
        board[7][6] = 0;  // No Player 2 pieces left

        // Check if the game is correctly detecting the win
        int endgameStatus = Main.checkEndgame();
        assertEquals(1, endgameStatus, "Player 1 should win since Player 2 has no pieces left");
    }

    @Test
    public void testDrawScenario() {
        // Set up a scenario where neither player can move
        int[][] board = Main.getBoardState();
        board[0][0] = 1;  // Player 1's piece
        board[1][1] = -1; // Player 2's piece
        board[2][2] = 0;  // No more moves left for either player

        // Check if the game correctly detects a draw
        int endgameStatus = Main.checkEndgame();
        assertEquals(2, endgameStatus, "The game should result in a draw when neither player can move");
    }
}
