/*
 * |------------------------------------------------------|
 * | Assignment 10: Maze of twisty passages               |
 * | Jorge Barron and Tri Watanasuparp                    |
 * | CS2510 Fundies 2                                     |
 * |------------------------------------------------------|
 */

// NOTE: please read our documentation (in README.md) for further details on how to play the game.
// Thank you for a great semester!

import java.util.ArrayList;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;
import java.util.Random;
import java.util.HashMap;
import java.util.Comparator;
import java.util.Collections;

// to represent a player
class Player {
  Vertex position;
  ArrayList<Vertex> alreadyVisited; // all the vertices that the player already visited

  Player(Vertex position, ArrayList<Vertex> alreadyVisited) {
    this.position = position;
    this.alreadyVisited = alreadyVisited;
  }

  // draws the current player's cell
  WorldImage drawPlayer(int cellSize) {
    return new RectangleImage(cellSize - 2, cellSize - 2, OutlineMode.SOLID, Color.ORANGE)
        .movePinhole(cellSize / -2, cellSize / -2);
  }

  // draws the visited cell
  WorldImage drawVisitedCell(int cellSize) {
    return new RectangleImage(cellSize - 2, cellSize - 2, OutlineMode.SOLID, Color.RED)
        .movePinhole(cellSize / -2, cellSize / -2);
  }

  // draws all of the vertices the player has visited
  WorldScene drawPlayerVisited(WorldScene s, int cellSize) {
    for (Vertex v : this.alreadyVisited) {
      s.placeImageXY(this.drawVisitedCell(cellSize), v.index.x * cellSize, v.index.y * cellSize);
    }
    return s;
  }

}

// to represent a vertex
class Vertex {
  Posn index;
  ArrayList<Edge> outEdges;
  boolean isVisited;

  // NOTE: we only need right and bottom edge fields here since
  // thats how we represented the edges in the maze
  boolean hasRightEdge; // does this vertex have a right edge?
  boolean hasBottomEdge; // does this vertex have a bottom edge?

  ArrayList<Vertex> adj = new ArrayList<Vertex>(); // all adjacent vertices of this vertex

  Vertex(Posn index, ArrayList<Edge> outEdges) {
    this.index = index;
    this.outEdges = outEdges;
    this.hasRightEdge = true;
    this.hasBottomEdge = true;
    this.isVisited = false;
  }

  // convenience constructor where the vertex's
  // outer edges are empty
  Vertex(Posn index) {
    this.index = index;
    this.outEdges = new ArrayList<Edge>();
    this.hasRightEdge = true;
    this.hasBottomEdge = true;
  }

  // visualizes the right edge of this vertex with the given cell size
  WorldImage drawRightEdge(int cellSize) {
    return new LineImage(new Posn(0, cellSize), Color.BLACK).movePinhole(cellSize * -1,
        cellSize * -0.5);
  }

  // visualizes the bottom edge of this vertex with the given cell size
  WorldImage drawBottomEdge(int cellSize) {
    return new LineImage(new Posn(cellSize, 0), Color.BLACK).movePinhole(cellSize * -0.5,
        cellSize * -1);
  }

  // draws a square cell with the given color and with the size
  // tailored to the maze world
  WorldImage drawCell(int cellSize, Color col) {
    return new RectangleImage(cellSize - 4, cellSize - 4, OutlineMode.SOLID, col)
        .movePinhole(cellSize / -2, cellSize / -2);
  }

  // is the given object equal to this vertex?
  public boolean equals(Object other) {
    if (!(other instanceof Vertex)) {
      return false;
    }
    Vertex that = (Vertex) other;
    return this.index.x == that.index.x && this.index.y == that.index.y;
  }

  // produces a hash code for this vertex
  public int hashCode() {
    return this.index.x * this.index.y * 10000;
  }

}

// to represent an edge
class Edge {
  Vertex from;
  Vertex to;
  int weight;

  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

}

// compares the weights of two Edges
class WeightComparator implements Comparator<Edge> {
  public int compare(Edge e1, Edge e2) {
    return e1.weight - e2.weight;
  }
}

// to represent the maze world 
class MazeWorld extends World {
  ArrayList<ArrayList<Vertex>> grid; // all vertices laid out for the maze
  int mazeWidth; // the width of the gameboard in pixels
  int mazeHeight; // the height of the gameboard in pixels
  int cellSize; // sizes of the maze cells

  // union/find data structure:
  HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>();
  ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
  // all edges in graph, sorted by edge weights
  ArrayList<Edge> worklist;
  WorldScene scene = new WorldScene(0, 0);
  ArrayList<Vertex> path = new ArrayList<Vertex>();

  // color constants in the maze
  static final Color MAROON = new Color(210, 77, 87);
  static final Color FOREST_GREEN = new Color(123, 239, 178);

