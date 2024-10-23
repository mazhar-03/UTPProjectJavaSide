package tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.Main;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class CheckersGameTest {

    @BeforeEach
    public void setUp() {
        // Assuming JNI call initializes the board here
        Main.getBoardState();
    }

    @Test
    public void testBoardInitialization() {
        // Fetch the current board state
        int[][] board = Main.getBoardState();

        // Check that the board is properly initialized
        // Player 1 (white pieces) should occupy rows 0, 1, 2
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    assertEquals(1, board[i][j], "Expected Player 1 piece at position [" + i + "][" + j + "]");
                }
            }
        }

        // Player 2 (black pieces) should occupy rows 5, 6, 7
        for (int i = 5; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 1) {
                    assertEquals(-1, board[i][j], "Expected Player 2 piece at position [" + i + "][" + j + "]");
                }
            }
        }
    }

    @Test
    public void testValidMove() {
        // Move a Player 1 piece from (2, 1) to (3, 2) - a valid move
        boolean isValidMove = Main.movePiece(2, 1, 3, 2);
        assertTrue(isValidMove, "Move from (2, 1) to (3, 2) should be valid");

        // Check that the piece was moved
        int[][] board = Main.getBoardState();
        assertEquals(0, board[2][1], "Expected the piece to have moved from (2, 1)");
        assertEquals(1, board[3][2], "Expected Player 1 piece at position (3, 2)");
    }

    @Test
    public void testInvalidMove() {
        // Attempt to move a Player 1 piece backwards, which should be invalid
        boolean isValidMove = Main.movePiece(3, 2, 2, 1);
        assertFalse(isValidMove, "Move from (3, 2) to (2, 1) should be invalid for a normal piece");
    }

    @Test
    public void testCaptureMove() {
        // Set up a scenario where a capture is possible
        int[][] board = Main.getBoardState();
        // Assume board is correctly initialized here

        // Move should be valid: (2, 1) -> (4, 3) with a capture at (3, 2)
        boolean isValidMove = Main.movePiece(2, 1, 4, 3);
        assertTrue(isValidMove, "Capture move from (2, 1) to (4, 3) should be valid");

        // Check if the opponent piece at (3, 2) was captured
        board = Main.getBoardState();
        assertEquals(0, board[3][2], "The opponent's piece at (3, 2) should have been captured");
        assertEquals(1, board[4][3], "The player's piece should have moved to (4, 3)");
    }

}
