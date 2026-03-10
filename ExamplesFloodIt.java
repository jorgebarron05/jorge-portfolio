import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import tester.*;
import javalib.worldcanvas.WorldCanvas;
import java.awt.Color;
import javalib.worldimages.*;

// examples and tests for the FloodItWorld class as well as other classes
// relevant in its implementation
class ExamplesFloodIt implements IConstants {
  ExamplesFloodIt() {
  }

  WorldCanvas mainCanvas;

  FloodItWorld gameWorld2x2;
  FloodItWorld gameWorld6x6;
  FloodItWorld gameWorld10x10;
  FloodItWorld gameWorld14x14;
  FloodItWorld gameWorld18x18;
  FloodItWorld gameWorld22x22;
  FloodItWorld gameWorld26x26;

  Cell cellGold;
  Cell cellLightGreen;
  Cell cellLightBlue;
  Cell cellLightRed;
  Cell cellLightBrown;
  Cell cellLightPurple;
  Cell cellGrey;
  Cell cellPeach;

  ArrayList<Color> lightColors;
  ArrayList<Color> darkColors;
  ArrayList<Integer> oneToFiveList;

  // EFFECT:
  // sets up the initial conditions for the FloodIt game
  void initConditions() {

    this.mainCanvas = new WorldCanvas(1100, 790);
    this.gameWorld2x2 = new FloodItWorld(2, 3, new Random(1));
    this.gameWorld6x6 = new FloodItWorld(6, 5, new Random(1));
    this.gameWorld10x10 = new FloodItWorld(10, 5);
    this.gameWorld14x14 = new FloodItWorld(14, 6);
    this.gameWorld18x18 = new FloodItWorld(18, 6);
    this.gameWorld22x22 = new FloodItWorld(22, 7);
    this.gameWorld26x26 = new FloodItWorld(26, 8, new Random(1));

    this.cellGold = new Cell(10, 10, GOLD, false);
    this.cellLightGreen = new Cell(8, 9, LIGHT_GREEN, false);
    this.cellLightBlue = new Cell(4, 8, LIGHT_BLUE, false);
    this.cellLightRed = new Cell(3, 5, LIGHT_RED, false);
    this.cellLightBrown = new Cell(2, 2, LIGHT_BROWN, true);
    this.cellLightPurple = new Cell(13, 25, LIGHT_PURPLE, true);
    this.cellGrey = new Cell(20, 15, GREY, true);
    this.cellPeach = new Cell(26, 26, PEACH, true);

    this.lightColors = new ArrayList<Color>(Arrays.asList(GOLD, LIGHT_GREEN, LIGHT_BLUE, LIGHT_RED,
        LIGHT_BROWN, LIGHT_PURPLE, GREY, PEACH));

    this.darkColors = new ArrayList<Color>(Arrays.asList(DARK_ORANGE, FOREST_GREEN, MIDNIGHT_BLUE,
        BLACK, MAROON, DARK_BROWN, DARK_RED, DARK_VIOLET));

    this.oneToFiveList = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
  }

  // tests for the method randMaker in the Utils class,
  // implementing a seed for testing purposes
  void testRandMaker(Tester t) {
    this.initConditions();

    t.checkExpect(new Utils().randMaker(5, 1), 0);
    t.checkExpect(new Utils().randMaker(100, 1), 85);
    t.checkExpect(new Utils().randMaker(9, 1), 6);

  }

  // tests for the method getRandArrayVal in the ArrayUtils class
  void testGetRandArrayVal(Tester t) {
    this.initConditions();

    t.checkExpect(new ArrayUtils().getRandArrayVal(3, this.lightColors, 1), GOLD);
    t.checkExpect(new ArrayUtils().getRandArrayVal(8, this.lightColors, 1), LIGHT_PURPLE);
    t.checkExpect(new ArrayUtils().getRandArrayVal(5, this.oneToFiveList, 1), 1);

  }

