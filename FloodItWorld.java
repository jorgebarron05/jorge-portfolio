
/*
 * |------------------------------------------------------|
 * | Assignment 9: Flood it, Part2                        |
 * | Jorge Barron and Tri Watanasuparp                    |
 * | CS2510 Fundies 2                                     |
 * |------------------------------------------------------|
 * 
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// to represent the game world
class FloodItWorld extends World implements IConstants {

  // a light-themed list of colors
  ArrayList<Color> lightColors = new ArrayList<Color>(Arrays.asList(GOLD, LIGHT_GREEN, LIGHT_BLUE,
      LIGHT_RED, LIGHT_BROWN, LIGHT_PURPLE, GREY, PEACH));

  // a dark-themed list of colors
  ArrayList<Color> darkColors = new ArrayList<Color>(Arrays.asList(DARK_ORANGE, FOREST_GREEN,
      MIDNIGHT_BLUE, BLACK, MAROON, DARK_BROWN, DARK_RED, DARK_VIOLET));

  // random variable used for creation of the colors in the world
  Random rand;

  // All the cells of the game
  ArrayList<Cell> board;

  int boardSize;
  int numOfColor;
  Color curColor;
  int numOfStep = 0;
  int timeCount = 0; // cumulative time elapsed after game has begun (updated in other methods)
  ArrayList<Color> colorTheme = this.lightColors;
  long startTime = System.currentTimeMillis(); // what is the start time of the game?

  FloodItWorld(int boardSize, int numOfColor) {
    this.rand = new Random();
    this.boardSize = boardSize;
    this.numOfColor = numOfColor;
    this.board = this.makeBoard();
  }

  // constructor with a random field allowing us to implement
  // a seed for testing purposes
  FloodItWorld(int boardSize, int numOfColor, Random rand) {
    this.rand = rand;
    this.boardSize = boardSize;
    this.numOfColor = numOfColor;
    this.board = this.makeBoard2(1);

  }

  // EFFECT:
  // updates this game world's color theme to the given ArrayList of colors
  // (Default is the light pastel colors)
  void updateColorTheme(ArrayList<Color> given) {
    this.colorTheme = given;
  }

  // EFFECT:
  // establishes the connections between all the cells in this game board
  void setCellConnection(ArrayList<ArrayList<Cell>> cellArr) {
    for (int i = 0; i < this.boardSize; i++) {
      for (int j = 0; j < this.boardSize; j++) {
        Cell curCell = cellArr.get(i).get(j);

        // sets the right and left connections
        if (curCell.y == 0) {
          curCell.setLeft(curCell);
          curCell.setRight(cellArr.get(i).get(j + 1));
        }
        else if (curCell.y == (this.boardSize - 1)) {
          curCell.setLeft(cellArr.get(i).get(j - 1));
          curCell.setRight(curCell);
        }
        else {
          curCell.setLeft(cellArr.get(i).get(j - 1));
          curCell.setRight(cellArr.get(i).get(j + 1));
        }

        // sets the top and bottom connections
        if (curCell.x == 0) {
          curCell.setTop(curCell);
          curCell.setBottom(cellArr.get(i + 1).get(j));
        }
        else if (curCell.x == (this.boardSize - 1)) {
          curCell.setTop(cellArr.get(i - 1).get(j));
          curCell.setBottom(curCell);
        }
        else {
          curCell.setTop(cellArr.get(i - 1).get(j));
          curCell.setBottom(cellArr.get(i + 1).get(j));
        }
      }
    }
  }

  // creates a two-dimensional grid of the Cells to represent the board
  public ArrayList<ArrayList<Cell>> createArrBoard() {
    ArrayList<ArrayList<Cell>> result = new ArrayList<ArrayList<Cell>>();
    for (int x = 0; x < this.boardSize; x++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      result.add(row); // adding the rows

      for (int y = 0; y < this.boardSize; y++) {
        Color randColor = new ArrayUtils().getRandArrayVal(this.numOfColor, this.colorTheme);
        Cell curCell = new Cell(x, y, randColor, false);
        result.get(x).add(curCell); // add cells to each row (thus creating the grid)
      }
    }
    // sets the connections for all of the cells in this game board
    this.setCellConnection(result);

    // top leftmost tile should be initialized as the first "flooded" tile
    result.get(0).get(0).flooded = true;
    return result;

  }

  // creates an arrayList of Cells from a 2d grid
  public ArrayList<Cell> makeBoard() {
    ArrayList<ArrayList<Cell>> listOfCell = this.createArrBoard();
    this.board = new ArrayList<Cell>();

    for (ArrayList<Cell> row : listOfCell) {
      for (Cell c : row) {
        this.board.add(c);
      }
    }
    this.curColor = this.board.get(0).color;
    return board;
  }

  // EFFECT:
  // draws the images on the given world scene
  // that do not change throughout this game world
  void placeImageConstants(WorldScene s) {
    s.placeImageXY(CREATORS_TEXT, 932, 680);
    s.placeImageXY(TITLE_TEXT, 940, 100);
    s.placeImageXY(INSTRUCTIONS_TEXT, 940, 150);
    s.placeImageXY(WINNING_INSTR_TEXT, 940, 180);
    s.placeImageXY(RESET_TEXT, 940, 210);
    s.placeImageXY(LINE_SEPARATOR, 940, 230);
    s.placeImageXY(DARK_MODE_TEXT, 940, 250);
    s.placeImageXY(LIGHT_MODE_TEXT, 940, 270);
    s.placeImageXY(WARNING_TEXT, 940, 290);
    s.placeImageXY(LINE_SEPARATOR, 940, 310);
  }

  @Override
  // produces a game scene with all the cells on the board
  public WorldScene makeScene() {
    // calculates the number of maximum steps allowed for user to have for them to
    // win
    int stepCalculations = (this.boardSize / 2) * (this.numOfColor / 2) + this.numOfColor;
    WorldImage stepsImage = new TextImage(
        "Steps: " + Integer.toString(this.numOfStep) + " / " + Integer.toString(stepCalculations),
        38, FontStyle.BOLD, MAROON);

    // Additional images put in to track information about this GameWorld
    WorldImage colorsImage = new TextImage("Colors: " + Integer.toString(numOfColor), 25,
        DARK_ORANGE);

    // timer image displayed:
    WorldImage timeImage = new TextImage(
        "Time Elapsed: " + Integer.toString(timeCount) + " seconds", 20, FOREST_GREEN);

    WorldScene s = this.getEmptyScene();

    for (Cell c : this.board) {
      s.placeImageXY(c.drawCell(), (c.x * CELL_SIZE) + (CELL_SIZE / 2),
          (c.y * CELL_SIZE) + (CELL_SIZE / 2));
    }
    // the whole board drawn including additional images:
    this.placeImageConstants(s);
    s.placeImageXY(stepsImage, 930, 550);
    s.placeImageXY(colorsImage, 930, 615);
    s.placeImageXY(timeImage, 940, 400);
    return s;
  }

  /* BIG BANG AND ALL ITS HANDLERS AND HELPERS FOR USER INTERACTIVITY: */

  // EFFECT:
  // resets the game board to its initial state
  public void reset() {
    this.board = this.makeBoard();
    this.numOfStep = 0;
    this.startTime = System.currentTimeMillis();
  }

  // EFFECT:
  // sets the flood mechanic (changing colors based on neighbors)
  // for the user's current clicked cell
  public void floodFill() {
    for (Cell c : this.board) {
      if (c.flooded) {
        c.updateNeighbors(this.curColor);
      }
    }
  }

  // EFFECT:
  // updates the state of the board based upon the key pressed
  public void onKeyEvent(String ke) {
    if (ke.equals("r")) {
      this.reset();
    }

    // extra credit feature implementing a dark mode
    else if (ke.equals("d")) {
      this.updateColorTheme(this.darkColors);
      this.reset();

    }
    // extra credit feature implementing a light mode
    else if (ke.equals("l")) {
      this.updateColorTheme(this.lightColors);
      this.reset();
    }
  }

  // EFFECT:
  // updates the state of the board on every tick
  public void onTick() {

    // extra credit method counting the time elapsed in the game
    long oneThousand = 1000;
    int elapsedTime = (int) ((System.currentTimeMillis() - this.startTime) / oneThousand);

    timeCount = (elapsedTime); // updating how much time has elapsed in the game

  }

  // EFFECT:
  // the game board is updated based on where the user clicks
  // (tracked through the posn)
  public void onMousePressed(Posn location) {
    int xIndex = (location.x / CELL_SIZE);
    int yIndex = (location.y / CELL_SIZE);

    Cell curCell;

    try {
      curCell = this.board.get((xIndex * this.boardSize) + yIndex);
      
      if (curCell.color.equals(this.curColor)) {
        // do nothing
      }
      else if (curCell.isNextToFlooded()) {
        curCell.flooded = true;   
        this.numOfStep++;
      }
      this.curColor = curCell.color;
      this.floodFill();
    }
    catch (IndexOutOfBoundsException e) {
      // do nothing if exception is thrown
    }

  }

  // are all the cells in this game board flooded,
  // meaning their colors are the same as the current color?
  public boolean allFlooded() {
    boolean bool = true;
    for (Cell c : this.board) {
      bool = bool && c.color.equals(this.curColor);
    }
    return bool;
  }

  // determines if the user has won the game
  public boolean gameWin() {
    int stepCalculations = (this.boardSize / 2) * (this.numOfColor / 2) + this.numOfColor;
    return this.allFlooded() && this.numOfStep <= stepCalculations;
  }

  // determines if the user has lost the game
  public boolean gameLose() {
    int stepCalculations = (this.boardSize / 2) * (this.numOfColor / 2) + this.numOfColor;
    return this.numOfStep >= stepCalculations && !this.allFlooded();
  }

  // produces the last scene of the game
  @Override
  public WorldScene lastScene(String msg) {
    WorldImage textImage = new TextImage(msg, 30, Color.BLACK);
    WorldScene s = this.makeScene();
    s.placeImageXY(textImage, 930, 460);
    return s;
  }

  // determines the world end based on user interactivity
  public WorldEnd worldEnds() {
    if (this.gameWin()) {
      return new WorldEnd(true,
          this.lastScene("You Won in " + String.valueOf(this.numOfStep) + " steps!"));
    }
    else if (this.gameLose()) {
      return new WorldEnd(true, this.lastScene("You Lost!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }

  }

  /* SECOND VERSIONS OF SOME METHODS FOR TESTING PURPOSES WITH SEEDS */

  // second version of makeBoard implementing a seed for testing purposes
  public ArrayList<Cell> makeBoard2(int seed) {
    ArrayList<ArrayList<Cell>> listOfCell = this.createArrBoard2(seed);
    this.board = new ArrayList<Cell>();

    for (ArrayList<Cell> row : listOfCell) {
      for (Cell c : row) {
        this.board.add(c);
      }
    }
    this.curColor = this.board.get(0).color;
    return board;
  }

  // second version of createArrBoard implementing a seed for testing purposes
  public ArrayList<ArrayList<Cell>> createArrBoard2(int seed) {
    ArrayList<ArrayList<Cell>> result = new ArrayList<ArrayList<Cell>>();
    for (int x = 0; x < this.boardSize; x++) {
      ArrayList<Cell> row = new ArrayList<Cell>();
      result.add(row);

      for (int y = 0; y < this.boardSize; y++) {
        Color randColor = new ArrayUtils().getRandArrayVal(this.numOfColor, this.colorTheme, seed);
        Cell curCell = new Cell(x, y, randColor, false);
        result.get(x).add(curCell);
      }
    }
    this.setCellConnection(result);

    result.get(0).get(0).flooded = true;
    return result;
  }
}
