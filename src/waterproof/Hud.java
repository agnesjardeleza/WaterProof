/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package waterproof;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.scene.Node;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author PC
 */
public class Hud {
    private AssetManager assetManager;
    private Node guiNode;
    private int screenWidth,screenHeight;
    private final int fontSize = 30;
    private final int fontSize1 = 50;
    
    private final int multiplierExpiryTime = 2000;
    private final int maxMultiplier = 25;
    
    public int player1score = 0;
    public int player2score = 0;
    
    private long multiplierActivationTime;
    private int scoreForExtraLife;
    
    private BitmapFont guiFont;
    private BitmapText livesText;
    private BitmapText scoreText;
    private BitmapText multiplierText;
    private Node gameOverNode;
    
    public Hud(AssetManager assetManager, Node guiNode, int screenWidth, int screenHeight) {
        this.assetManager = assetManager;
        this.guiNode = guiNode;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        setupText();
    }
    
    public void setupText() {
        guiFont = assetManager.loadFont("Interface/Fonts/CenturyGothic.fnt");
        
        livesText = new BitmapText(guiFont,false);
        livesText.setLocalTranslation(30,screenHeight-30,0);
        livesText.setSize(fontSize);
        livesText.setText("Player 1: "+player1score);
        guiNode.attachChild(livesText);
        
        scoreText = new BitmapText(guiFont, true);
        scoreText.setLocalTranslation(screenWidth - 200,screenHeight-30,0);
        scoreText.setSize(fontSize);
        scoreText.setText("Player 2: "+player2score);
        guiNode.attachChild(scoreText);   
        reset();
    }
    
    public void reset() {
           player1score = 0;
           player2score = 0;
           updateHUD();
     }
    public void addPoints(String deadPlayer) {
        if(deadPlayer.equals("Player2")){
            player1score += 1;
        }
        else if (deadPlayer.equals("Player1")){
            player2score += 1;
        }
        updateHUD();
    }
    
    public boolean checkScores() {
        if(player1score < 3 && player2score < 3) {return false;}        
        return true;
    }
    
    private void updateHUD() {
        livesText.setText("Player 1: "+player1score);
        scoreText.setText("Player 2: "+player2score);
    }
    
    public void update(int player1, int player2) {
        player1score = player1;
        player2score = player2;
        updateHUD();
    }
    
    public void endGame() {
    // init gameOverNode
        String winner;
        gameOverNode = new Node();
        gameOverNode.setLocalTranslation(screenWidth/2 - 180, screenHeight/2 + 100,0);
        guiNode.attachChild(gameOverNode);

        // check highscore
        if (player1score > player2score) {winner = "Player 1 Wins";}
        else {winner = "Player 2 Wins";}

        // init and display text
        BitmapText gameOverText = new BitmapText(guiFont, false);
        gameOverText.setLocalTranslation(0,0,0);
        gameOverText.setSize(fontSize1);
        gameOverText.setText("Game Set");
        gameOverNode.attachChild(gameOverText);

        BitmapText yourScoreText = new BitmapText(guiFont, false);
        yourScoreText.setLocalTranslation(0,-50,0);
        yourScoreText.setSize(fontSize1);
        yourScoreText.setText(winner);
        gameOverNode.attachChild(yourScoreText);
    }
    public int getPlayer1Score() {
        return player1score;
    }
    public int getPlayer2Score() {
        return player2score;
    }
}
