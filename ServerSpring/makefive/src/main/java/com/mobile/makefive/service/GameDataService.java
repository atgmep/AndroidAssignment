package com.mobile.makefive.service;

import com.mobile.makefive.common.Fix;
import com.mobile.makefive.common.Methods;
import com.mobile.makefive.entity.TblAccount;
import com.mobile.makefive.model.AppData;
import com.mobile.makefive.model.CurrentGame;
import com.mobile.makefive.model.GameData;
import com.mobile.makefive.model.Response;
import com.mobile.makefive.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GameDataService {

    @Autowired
    private AppData appData;

    @Autowired
    private CurrentGame currentGame;

    private AccountRepository accountRepository;

    public GameDataService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    // check dup
    // pack up
    public Response startMatch() {
        Methods methods = new Methods();
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        TblAccount tblAccount = methods.getUser();
        List<GameData> matches = appData.getMatches();

        for (GameData match : matches) {
            if (match.getPlayer2() == null) {
                if (match.getPlayer1().equals(tblAccount)) {
                    response.setResponse(Response.STATUS_FAIL, "Already on queue");
                    return response;
                }
                currentGame.gameData = match;
                match.setPlayer2(tblAccount);
                match.setMoveCount(1);
                System.out.println(tblAccount.getUsername() + " join match " + match.getId());
                response.setResponse(Response.STATUS_SUCCESS, "2", match.getId(), match.getPlayer1().getUsername(), match.getPlayer1().getPoint() + "");
                return response;
            }
        }
        GameData newMatch = appData.newMatch(tblAccount);
        currentGame.gameData = newMatch;
        currentGame.isCancelQueue = false;
        String matchId = newMatch.getId();
        System.out.println(tblAccount.getUsername() + " create match " + matchId);
        long waitTime = methods.getTimeNow() + Fix.WAIT_LONG;
        while (methods.getTimeNow() < waitTime) {
            try {
                Thread.sleep(Fix.WAIT_SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (currentGame.isCancelQueue) {
                response.setResponse(Response.STATUS_FAIL, "Quit queue");
                return response;
            }
            if (newMatch.getPlayer2() != null) {
                response.setResponse(Response.STATUS_SUCCESS, "1", matchId, newMatch.getPlayer2().getUsername(), newMatch.getPlayer2().getPoint() + "");
                return response;
            }


        }
        appData.removeMatch(matchId);
        System.out.println("out queue");
        response.setResponse(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        return response;
    }

    public void clearMatch(GameData match) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Methods methods = new Methods();
                TblAccount player1 = match.getPlayer1();
                TblAccount player2 = match.getPlayer2();
                Long p1point = player1.getPoint();
                Long p2point = player2.getPoint();
                if (match.isPlayer1Win()) {
                    long pointEarn = Math.max(10l, ((p2point - p1point) / 8) + 50);
                    player1.setPoint(p1point + pointEarn);
                    player1.setWin(player1.getWin() + 1);
                    player2.setPoint(p2point - pointEarn);
                    player2.setLose(player2.getLose() + 1);
                    System.out.println("End " + pointEarn + " " + player1 + " + " + player2 + " -");
                } else {
                    long pointEarn = Math.max(10l, ((p1point - p2point) / 8) + 50);
                    player1.setPoint(p1point - pointEarn);
                    player1.setLose(player1.getLose() + 1);
                    player2.setPoint(p2point + pointEarn);
                    player2.setWin(player2.getWin() + 1);
                    System.out.println("End " + pointEarn + " " + player1 + " - " + player2 + " +");
                }
                accountRepository.save(player1);
                accountRepository.save(player2);

                long maxWait = methods.getTimeNow() + 30 * 1000;
                while ((!match.isPlayer1Confirm() || !match.isPlayer2Confirm()) && methods.getTimeNow() < maxWait) {
                    try {
                        Thread.sleep(Fix.WAIT_SHORT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                appData.getMatches().remove(match);
            }
        });
        t.start();
    }


    public Response quitMatch() {
        Methods methods = new Methods();
        TblAccount currentUser = methods.getUser();

        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);

        GameData gameData = currentGame.gameData;

        if (gameData == null) {
            response.setResponse(Response.STATUS_FAIL, "Match null");
            return response;
        }
        if (gameData.isOver()) {
            response.setResponse(Response.STATUS_FAIL, "Game end");
            return response;
        }
        boolean isPlayer1;
        if (gameData.isPlayer1(currentUser)) {
            isPlayer1 = true;
        } else if (gameData.isPlayer2(currentUser)) {
            isPlayer1 = false;
        } else {
            response.setResponse(Response.STATUS_FAIL, "Illegal access");
            return response;
        }
        if (gameData.getMoveCount() == 0 && isPlayer1) {
            System.out.println("stop queue");
            appData.removeMatch(gameData.getId());
            response.setResponse(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS);
            return response;
        }
        System.out.println("forfeit");
        gameData.setPlayer1Win(!isPlayer1);
        gameData.setOver(true);
        gameData.confirmResult(isPlayer1);
        // LOST - You quit
        response.setResponse(Response.STATUS_SUCCESS, Fix.YOU_LOS);
        return response;
    }

    public Response setMove(String id, int col, int row) {
        Methods methods = new Methods();
        TblAccount currentUser = methods.getUser();
        GameData gameData = appData.getMatch(id);
        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        long winTime = methods.getTimeNow() + 35 * 1000;

        boolean isPlayer1;
        if (gameData.isPlayer1(currentUser) && gameData.isPlayer1Turn()) {
            isPlayer1 = true;
        } else if (gameData.isPlayer2(currentUser) && !gameData.isPlayer1Turn()) {
            isPlayer1 = false;
        } else {
            response.setResponse(Response.STATUS_FAIL, "Illegal access");
            return response;
        }
        if (!gameData.checkPiece(col, row, 0)) {
            response.setResponse(Response.STATUS_FAIL, "Illegal move");
            return response;
        }

        // normal
        int[][] board = gameData.getBoard();
        board[col][row] = gameData.getPiece();
        gameData.setCol(col);
        gameData.setRow(row);
        System.out.println(currentUser + " move col " + col + " row " + row);
        if (gameData.checkWin(col, row)) {
            gameData.setPlayer1Win(isPlayer1);
            gameData.setOver(true);
            // WIN - Get Streak
            clearMatch(gameData);
            response.setResponse(Response.STATUS_SUCCESS, Fix.YOU_WIN);
            return response;
        }
        gameData.setMoveCount(gameData.getMoveCount() + 1);
        int nextMoveCount = gameData.getMoveCount() + 1;

        while (true) {
            try {
                Thread.sleep(Fix.WAIT_SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gameData.getMoveCount() == nextMoveCount) {
                // Opponent move
                response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_MOV, gameData.getCol() + "x" + gameData.getRow());
                return response;
            }

            if (gameData.isOver()) {
                if (isPlayer1 == gameData.isPlayer1Win()) {
                    // WIN - Opponent quitMatch
                    clearMatch(gameData);
                    gameData.confirmResult(isPlayer1);
                    response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_LOS);
                    return response;
                } else {
                    // LOST - Opponent win
                    gameData.confirmResult(isPlayer1);
                    response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_WIN, gameData.getCol() + "x" + gameData.getRow());
                    return response;
                }
            }

            if (methods.getTimeNow() >= winTime) {
                gameData.setPlayer1Win(isPlayer1);
                gameData.setOver(true);
                System.out.println(currentUser.getUsername() + " wait Win");
                // WIN - Opponent afk
                clearMatch(gameData);
                gameData.confirmResult(isPlayer1);
                response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_AFK);
                return response;
            }
        }
    }

    public Response firstWait(String id) {
        Methods methods = new Methods();
        TblAccount currentUser = methods.getUser();
        GameData gameData = appData.getMatch(id);

        Response response = new Response(Response.STATUS_FAIL, Response.MESSAGE_FAIL);
        long winTime = methods.getTimeNow() + 37 * 1000;

        if (gameData.getMoveCount() != 1 || !gameData.isPlayer2(currentUser)) {
            response.setResponse(Response.STATUS_FAIL, "Illegal access");
            return response;
        }
        System.out.println(currentUser + " 1st wait " + gameData);
        int nextMoveCount = gameData.getMoveCount() + 1;
        while (true) {
            try {
                Thread.sleep(Fix.WAIT_SHORT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (gameData.getMoveCount() == nextMoveCount) {
                // Opponent move
                response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_MOV, gameData.getCol() + "x" + gameData.getRow());
                return response;
            }

            if (gameData.isOver()) {
                if (!gameData.isPlayer1Win()) {
                    // WIN - Opponent quitMatch
                    clearMatch(gameData);
                    gameData.confirmResult(false);
                    response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_LOS);
                    return response;
                }
            }
            if (methods.getTimeNow() >= winTime) {
                gameData.setPlayer1Win(false);
                gameData.setOver(true);
                System.out.println(currentUser.getUsername() + " wait Win");
                // WIN - Opponent afk
                clearMatch(gameData);
                gameData.confirmResult(false);
                response.setResponse(Response.STATUS_SUCCESS, Fix.OPP_AFK);
                return response;
            }
        }
    }
}
