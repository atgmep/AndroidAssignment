package com.mobile.makefive.model;

import com.mobile.makefive.entity.TblAccount;

import java.util.UUID;

public class GameData {


    private String id;
    private TblAccount player1;
    private TblAccount player2;
    private int moveCount;
    private int col;
    private int row;
    private int[][] board;
    private boolean isPlayer1Win;
    private boolean isOver;

    public GameData(TblAccount player1) {
        this.id = UUID.randomUUID().toString();
        this.player1 = player1;
        this.player2 = null;
        this.moveCount = 0;
        this.col = -1;
        this.row = -1;
        this.board = new int[10][10];
        for (int i = 0; i <= 9; i++) {
            for (int j = 0; j <= 9; j++) {
                board[i][j] = 0;
            }
        }
        this.isPlayer1Win = true;
        this.isOver = false;
    }

    public int getPiece() {
        return moveCount % 2 == 1 ? 1 : 2;
    }

    public boolean isPlayer1Turn() {
        return moveCount % 2 == 1;
    }

    public boolean isPlayer1(TblAccount tblAccount) {
        return tblAccount.equals(player1);
    }

    public boolean isPlayer2(TblAccount tblAccount) {
        return tblAccount.equals(player2);
    }

    public boolean checkPiece(int col, int row, int piece) {
        if (0 > col || col > 9) {
//            System.out.println("ill col " + col);
            return false;
        }
        if (0 > row || row > 9) {
//            System.out.println("ill row " + col);
            return false;
        }
        return board[col][row] == piece;
    }

    private boolean checkWin(int col, int row, int deltaCol, int deltaRow) {
        int piece = board[col][row];
        int streak = 1;
        int x = col + deltaCol;
        int y = row + deltaRow;
        while (checkPiece(x, y, piece)) {
            streak++;
            x += deltaCol;
            y += deltaRow;
        }
        x = col - deltaCol;
        y = row - deltaRow;
        while (checkPiece(x, y, piece)) {
            streak++;
            x -= deltaCol;
            y -= deltaRow;
        }
        return streak >= 5;
    }

    public boolean checkWin(int col, int row) {
        if (checkWin(col, row, 0, 1)) {
            return true;
        }
        if (checkWin(col, row, 1, 1)) {
            return true;
        }
        if (checkWin(col, row, 1, 0)) {
            return true;
        }
        if (checkWin(col, row, 1, -1)) {
            return true;
        }
        return false;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TblAccount getPlayer1() {
        return player1;
    }

    public void setPlayer1(TblAccount player1) {
        this.player1 = player1;
    }

    public TblAccount getPlayer2() {
        return player2;
    }

    public void setPlayer2(TblAccount player2) {
        this.player2 = player2;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int[][] getBoard() {
        return board;
    }

    public void setBoard(int[][] board) {
        this.board = board;
    }

    public boolean isPlayer1Win() {
        return isPlayer1Win;
    }

    public void setPlayer1Win(boolean player1Win) {
        isPlayer1Win = player1Win;
    }

    public boolean isOver() {
        return isOver;
    }

    public void setOver(boolean over) {
        isOver = over;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "id='" + id + '\'' +
                '}';
    }
}
