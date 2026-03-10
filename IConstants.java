import java.awt.Color;
import javalib.worldimages.*;

// to represent constants utilized throughout the classes for the game
interface IConstants {

  // size of the cells in the game board
  int CELL_SIZE = 30;

  // assorted colors used throughout the game

  // light-pastel colors theme
  Color GOLD = new Color(255, 204, 51);
  Color LIGHT_GREEN = new Color(152, 251, 152);
  Color LIGHT_BLUE = new Color(116, 189, 203);
  Color LIGHT_RED = new Color(255, 102, 102);
  Color LIGHT_BROWN = new Color(195, 155, 119);
  Color LIGHT_PURPLE = new Color(177, 156, 217);
  Color GREY = new Color(192, 192, 192);
  Color PEACH = new Color(255, 191, 171);

  // dark colors theme
  Color DARK_ORANGE = new Color(210, 105, 30);
  Color FOREST_GREEN = new Color(34, 139, 34);
  Color MIDNIGHT_BLUE = new Color(14, 77, 146);
  Color MAROON = new Color(138, 3, 3);
  Color DARK_BROWN = new Color(101, 67, 33);
  Color DARK_RED = new Color(194, 24, 7);
  Color BLACK = new Color(27, 27, 27);
  Color DARK_VIOLET = new Color(91, 10, 145);

  // assorted text images used in the game for graphics enhancement
  int TEXT_SIZE = 50;
  WorldImage TITLE_TEXT = new TextImage("FloodIt Game", 35, FontStyle.BOLD, MIDNIGHT_BLUE);
  WorldImage CREATORS_TEXT = new TextImage("Created by: Jorge and Tri", 20, MIDNIGHT_BLUE);
  WorldImage INSTRUCTIONS_TEXT = new TextImage("Click on the colored cells!", 20, Color.BLACK);
  WorldImage WINNING_INSTR_TEXT = new TextImage("Win by flooding the board with a single color.",
      14, Color.BLACK);
  WorldImage DARK_MODE_TEXT = new TextImage("Press 'D' to toggle on dark mode", 13, Color.BLACK);
  WorldImage LIGHT_MODE_TEXT = new TextImage("Press 'L' to toggle on light mode", 13, Color.BLACK);
  WorldImage RESET_TEXT = new TextImage("Press 'R' to reset game", 15, FontStyle.BOLD, MAROON);
  WorldImage WARNING_TEXT = new TextImage("Note: Toggling modes will reset the game.", 13,
      Color.BLACK);
  WorldImage LINE_SEPARATOR = new TextImage("-----------------------------------", 13, Color.BLACK);

}