  // random variable for random weight assignments for edges
  // in the maze
  Random rand = new Random();

  Player player;

  int numOfStep;

  // NOTE: to try to make the maze less vertically/horizontally biased by default,
  // weights
  // for horizontal and vertical edges are assigned from a random value of 0-101
  int horizontalEdgeLimit = 101;
  int verticalEdgeLimit = 101;

  // sets up the MazeWorld according to the given mazeWidth and mazeHeight
  MazeWorld(int mazeWidth, int mazeHeight) {
    this.mazeWidth = mazeWidth;
    this.mazeHeight = mazeHeight;
    this.grid = this.constructMaze();
    this.worklist = this.constructEdges();
    this.edgesInTree = this.findMinSpanTree();
    this.cellSize = this.updateCellSize();
    this.player = new Player(this.grid.get(0).get(0), new ArrayList<Vertex>());
    this.numOfStep = 0;
  }

  // convenience constructor for an example of a
  // horizontally or vertically
  // biased maze based on the supplied type of bias identified
  // with a string: "vertical" or "horizontal"
  MazeWorld(int mazeWidth, int mazeHeight, String typeOfBias) {
    this.mazeWidth = mazeWidth;
    this.mazeHeight = mazeHeight;
    this.grid = this.constructMaze();
    this.worklist = this.constructEdges();
    this.edgesInTree = this.findMinSpanTree();
    this.cellSize = this.updateCellSize();
    this.player = new Player(this.grid.get(0).get(0), new ArrayList<Vertex>());
    this.numOfStep = 0;

    if (typeOfBias.equals("vertical")) {
      this.verticalEdgeLimit = 100;
      this.horizontalEdgeLimit = 20;
    }
    else if (typeOfBias.equals("horizontal")) {
      this.horizontalEdgeLimit = 100;
      this.verticalEdgeLimit = 20;
    }

  }

  // EFFECT:
  // updates the cell size of the board to scale according to the
  // maze's parameters
  public int updateCellSize() {
    if (this.mazeWidth >= 20 || this.mazeHeight >= 20) {
      return 12;
    }
    else {
      return 30;
    }
  }

  // creates the scene of the game, displaying the edges
  // of the vertices in the maze
  @Override
  public WorldScene makeScene() {

    this.removeRightEdges();
    this.removeBottomEdges();

    for (int i = 0; i < this.mazeHeight; i++) {
      for (int j = 0; j < this.mazeWidth; j++) {
        Vertex currVertex = grid.get(i).get(j);

        if (this.grid.get(i).get(j).hasRightEdge) {
          this.scene.placeImageXY(currVertex.drawRightEdge(this.cellSize), (this.cellSize * j),
              (this.cellSize * i));
        }

        if (this.grid.get(i).get(j).hasBottomEdge) {
          this.scene.placeImageXY(currVertex.drawBottomEdge(this.cellSize), (this.cellSize * j),
              (this.cellSize * i));
        }

        if (this.grid.get(i).get(j).isVisited) {
          this.scene.placeImageXY(currVertex.drawCell(this.cellSize, Color.BLACK),
              (this.cellSize * j), (this.cellSize * i));
        }
      }
    }

    // draws the starting point of the maze (in green)
    this.scene.placeImageXY(this.grid.get(0).get(0).drawCell(this.cellSize, FOREST_GREEN), 0, 0);

    // draws the ending point of the maze (in red)
    this.scene.placeImageXY(
        this.grid.get(this.mazeHeight - 1).get(this.mazeWidth - 1).drawCell(this.cellSize, MAROON),
        (this.mazeWidth - 1) * this.cellSize, (this.mazeHeight - 1) * this.cellSize);

    // draws the player
    this.scene.placeImageXY(this.player.drawPlayer(this.cellSize),
        (this.player.position.index.x * this.cellSize),
        (this.player.position.index.y * this.cellSize));

    this.drawPath(this.path);

    return this.scene;
  }

  // draws the given list of vertices on the board
  void drawPath(ArrayList<Vertex> path) {
    for (Vertex v : path) {
      this.scene.placeImageXY(v.drawCell(this.cellSize, Color.MAGENTA), v.index.x * this.cellSize,
          v.index.y * this.cellSize);
    }
  }

  // constructs all the vertices of the maze
  public ArrayList<ArrayList<Vertex>> constructMaze() {
    ArrayList<ArrayList<Vertex>> result = new ArrayList<ArrayList<Vertex>>();
    for (int col = 0; col < this.mazeHeight; col++) {
      ArrayList<Vertex> colVerticesList = new ArrayList<Vertex>();
      for (int row = 0; row < this.mazeWidth; row++) {
        Vertex v = new Vertex(new Posn(row, col));
        colVerticesList.add(v);
      }
      result.add(colVerticesList);
    }
    return result;
  }

