import java.awt.Color;
import javalib.worldimages.*;

// Represents a single square of the game area
class Cell implements IConstants {
  // In logical coordinates, with the origin at the top-left corner of the screen
  int x;
  int y;
  Color color;
  boolean flooded;
  // the four adjacent cells to this one
  Cell left;
  Cell top;
  Cell right;
  Cell bottom;

  Cell(int x, int y, Color color, boolean flooded) {
    this.x = x;
    this.y = y;
    this.color = color;
    this.flooded = flooded;

    this.left = this;
    this.top = this;
    this.right = this;
    this.bottom = this;
  }

  // draws the cell image (with a white star inside it for graphics enhancement)
  public WorldImage drawCell() {
    return new OverlayImage(new StarImage(CELL_SIZE / 3.5, 5, OutlineMode.SOLID, Color.WHITE),
        new RectangleImage(CELL_SIZE, CELL_SIZE, OutlineMode.SOLID, this.color));
  }

  // EFFECT:
  // sets the left cell of the current cell to be the given
  public void setLeft(Cell c) {
    this.left = c;

  }

  // EFFECT:
  // sets the top cell of the current cell to be the given
  public void setTop(Cell c) {
    this.top = c;
  }

  // EFFECT:
  // sets the right cell of the current cell to be the given
  public void setRight(Cell c) {
    this.right = c;
  }

  // EFFECT:
  // sets the bottom cell of the current cell to be the given
  public void setBottom(Cell c) {
    this.bottom = c;
  }

  // EFFECT:
  // updates the neighboring cells of this cell based on the given color
  public void updateNeighbors(Color curColor) {

    // the color of this cell becomes the color given
    this.color = curColor;

    if (this.left.color.equals(curColor) && !this.left.flooded) {
      this.left.flooded = true;
      this.left.updateNeighbors(curColor);
    }

    if (this.right.color.equals(curColor) && !this.right.flooded) {
      this.right.flooded = true;
      this.right.updateNeighbors(curColor);
    }

    if (this.top.color.equals(curColor) && !this.top.flooded) {
      this.top.flooded = true;
      this.top.updateNeighbors(curColor);
    }

    if (this.bottom.color.equals(curColor) && !this.bottom.flooded) {
      this.bottom.flooded = true;
      this.bottom.updateNeighbors(curColor);
    }
  }

  // is this cell next to any flooded cell?
  public boolean isNextToFlooded() {
    return this.top.flooded || this.left.flooded || this.right.flooded || this.bottom.flooded;
  }
}