  // tests for the drawCell method in the Cell class
  void testDrawCell(Tester t) {
    this.initConditions();

    t.checkExpect(this.cellGold.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, GOLD)));

    t.checkExpect(this.cellLightGreen.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, LIGHT_GREEN)));

    t.checkExpect(this.cellLightBlue.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, LIGHT_BLUE)));

    t.checkExpect(this.cellLightRed.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, LIGHT_RED)));

    t.checkExpect(this.cellLightBrown.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, LIGHT_BROWN)));

    t.checkExpect(this.cellLightPurple.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, LIGHT_PURPLE)));

    t.checkExpect(this.cellGrey.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, GREY)));

    t.checkExpect(this.cellPeach.drawCell(),
        new OverlayImage(new StarImage(30 / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
            new RectangleImage(30, 30, OutlineMode.SOLID, PEACH)));

  }

  // tests for the setter methods in the Cell class
  void testSetters(Tester t) {
    this.initConditions();

    this.cellGold.setLeft(this.cellLightBlue);
    this.cellGold.setTop(this.cellLightRed);
    this.cellGold.setRight(this.cellLightPurple);
    this.cellGold.setBottom(this.cellLightGreen);

    this.cellLightBlue.setLeft(this.cellLightBlue);
    this.cellLightBlue.setTop(this.cellLightBlue);
    this.cellLightBlue.setBottom(this.cellLightBlue);

    this.cellLightPurple.setRight(this.cellLightPurple);
    this.cellLightPurple.setTop(this.cellLightPurple);
    this.cellLightPurple.setBottom(this.cellLightPurple);

    this.cellLightRed.setRight(this.cellLightRed);
    this.cellLightRed.setTop(this.cellLightRed);
    this.cellLightRed.setLeft(this.cellLightRed);

    this.cellLightGreen.setRight(this.cellLightGreen);
    this.cellLightGreen.setBottom(this.cellLightGreen);
    this.cellLightGreen.setLeft(this.cellLightGreen);

    t.checkExpect(this.cellGold.left, this.cellLightBlue);
    t.checkExpect(this.cellGold.top, this.cellLightRed);
    t.checkExpect(this.cellGold.right, this.cellLightPurple);
    t.checkExpect(this.cellGold.bottom, this.cellLightGreen);

    t.checkExpect(this.cellLightBlue.left, this.cellLightBlue);
    t.checkExpect(this.cellLightBlue.top, this.cellLightBlue);
    t.checkExpect(this.cellLightBlue.bottom, this.cellLightBlue);

    t.checkExpect(this.cellLightPurple.right, this.cellLightPurple);
    t.checkExpect(this.cellLightPurple.top, this.cellLightPurple);
    t.checkExpect(this.cellLightPurple.bottom, this.cellLightPurple);

    t.checkExpect(this.cellLightRed.right, this.cellLightRed);
    t.checkExpect(this.cellLightRed.top, this.cellLightRed);
    t.checkExpect(this.cellLightRed.left, this.cellLightRed);

    t.checkExpect(this.cellLightGreen.right, this.cellLightGreen);
    t.checkExpect(this.cellLightGreen.bottom, this.cellLightGreen);
    t.checkExpect(this.cellLightGreen.left, this.cellLightGreen);

  }

  // tests for the method createArrBoard in the FloodItWorld class
  void testCreateArrBoard(Tester t) {
    this.initConditions();

    t.checkExpect(this.gameWorld6x6.createArrBoard().get(0).get(0).x, 0);
    t.checkExpect(this.gameWorld6x6.createArrBoard().get(3).get(5).y, 5);
    t.checkExpect(this.gameWorld6x6.createArrBoard().get(2).get(2).x, 2);
    t.checkExpect(this.gameWorld6x6.createArrBoard().size(), 6);
    t.checkExpect(this.gameWorld6x6.createArrBoard().get(0).get(0).flooded, true);

    t.checkExpect(this.gameWorld2x2.createArrBoard().get(0).get(0).x, 0);
    t.checkExpect(this.gameWorld2x2.createArrBoard().get(1).get(1).y, 1);
    t.checkExpect(this.gameWorld2x2.createArrBoard().get(1).get(1).x, 1);
    t.checkExpect(this.gameWorld2x2.createArrBoard().size(), 2);
    t.checkExpect(this.gameWorld2x2.createArrBoard().get(0).get(0).flooded, true);

    t.checkExpect(this.gameWorld26x26.createArrBoard().get(25).get(0).x, 25);
    t.checkExpect(this.gameWorld26x26.createArrBoard().get(13).get(25).y, 25);
    t.checkExpect(this.gameWorld26x26.createArrBoard().get(10).get(24).x, 10);
    t.checkExpect(this.gameWorld26x26.createArrBoard().size(), 26);
    t.checkExpect(this.gameWorld26x26.createArrBoard().get(0).get(0).flooded, true);

    // This method also sets up the cell connections which
    // is thoroughly tested in testSetConnection.

  }

  // tests for the method makeBoard in the FloodItWorld class
  void testMakeBoard(Tester t) {
    this.initConditions();

    t.checkExpect(this.gameWorld2x2.curColor, this.gameWorld2x2.board.get(0).color);
    t.checkExpect(this.gameWorld2x2.makeBoard().size(), 4);

    t.checkExpect(this.gameWorld10x10.curColor, this.gameWorld10x10.board.get(0).color);
    t.checkExpect(this.gameWorld10x10.makeBoard().size(), 100);

    t.checkExpect(this.gameWorld26x26.curColor, this.gameWorld26x26.board.get(0).color);
    t.checkExpect(this.gameWorld26x26.makeBoard().size(), 676);

  }

  // NOTE to TA: please uncomment the code below to see our tests for makeScene
  /*
   * // test for the makeScene method in the class FloodItWorld for the 26 x 26
   * //
   * game board boolean testMakeScene26x26(Tester t) { this.initConditions();
   * 
   * return mainCanvas.drawScene(this.gameWorld26x26.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * // test for the makeScene method in the class FloodItWorld for the 10 x 10
   * //
   * game board boolean testMakeScene10x10(Tester t) { this.initConditions();
   * 
   * return mainCanvas.drawScene(this.gameWorld10x10.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * // test for the makeScene method in the class FloodItWorld for the 2 x 2 //
   * game board boolean testMakeScene2x2(Tester t) { this.initConditions();
   * 
   * return mainCanvas.drawScene(this.gameWorld2x2.makeScene()) &&
   * mainCanvas.show(); }
   */

  // tests the method setCellConnection in the class FloodItWorld
  void testSetCellConnection(Tester t) {
    this.initConditions();
    ArrayList<ArrayList<Cell>> gameArrBoard6x6 = this.gameWorld6x6.createArrBoard();
    ArrayList<ArrayList<Cell>> gameArrBoard2x2 = this.gameWorld2x2.createArrBoard();
    ArrayList<ArrayList<Cell>> gameArrBoard26x26 = this.gameWorld26x26.createArrBoard();

    t.checkExpect(gameArrBoard2x2.get(0).get(1).left, gameArrBoard2x2.get(0).get(0));
    t.checkExpect(gameArrBoard2x2.get(0).get(0).right, gameArrBoard2x2.get(0).get(1));
    t.checkExpect(gameArrBoard2x2.get(0).get(1).bottom, gameArrBoard2x2.get(1).get(1));
    t.checkExpect(gameArrBoard2x2.get(1).get(1).top, gameArrBoard2x2.get(0).get(1));
    t.checkExpect(gameArrBoard2x2.get(1).get(1).left, gameArrBoard2x2.get(1).get(0));
    t.checkExpect(gameArrBoard2x2.get(0).get(1).right, gameArrBoard2x2.get(0).get(1));

    t.checkExpect(gameArrBoard6x6.get(1).get(5).left, gameArrBoard6x6.get(1).get(4));
    t.checkExpect(gameArrBoard6x6.get(0).get(0).left, gameArrBoard6x6.get(0).get(0));
    t.checkExpect(gameArrBoard6x6.get(0).get(5).left, gameArrBoard6x6.get(0).get(4));
    t.checkExpect(gameArrBoard6x6.get(0).get(5).right, gameArrBoard6x6.get(0).get(5));
    t.checkExpect(gameArrBoard6x6.get(0).get(5).top, gameArrBoard6x6.get(0).get(5));
    t.checkExpect(gameArrBoard6x6.get(0).get(5).bottom, gameArrBoard6x6.get(1).get(5));
    t.checkExpect(gameArrBoard6x6.get(5).get(5).left, gameArrBoard6x6.get(5).get(4));
    t.checkExpect(gameArrBoard6x6.get(5).get(5).right, gameArrBoard6x6.get(5).get(5));
    t.checkExpect(gameArrBoard6x6.get(5).get(5).top, gameArrBoard6x6.get(4).get(5));
    t.checkExpect(gameArrBoard6x6.get(5).get(5).bottom, gameArrBoard6x6.get(5).get(5));
    t.checkExpect(gameArrBoard6x6.get(0).get(0).left, gameArrBoard6x6.get(0).get(0));
    t.checkExpect(gameArrBoard6x6.get(0).get(0).right, gameArrBoard6x6.get(0).get(1));
    t.checkExpect(gameArrBoard6x6.get(0).get(0).top, gameArrBoard6x6.get(0).get(0));
    t.checkExpect(gameArrBoard6x6.get(0).get(0).bottom, gameArrBoard6x6.get(1).get(0));
    t.checkExpect(gameArrBoard6x6.get(5).get(0).left, gameArrBoard6x6.get(5).get(0));
    t.checkExpect(gameArrBoard6x6.get(5).get(0).right, gameArrBoard6x6.get(5).get(1));
    t.checkExpect(gameArrBoard6x6.get(5).get(0).top, gameArrBoard6x6.get(4).get(0));
    t.checkExpect(gameArrBoard6x6.get(5).get(0).bottom, gameArrBoard6x6.get(5).get(0));

    t.checkExpect(gameArrBoard6x6.get(0).get(1).left, gameArrBoard6x6.get(0).get(0));
    t.checkExpect(gameArrBoard6x6.get(1).get(1).right, gameArrBoard6x6.get(1).get(2));
    t.checkExpect(gameArrBoard6x6.get(1).get(5).right, gameArrBoard6x6.get(1).get(5));
    t.checkExpect(gameArrBoard6x6.get(1).get(5).top, gameArrBoard6x6.get(0).get(5));
    t.checkExpect(gameArrBoard6x6.get(1).get(5).bottom, gameArrBoard6x6.get(2).get(5));

    t.checkExpect(gameArrBoard26x26.get(0).get(1).left, gameArrBoard26x26.get(0).get(0));
    t.checkExpect(gameArrBoard26x26.get(0).get(1).right, gameArrBoard26x26.get(0).get(2));
    t.checkExpect(gameArrBoard26x26.get(0).get(1).top, gameArrBoard26x26.get(0).get(1));
    t.checkExpect(gameArrBoard26x26.get(0).get(1).bottom, gameArrBoard26x26.get(1).get(1));
    t.checkExpect(gameArrBoard26x26.get(0).get(25).left, gameArrBoard26x26.get(0).get(24));
    t.checkExpect(gameArrBoard26x26.get(0).get(25).right, gameArrBoard26x26.get(0).get(25));
    t.checkExpect(gameArrBoard26x26.get(0).get(25).top, gameArrBoard26x26.get(0).get(25));
    t.checkExpect(gameArrBoard26x26.get(0).get(25).bottom, gameArrBoard26x26.get(1).get(25));
  }

  // tests for the updateNeighbors method in the Cell class 
  void testUpdateNeighbors(Tester t) {
    this.initConditions();

    this.cellGold.setBottom(this.cellLightGreen);
    this.cellGold.setTop(this.cellLightRed);
    this.cellGold.setRight(this.cellLightGreen);
    this.cellGold.setLeft(this.cellLightBlue);

    this.cellGold.updateNeighbors(LIGHT_GREEN);

    // now the gold cell is a light green cell
    t.checkExpect(this.cellGold.color, LIGHT_GREEN);
    t.checkExpect(this.cellGold.top, this.cellLightRed);
    t.checkExpect(this.cellGold.bottom, this.cellLightGreen);
    t.checkExpect(this.cellGold.right, this.cellLightGreen);
    t.checkExpect(this.cellGold.left, this.cellLightBlue);

  }

  // tests for the isNextToFlooded method in the Cell class
  void testIsNextToFlooded(Tester t) {
    this.initConditions();
    // creating the board with the initial state of
    // each game board (only the uppermost left cell is flooded)
    ArrayList<ArrayList<Cell>> gameArrBoard2x2 = this.gameWorld2x2.createArrBoard();
    ArrayList<ArrayList<Cell>> gameArrBoard10x10 = this.gameWorld10x10.createArrBoard();

    // setting some flooded cells
    gameArrBoard10x10.get(0).get(1).flooded = true;
    gameArrBoard10x10.get(1).get(0).flooded = true;
    gameArrBoard10x10.get(1).get(1).flooded = true;

    // checking the cell recognizes a flooded cell next to it
    t.checkExpect(gameArrBoard2x2.get(0).get(1).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard2x2.get(1).get(0).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard2x2.get(1).get(1).isNextToFlooded(), false);

    t.checkExpect(gameArrBoard10x10.get(0).get(1).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard10x10.get(1).get(0).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard10x10.get(1).get(1).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard10x10.get(1).get(2).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard10x10.get(2).get(1).isNextToFlooded(), true);
    t.checkExpect(gameArrBoard10x10.get(2).get(2).isNextToFlooded(), false);
    t.checkExpect(gameArrBoard10x10.get(2).get(3).isNextToFlooded(), false);
    t.checkExpect(gameArrBoard10x10.get(3).get(3).isNextToFlooded(), false);

  }

  // tests for the updateColorTheme method in the cell class
  void testUpdateColorTheme(Tester t) {
    this.initConditions();
    // checking before and after that the color themes have adequately changed

    t.checkExpect(this.gameWorld2x2.colorTheme, lightColors);
    this.gameWorld2x2.updateColorTheme(darkColors);
    t.checkExpect(this.gameWorld2x2.colorTheme, darkColors);
    this.gameWorld2x2.updateColorTheme(lightColors);
    t.checkExpect(this.gameWorld2x2.colorTheme, lightColors);

    t.checkExpect(this.gameWorld10x10.colorTheme, lightColors);
    this.gameWorld10x10.updateColorTheme(darkColors);
    t.checkExpect(this.gameWorld10x10.colorTheme, darkColors);
    this.gameWorld10x10.updateColorTheme(lightColors);
    t.checkExpect(this.gameWorld10x10.colorTheme, lightColors);

    t.checkExpect(this.gameWorld26x26.colorTheme, lightColors);
    this.gameWorld26x26.updateColorTheme(darkColors);
    t.checkExpect(this.gameWorld26x26.colorTheme, darkColors);
    this.gameWorld26x26.updateColorTheme(lightColors);
    t.checkExpect(this.gameWorld26x26.colorTheme, lightColors);

  }

  // tests for the reset method in the FloodItWorld class
  void testReset(Tester t) {
    this.initConditions();

    // modifying values different from the original
    this.gameWorld10x10.numOfStep = 10;
    this.gameWorld10x10.updateColorTheme(darkColors);
    this.gameWorld10x10.board.get(2).flooded = true;

    // resetting the game world back to its initial state
    this.gameWorld10x10.reset();

    // changes should be back to the original
    t.checkExpect(this.gameWorld10x10.numOfStep, 0);
    t.checkExpect(this.gameWorld10x10.colorTheme, darkColors); // themes don't change on reset
    t.checkExpect(this.gameWorld10x10.board.get(2).flooded, false);

    // resetting a game that is already in its initial state
    // (no changes were made as of yet)
    this.gameWorld2x2.reset();

    // changes should remain unchanged after the reset
    t.checkExpect(this.gameWorld2x2.numOfStep, 0);
    t.checkExpect(this.gameWorld2x2.colorTheme, lightColors);
    t.checkExpect(this.gameWorld2x2.board.get(1).flooded, false);
  }

  // tests for the floodFill method in the FloodItWorld class 
  void testFloodFill(Tester t) {
    this.initConditions();

    FloodItWorld gameWorld2x2Tester = new FloodItWorld(2, 3, new Random(1));
    t.checkExpect(gameWorld2x2Tester.board.get(0).flooded, true);
    t.checkExpect(gameWorld2x2Tester.board.get(0).color, gameWorld2x2Tester.curColor);
    t.checkExpect(gameWorld2x2Tester.board.get(1).flooded, false);
    gameWorld2x2Tester.floodFill();
    t.checkExpect(gameWorld2x2Tester.board.get(1).flooded, true);
    t.checkExpect(gameWorld2x2Tester.board.get(2).flooded, true);

  }

  // tests for the gameWin method in the FloodItWorld class
  void testGameWin(Tester t) {
    this.initConditions();

    t.checkExpect(this.gameWorld10x10.gameWin(), false);
    // sets all the cells in the board to the same color
    for (Cell c : this.gameWorld10x10.board) {
      c.color = this.gameWorld10x10.curColor;
    }
    t.checkExpect(this.gameWorld10x10.gameWin(), true);

    t.checkExpect(this.gameWorld26x26.gameWin(), true);
    // sets all the cells in the board to the same color
    for (Cell c : this.gameWorld26x26.board) {
      c.color = this.gameWorld26x26.curColor;
    }
    // number of steps is greater than the number allowed
    this.gameWorld26x26.numOfStep = 100;
    t.checkExpect(gameWorld26x26.gameWin(), false);

  }

  // tests for the gameLose method in the FloodItWorld class
  void testGameLose(Tester t) {
    this.initConditions();

    t.checkExpect(this.gameWorld2x2.gameLose(), false);
    this.gameWorld2x2.board.get(0).color = LIGHT_BLUE;
    this.gameWorld2x2.board.get(1).color = LIGHT_RED;
    t.checkExpect(this.gameWorld2x2.gameLose(), false);
    this.gameWorld2x2.numOfStep = 100;
    t.checkExpect(this.gameWorld2x2.gameLose(), true);

    t.checkExpect(this.gameWorld10x10.gameLose(), false);
    this.gameWorld10x10.board.get(0).color = LIGHT_BLUE;
    this.gameWorld10x10.board.get(1).color = PEACH;
    t.checkExpect(this.gameWorld10x10.gameLose(), false);
    this.gameWorld10x10.numOfStep = 100;
    t.checkExpect(this.gameWorld10x10.gameLose(), true);

    t.checkExpect(this.gameWorld26x26.gameLose(), false);
    this.gameWorld26x26.board.get(0).color = LIGHT_BROWN;
    this.gameWorld26x26.board.get(1).color = GREY;
    t.checkExpect(this.gameWorld26x26.gameLose(), false);
    this.gameWorld26x26.numOfStep = 100;
    t.checkExpect(this.gameWorld26x26.gameLose(), true);

  }

  // tests for the allFlooded method in the FloodItWorld class
  void testAllFlooded(Tester t) {
    this.initConditions();

    t.checkExpect(this.gameWorld2x2.allFlooded(), true);
    for (Cell c : this.gameWorld2x2.board) {
      // all the cells have the same color, which
      // implies that every cell has been flooded
      c.color = this.gameWorld2x2.curColor;
    }
    t.checkExpect(this.gameWorld2x2.allFlooded(), true);

    t.checkExpect(this.gameWorld14x14.allFlooded(), false);

    for (Cell c : this.gameWorld26x26.board) {
      // all the cells have the same color, which
      // implies that every cell has been flooded
      c.color = this.gameWorld26x26.curColor;
    }
    for (Cell c : this.gameWorld26x26.board) {
      // all the cells have the same color, which
      // implies that every cell has been flooded
      c.flooded = true;
    }
    t.checkExpect(this.gameWorld26x26.allFlooded(), true);

  }

  // tests for the onMousePressed method in the FloodItWorld class
  void testOnMousePressed(Tester t) {
    this.initConditions();

    FloodItWorld gameWorld4x4Tester = new FloodItWorld(4, 5, new Random(1));
    t.checkExpect(gameWorld4x4Tester.board.get(0).flooded, true);
    t.checkExpect(gameWorld4x4Tester.board.get(0).color, gameWorld4x4Tester.curColor);
    t.checkExpect(gameWorld4x4Tester.board.get(1).flooded, false);

    // user clicks on the cell to the right of the upper
    // leftmost cell (the second cell of the board)
    gameWorld4x4Tester.onMousePressed(new Posn(8, 0));

    // the first and second cell of the board must be
    // the same color as the current color
    t.checkExpect(gameWorld4x4Tester.curColor, gameWorld4x4Tester.board.get(0).color);
    t.checkExpect(gameWorld4x4Tester.board.get(0).color, gameWorld4x4Tester.board.get(1).color);

    // both the first and second cell should now be flooded
    t.checkExpect(gameWorld4x4Tester.board.get(0).flooded, true);
    t.checkExpect(gameWorld4x4Tester.board.get(1).flooded, true);

    // --------------------//

    FloodItWorld gameWorld10x10Tester = new FloodItWorld(10, 6, new Random(1));
    t.checkExpect(gameWorld10x10Tester.board.get(0).flooded, true);
    t.checkExpect(gameWorld10x10Tester.board.get(0).color, gameWorld10x10Tester.curColor);
    t.checkExpect(gameWorld10x10Tester.board.get(1).flooded, false);

    // user clicks on the cell to the right of the upper
    // leftmost cell (the second cell of the board)
    gameWorld10x10Tester.onMousePressed(new Posn(8, 0));

    // the first and second cell of the board must be
    // the same color as the current color
    t.checkExpect(gameWorld10x10Tester.curColor, gameWorld10x10Tester.board.get(0).color);
    t.checkExpect(gameWorld10x10Tester.board.get(0).color, gameWorld10x10Tester.board.get(1).color);

    // both the first and second cell should now be flooded
    t.checkExpect(gameWorld10x10Tester.board.get(0).flooded, true);
    t.checkExpect(gameWorld10x10Tester.board.get(1).flooded, true);

  }

  // tests running the game with specific game boards
  // (change what game board you play by modifying the gameWorld object below)
  void testRunGame(Tester t) {
    this.initConditions();

    gameWorld10x10.bigBang(1100, 800, 0.005);
    /*
     * NOTE TO TA: uncomment the following tests for the rest of the game boards
     * of
     * big-bang
     */
    // gameWorld2x2.bigBang(1100, 800, 0.005);
    // gameWorld6x6.bigBang(1100, 800, 0.005);
    // gameWorld14x14.bigBang(1100, 800, 0.005);
    // gameWorld18x18.bigBang(1100, 800, 0.005);
    // gameWorld22x22.bigBang(1100, 800, 0.005);
    // gameWorld26x26.bigBang(1100, 800, 0.005);
  }
}