  // constructs all the possible edges for each vertex of
  // the maze with a random weight
  public ArrayList<Edge> constructEdges() {
    ArrayList<Edge> result = new ArrayList<Edge>();
    for (int col = 0; col < this.grid.size(); col++) {
      for (int row = 0; row < this.grid.get(col).size(); row++) {

        Vertex currVertex = this.grid.get(col).get(row);
        if (col < this.mazeHeight - 1) {
          Vertex rightVertex = this.grid.get(col + 1).get(row);
          Edge edgeToRight = new Edge(currVertex, rightVertex,
              this.rand.nextInt(this.verticalEdgeLimit));
          result.add(edgeToRight);
        }

        if (row < this.mazeWidth - 1) {
          Vertex bottomVertex = this.grid.get(col).get(row + 1);
          Edge edgeToBottom = new Edge(currVertex, bottomVertex,
              this.rand.nextInt(this.horizontalEdgeLimit));
          result.add(edgeToBottom);
        }
      }
    }
    this.sortEdges(result);

    return result;
  }

  // EFFECT:
  // sorts all the edges in the given list of edges
  // by their weight, from lowest to greatest
  public void sortEdges(ArrayList<Edge> arrEdge) {
    Collections.sort(arrEdge, new WeightComparator());
  }

  // EFFECT:
  // establishes the connections between edges
  // in edgesInTree
  public void assignOutEdges() {
    ArrayList<Edge> edges = this.edgesInTree;
    for (Edge e : edges) {
      e.from.outEdges.add(e);
    }
  }

  // EFFECT:
  // removes the right edges from edgesInTree in this
  // maze based on whether or not they're connected to each other
  public void removeRightEdges() {
    for (Edge e : this.edgesInTree) {
      if (e.from.index.y == e.to.index.y) {
        e.from.hasRightEdge = false;
      }
    }
  }

  // EFFECT:
  // removes the bottom edges from edgesInTree in this
  // maze based on whether or not they're connected to each other
  public void removeBottomEdges() {
    for (Edge e : this.edgesInTree) {
      if (e.from.index.x == e.to.index.x) {
        e.from.hasBottomEdge = false;
      }
    }
  }

  // EFFECT:
  // maps every vertex in this grid in the maze world
  // to itself, making every vertex as its own representative element
  public void createHashMap() {
    for (int i = 0; i < this.grid.size(); i++) {
      for (int j = 0; j < this.grid.get(i).size(); j++) {
        this.representatives.put(this.grid.get(i).get(j), this.grid.get(i).get(j));
      }
    }
  }

  // creates a minimum spanning tree through the utilization of Kruskal's
  // algorithm
  public ArrayList<Edge> findMinSpanTree() {
    int i = 0;
    this.createHashMap();

    while (this.edgesInTree.size() < this.worklist.size() && i < this.worklist.size()) {
      Edge e = worklist.get(i);
      if (this.find(e.from).equals(this.find(e.to))) {
        // already connected; don't add to
        // minimum spanning tree
      }
      else {
        // records the current edge in edgesInTree
        // and creates a union between the to and from vertices
        this.edgesInTree.add(e);
        this.union(find(e.from), find(e.to));
      }
      i++;
    }
    this.assignOutEdges();
    return this.edgesInTree;

  }

  // EFFECT:
  // unions the two vertices
  public void union(Vertex v, Vertex other) {
    this.representatives.put(this.find(v), this.find(other));
  }

  // finds the representative of this vertex
  public Vertex find(Vertex v) {
    if (this.representatives.get(v).equals(v)) {
      return v;
    }
    else {
      return this.find(this.representatives.get(v));
    }
  }

  // finds the path from the starting point to the ending point
  // of the maze using breadth-first search (and queues)
  ArrayList<Vertex> bfs(Vertex from, Vertex to) {
    return searchHelp(from, to, new Queue<Vertex>());
  }

  // finds the path from the starting point to the ending point
  // of the maze using depth-first search (and stacks)
  ArrayList<Vertex> dfs(Vertex from, Vertex to) {
    return searchHelp(from, to, new Stack<Vertex>());
  }

  // determines a path from the supplied vertex to the second given vertex
  // using the given worklist
  public ArrayList<Vertex> searchHelp(Vertex from, Vertex to, ICollection<Vertex> worklist) {
    ArrayList<Vertex> exploredVertices = new ArrayList<Vertex>();

    // initialize the worklist to contain the starting node
    worklist.add(from);
    while (!worklist.isEmpty()) {
      Vertex next = worklist.remove();
      if (!exploredVertices.contains(next)) {
        exploredVertices.add(next);

        if (next.equals(to)) {
          return exploredVertices;
        }
        ArrayList<Edge> edges = next.outEdges;
        for (Edge e : edges) {
          Vertex edgeTo = e.to;
          worklist.add(edgeTo);
        }
      }
    }
    return exploredVertices;
  }

  // determines if the key pressed by the player is a valid move
  boolean validMove(String key) {

    try {
      return (key.equals("left") && !this.grid.get(this.player.position.index.y)
          .get(this.player.position.index.x - 1).hasRightEdge)
          || (key.equals("right") && !this.grid.get(this.player.position.index.y)
              .get(this.player.position.index.x).hasRightEdge)
          || (key.equals("up") && !this.grid.get(this.player.position.index.y - 1)
              .get(this.player.position.index.x).hasBottomEdge)
          || (key.equals("down") && !this.grid.get(this.player.position.index.y)
              .get(this.player.position.index.x).hasBottomEdge);
    }
    catch (Exception e) {
      // if an exception is thrown, do nothing
      // (works for out of bounds)
    }
    return false;
  }

  // EFFECT:
  // resets the maze to its original state
  public void resetWorld() {
    this.scene = this.getEmptyScene();
    this.grid.clear();
    this.worklist.clear();
    this.representatives.clear();
    this.edgesInTree.clear();
    this.path.clear();

    this.grid = this.constructMaze();
    this.worklist = this.constructEdges();
    this.edgesInTree = this.findMinSpanTree();
    this.cellSize = this.updateCellSize();
    this.player = new Player(this.grid.get(0).get(0), new ArrayList<Vertex>());
  }

  // EFFECT:
  // updates the state of the board based upon the key pressed
  public void onKeyEvent(String key) {
    if (key.equals("r")) {
      // extra credit feature that
      // resets the maze world with its initial setting
      this.resetWorld();
    }

    if (key.equals("left") && this.validMove("left")) {
      this.player.position.index = new Posn(this.player.position.index.x - 1,
          this.player.position.index.y);
      this.player.alreadyVisited.add(this.player.position);
      this.player.drawPlayerVisited(this.scene, this.cellSize);
      this.numOfStep++;
    }
    else if (key.equals("right") && this.validMove("right")) {
      this.player.position.index = new Posn(this.player.position.index.x + 1,
          this.player.position.index.y);
      this.player.alreadyVisited.add(this.player.position);
      this.player.drawPlayerVisited(this.scene, this.cellSize);
      this.numOfStep++;

    }
    else if (key.equals("up") && this.validMove("up")) {
      this.player.position.index = new Posn(this.player.position.index.x,
          this.player.position.index.y - 1);
      this.player.alreadyVisited.add(this.player.position);
      this.player.drawPlayerVisited(this.scene, this.cellSize);
      this.numOfStep++;
    }
    else if (key.equals("down") && this.validMove("down")) {
      this.player.position.index = new Posn(this.player.position.index.x,
          this.player.position.index.y + 1);
      this.player.alreadyVisited.add(this.player.position);
      this.player.drawPlayerVisited(this.scene, this.cellSize);
      this.numOfStep++;
    }
    // triggers the depth-first-search
    else if (key.equals("d")) {

      this.path = this.dfs(this.grid.get(0).get(0),
          this.grid.get(this.mazeHeight - 1).get(this.mazeHeight - 1));
    }
    // triggers the breadth-first-search
    else if (key.equals("b")) {

      this.path = this.bfs(this.grid.get(0).get(0),
          this.grid.get(this.mazeHeight - 1).get(this.mazeWidth - 1));
    }
  }

  // determines if the user has won the game
  // (we are making the game easy so that you can't lose :) )
  public boolean gameWin() {
    Vertex lastCell = this.grid.get(this.mazeHeight - 1).get(this.mazeWidth - 1);
    return this.player.position.equals(lastCell);
  }

  // produces the last scene of the game
  // (with a win message and your number of steps)
  @Override
  public WorldScene lastScene(String msg) {
    WorldImage textImage = new TextImage(msg, (int) Math.sqrt(this.mazeWidth * this.mazeHeight) * 2,
        Color.BLACK);
    WorldImage backgroundImage = new RectangleImage(msg.length() * this.cellSize,
        (int) Math.sqrt(this.mazeWidth * this.mazeHeight) * 2, OutlineMode.SOLID, Color.lightGray);
    WorldImage textOnBackground = new OverlayImage(textImage, backgroundImage);
    WorldScene s = this.makeScene();
    s.placeImageXY(textOnBackground, (this.mazeWidth * this.cellSize) / 2,
        (this.mazeHeight * this.cellSize) / 2);
    return s;
  }

  // determines the world end based on user interactivity
  public WorldEnd worldEnds() {
    if (this.gameWin()) {
      return new WorldEnd(true,
          this.lastScene("You won in " + String.valueOf(this.numOfStep) + " steps!"));
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}
