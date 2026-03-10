import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

import javalib.worldcanvas.WorldCanvas;
import javalib.worldimages.LineImage;
import javalib.worldimages.OutlineMode;
import javalib.worldimages.Posn;
import javalib.worldimages.RectangleImage;
import tester.Tester;

// examples and tests for the MazeWorld class 
// and other relevant classes of utility 
class ExamplesMazeWorld {
  ExamplesMazeWorld() {
  }

  MazeWorld mazeWorld4x4;
  MazeWorld mazeWorld10x10;
  MazeWorld mazeWorld20x60;
  MazeWorld mazeWorld50x50;
  MazeWorld mazeWorld100x60;

  // examples of positions
  Posn zeroZero;
  Posn oneZero;
  Posn zeroOne;
  Posn oneOne;

  // examples of vertices
  Vertex v1;
  Vertex v2;
  Vertex v3;

  // examples of edges
  Edge e10;
  Edge e20;
  Edge e30;
  Edge e40;
  ArrayList<Edge> mtedges;
  ArrayList<Edge> unsorted40;
  ArrayList<Edge> sorted40;

  WorldCanvas c1000;

  // setups the initial conditions for the maze world
  void initConditions() {
    this.mazeWorld4x4 = new MazeWorld(4, 4);
    this.mazeWorld10x10 = new MazeWorld(10, 10);
    this.mazeWorld20x60 = new MazeWorld(20, 60);
    this.mazeWorld50x50 = new MazeWorld(50, 50);
    this.mazeWorld100x60 = new MazeWorld(100, 60);

    this.zeroZero = new Posn(0, 0);
    this.oneZero = new Posn(1, 0);
    this.zeroOne = new Posn(0, 1);
    this.oneOne = new Posn(1, 1);

    this.v1 = new Vertex(zeroZero, sorted40);
    this.v2 = new Vertex(oneZero, sorted40);
    this.v3 = new Vertex(zeroOne, sorted40);

    this.e10 = new Edge(this.v1, this.v2, 10);
    this.e20 = new Edge(this.v2, this.v3, 20);
    this.e30 = new Edge(this.v3, this.v2, 30);
    this.e40 = new Edge(this.v2, this.v1, 40);

    this.mtedges = new ArrayList<Edge>();
    this.sorted40 = new ArrayList<Edge>(Arrays.asList(this.e10, this.e20, this.e30, this.e40));
    this.unsorted40 = new ArrayList<Edge>(Arrays.asList(this.e40, this.e10, this.e30, this.e20));

    c1000 = new WorldCanvas(1000, 1000);

  }

  // tests for the drawRightEdge method in the Vertex class
  void testDrawRightEdge(Tester t) {
    this.initConditions();

    t.checkExpect(this.v1.drawRightEdge(20),
        new LineImage(new Posn(0, 20), Color.BLACK).movePinhole(20 * -1, 20 * -0.5));
    t.checkExpect(this.v2.drawRightEdge(100),
        new LineImage(new Posn(0, 100), Color.BLACK).movePinhole(100 * -1, 100 * -0.5));
    t.checkExpect(this.v3.drawRightEdge(10),
        new LineImage(new Posn(0, 10), Color.BLACK).movePinhole(10 * -1, 10 * -0.5));
    t.checkExpect(this.v1.drawRightEdge(5),
        new LineImage(new Posn(0, 5), Color.BLACK).movePinhole(5 * -1, 5 * -0.5));
  }

  // tests for the drawBottomEdge method in the Vertex class
  void testDrawBottomEdge(Tester t) {
    this.initConditions();

    t.checkExpect(this.v1.drawBottomEdge(80),
        new LineImage(new Posn(80, 0), Color.BLACK).movePinhole(80 * -0.5, 80 * -1));
    t.checkExpect(this.v2.drawBottomEdge(22),
        new LineImage(new Posn(22, 0), Color.BLACK).movePinhole(22 * -0.5, 22 * -1));
    t.checkExpect(this.v3.drawBottomEdge(1),
        new LineImage(new Posn(1, 0), Color.BLACK).movePinhole(1 * -0.5, 1 * -1));
    t.checkExpect(this.v3.drawBottomEdge(9000),
        new LineImage(new Posn(9000, 0), Color.BLACK).movePinhole(9000 * -0.5, 9000 * -1));
  }

  // tests for the drawCell method in the Vertex class
  void testDrawCell(Tester t) {
    this.initConditions();

    t.checkExpect(this.v1.drawCell(1, Color.DARK_GRAY),
        new RectangleImage(1 - 4, 1 - 4, OutlineMode.SOLID, Color.DARK_GRAY).movePinhole(1 / -2,
            1 / -2));
    t.checkExpect(this.v2.drawCell(100, Color.CYAN),
        new RectangleImage(100 - 4, 100 - 4, OutlineMode.SOLID, Color.CYAN).movePinhole(100 / -2,
            100 / -2));
    t.checkExpect(this.v2.drawCell(30, Color.DARK_GRAY),
        new RectangleImage(30 - 4, 30 - 4, OutlineMode.SOLID, Color.DARK_GRAY).movePinhole(30 / -2,
            30 / -2));
    t.checkExpect(this.v3.drawCell(99, Color.RED),
        new RectangleImage(99 - 4, 99 - 4, OutlineMode.SOLID, Color.RED).movePinhole(99 / -2,
            99 / -2));
  }

  // tests for the compare method in the WeightComparator class
  void testCompare(Tester t) {
    this.initConditions();
    t.checkExpect(new WeightComparator().compare(e10, e20), -10);
    t.checkExpect(new WeightComparator().compare(e30, e20), 10);
    t.checkExpect(new WeightComparator().compare(e40, e10), 30);
    t.checkExpect(new WeightComparator().compare(e10, e10), 0);
  }

  // tests for the updateCellSize method in the MazeWorld class
  void testUpdateCellSize(Tester t) {
    this.initConditions();

    this.mazeWorld4x4.updateCellSize();
    t.checkExpect(this.mazeWorld4x4.cellSize, 30);

    this.mazeWorld10x10.updateCellSize();
    t.checkExpect(this.mazeWorld10x10.cellSize, 30);

    this.mazeWorld20x60.updateCellSize();
    t.checkExpect(this.mazeWorld20x60.cellSize, 12);

    this.mazeWorld50x50.updateCellSize();
    t.checkExpect(this.mazeWorld50x50.cellSize, 12);

    this.mazeWorld100x60.updateCellSize();
    t.checkExpect(this.mazeWorld100x60.cellSize, 12);

  }

  // NOTE to TA: please uncomment the code below to see our tests for makeScene:

  /*
   * 
   * // test for the makeScene method for the 4x4 maze // in the class MazeWorld
   * boolean testMakeScene4x4(Tester t) { this.initConditions();
   * 
   * int canvasWidth = this.mazeWorld4x4.mazeWidth * this.mazeWorld4x4.cellSize;
   * int canvasHeight = this.mazeWorld4x4.mazeHeight * this.mazeWorld4x4.cellSize;
   * 
   * WorldCanvas mainCanvas = new WorldCanvas(canvasWidth, canvasHeight);
   * 
   * return mainCanvas.drawScene(this.mazeWorld4x4.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * // test for the makeScene method for the 10x10 maze // in the class MazeWorld
   * boolean testMakeScene10x10(Tester t) { this.initConditions();
   * 
   * int canvasWidth = this.mazeWorld10x10.mazeWidth *
   * this.mazeWorld10x10.cellSize; int canvasHeight =
   * this.mazeWorld10x10.mazeHeight * this.mazeWorld10x10.cellSize;
   * 
   * WorldCanvas mainCanvas = new WorldCanvas(canvasWidth, canvasHeight);
   * 
   * return mainCanvas.drawScene(this.mazeWorld10x10.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * // test for the makeScene method for the 20x60 maze // in the class MazeWorld
   * boolean testMakeScene20x60(Tester t) { this.initConditions();
   * 
   * int canvasWidth = this.mazeWorld20x60.mazeWidth *
   * this.mazeWorld20x60.cellSize; int canvasHeight =
   * this.mazeWorld20x60.mazeHeight * this.mazeWorld20x60.cellSize;
   * 
   * WorldCanvas mainCanvas = new WorldCanvas(canvasWidth, canvasHeight);
   * 
   * return mainCanvas.drawScene(this.mazeWorld20x60.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * 
   * // test for the makeScene method for the 50x50 maze // in the class MazeWorld
   * boolean testMakeScene50x50(Tester t) { this.initConditions();
   * 
   * int canvasWidth = this.mazeWorld50x50.mazeWidth *
   * this.mazeWorld50x50.cellSize; int canvasHeight =
   * this.mazeWorld50x50.mazeHeight * this.mazeWorld50x50.cellSize;
   * 
   * WorldCanvas mainCanvas = new WorldCanvas(canvasWidth, canvasHeight);
   * 
   * return mainCanvas.drawScene(this.mazeWorld50x50.makeScene()) &&
   * mainCanvas.show(); }
   * 
   * // test for the makeScene method for the 100x60 maze // in the class
   * MazeWorld boolean testMakeScene100x60(Tester t) { this.initConditions();
   * 
   * int canvasWidth = this.mazeWorld100x60.mazeWidth *
   * this.mazeWorld100x60.cellSize; int canvasHeight =
   * this.mazeWorld100x60.mazeHeight * this.mazeWorld100x60.cellSize;
   * 
   * WorldCanvas mainCanvas = new WorldCanvas(canvasWidth, canvasHeight);
   * 
   * return mainCanvas.drawScene(this.mazeWorld100x60.makeScene()) &&
   * mainCanvas.show(); }
   * 
   */

  // tests for the constructMaze method in the MazeWorld class
  void testConstructMaze(Tester t) {
    MazeWorld exampleWorld2x2 = new MazeWorld(2, 2);
    MazeWorld exampleWorld3x2 = new MazeWorld(3, 2);

    t.checkExpect(exampleWorld2x2.grid,
        new ArrayList<ArrayList<Vertex>>(Arrays.asList(
            new ArrayList<Vertex>(Arrays.asList(exampleWorld2x2.grid.get(0).get(0),
                exampleWorld2x2.grid.get(0).get(1))),
            new ArrayList<Vertex>(Arrays.asList(exampleWorld2x2.grid.get(1).get(0),
                exampleWorld2x2.grid.get(1).get(1))))));

    t.checkExpect(exampleWorld3x2.grid,
        new ArrayList<ArrayList<Vertex>>(Arrays.asList(
            new ArrayList<Vertex>(Arrays.asList(exampleWorld3x2.grid.get(0).get(0),
                exampleWorld3x2.grid.get(0).get(1), exampleWorld3x2.grid.get(0).get(2))),
            new ArrayList<Vertex>(Arrays.asList(exampleWorld3x2.grid.get(1).get(0),
                exampleWorld3x2.grid.get(1).get(1), exampleWorld3x2.grid.get(1).get(2))))));

  }

  // tests for the method constructEdges in the class MazeWorld
  void testConstructEdges(Tester t) {
    this.initConditions();

    MazeWorld exampleWorld2x3 = new MazeWorld(2, 3);

    exampleWorld2x3.worklist = new ArrayList<Edge>(
        Arrays.asList(new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(1, 0)), 10),
            new Edge(new Vertex(new Posn(0, 0)), new Vertex(new Posn(0, 1)), 12),
            new Edge(new Vertex(new Posn(1, 0)), new Vertex(new Posn(1, 1)), 32),
            new Edge(new Vertex(new Posn(0, 1)), new Vertex(new Posn(1, 1)), 44),
            new Edge(new Vertex(new Posn(0, 1)), new Vertex(new Posn(0, 2)), 56),
            new Edge(new Vertex(new Posn(1, 1)), new Vertex(new Posn(1, 2)), 68),
            new Edge(new Vertex(new Posn(0, 2)), new Vertex(new Posn(1, 2)), 72)));

    t.checkExpect(exampleWorld2x3.worklist.get(0),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(0).get(0).index.x,
                exampleWorld2x3.grid.get(0).get(0).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(0).get(1).index.x,
                exampleWorld2x3.grid.get(0).get(1).index.y)),
            10));

    t.checkExpect(exampleWorld2x3.worklist.get(1),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(0).get(0).index.x,
                exampleWorld2x3.grid.get(0).get(0).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(0).index.x,
                exampleWorld2x3.grid.get(1).get(0).index.y)),
            12));

    t.checkExpect(exampleWorld2x3.worklist.get(2),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(0).get(1).index.x,
                exampleWorld2x3.grid.get(0).get(1).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(1).index.x,
                exampleWorld2x3.grid.get(1).get(1).index.y)),
            32));

    t.checkExpect(exampleWorld2x3.worklist.get(3),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(0).index.x,
                exampleWorld2x3.grid.get(1).get(0).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(1).index.x,
                exampleWorld2x3.grid.get(1).get(1).index.y)),
            44));

    t.checkExpect(exampleWorld2x3.worklist.get(4),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(0).index.x,
                exampleWorld2x3.grid.get(1).get(0).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(2).get(0).index.x,
                exampleWorld2x3.grid.get(2).get(0).index.y)),
            56));

    t.checkExpect(exampleWorld2x3.worklist.get(5),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(1).get(1).index.x,
                exampleWorld2x3.grid.get(1).get(1).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(2).get(1).index.x,
                exampleWorld2x3.grid.get(2).get(1).index.y)),
            68));

    t.checkExpect(exampleWorld2x3.worklist.get(6),
        new Edge(
            new Vertex(new Posn(exampleWorld2x3.grid.get(2).get(0).index.x,
                exampleWorld2x3.grid.get(2).get(0).index.y)),
            new Vertex(new Posn(exampleWorld2x3.grid.get(2).get(1).index.x,
                exampleWorld2x3.grid.get(2).get(1).index.y)),
            72));

  }

  // tests for the method sortEdges in the class MazeWorld
  void testSortEdges(Tester t) {
    this.initConditions();

    this.mazeWorld10x10.sortEdges(this.unsorted40);
    t.checkExpect(this.unsorted40, this.sorted40);

    this.mazeWorld20x60.sortEdges(this.sorted40);
    t.checkExpect(this.sorted40,
        new ArrayList<Edge>(Arrays.asList(this.e10, this.e20, this.e30, this.e40)));

    this.mazeWorld4x4.sortEdges(this.unsorted40);
    t.checkExpect(this.unsorted40, this.sorted40);
  }

  // tests for the assignOutEdges method in the MazeWorld class
  void testAssignOutEdges(Tester t) {
    this.initConditions();

    this.mazeWorld4x4.assignOutEdges();
    for (Edge e : this.mazeWorld4x4.edgesInTree) {
      t.checkExpect(e.from.outEdges.contains(e), true);
      t.checkExpect(e.to.outEdges.contains(e), false);
    }

    this.mazeWorld10x10.assignOutEdges();
    for (Edge e : this.mazeWorld10x10.edgesInTree) {
      t.checkExpect(e.from.outEdges.contains(e), true);
      t.checkExpect(e.to.outEdges.contains(e), false);
    }

  }

  // tests for the removeRightEdges method in the MazeWorld class
  void testRemoveRightEdges(Tester t) {
    this.initConditions();

    MazeWorld exampleWorld2x2 = new MazeWorld(2, 2);
    exampleWorld2x2.edgesInTree = new ArrayList<Edge>(
        Arrays.asList(this.e10, this.e20, this.e30, this.e40));

    exampleWorld2x2.removeRightEdges();
    t.checkExpect(this.e10.to.hasRightEdge, false);
    t.checkExpect(this.e10.from.hasRightEdge, false);

    t.checkExpect(this.e20.to.hasRightEdge, true);
    t.checkExpect(this.e20.from.hasRightEdge, false);

    t.checkExpect(this.e30.to.hasRightEdge, false);
    t.checkExpect(this.e30.from.hasRightEdge, true);

    t.checkExpect(this.e40.to.hasRightEdge, false);
    t.checkExpect(this.e40.from.hasRightEdge, false);

    MazeWorld exampleWorld3x2 = new MazeWorld(3, 2);
    exampleWorld3x2.edgesInTree = new ArrayList<Edge>(
        Arrays.asList(this.e10, this.e20, this.e20, this.e40, this.e30, this.e40));

    exampleWorld3x2.removeBottomEdges();
    t.checkExpect(this.e10.to.hasRightEdge, false);
    t.checkExpect(this.e10.from.hasRightEdge, false);

    t.checkExpect(this.e20.to.hasRightEdge, true);
    t.checkExpect(this.e20.from.hasRightEdge, false);

    t.checkExpect(this.e30.to.hasRightEdge, false);
    t.checkExpect(this.e30.from.hasRightEdge, true);

    t.checkExpect(this.e40.to.hasRightEdge, false);
    t.checkExpect(this.e40.from.hasRightEdge, false);

  }

  // tests for the removeBottomEdges method in the MazeWorld class
  void testRemoveBottomEdges(Tester t) {
    this.initConditions();

    MazeWorld exampleWorld2x2 = new MazeWorld(2, 2);
    exampleWorld2x2.edgesInTree = new ArrayList<Edge>(
        Arrays.asList(this.e10, this.e20, this.e30, this.e40));

    exampleWorld2x2.removeBottomEdges();
    t.checkExpect(this.e10.to.hasBottomEdge, true);
    t.checkExpect(this.e10.from.hasBottomEdge, true);

    t.checkExpect(this.e20.to.hasBottomEdge, true);
    t.checkExpect(this.e20.from.hasBottomEdge, true);

    t.checkExpect(this.e30.to.hasBottomEdge, true);
    t.checkExpect(this.e30.from.hasBottomEdge, true);

    t.checkExpect(this.e40.to.hasBottomEdge, true);
    t.checkExpect(this.e40.from.hasBottomEdge, true);

    MazeWorld exampleWorld3x2 = new MazeWorld(3, 2);
    exampleWorld3x2.edgesInTree = new ArrayList<Edge>(
        Arrays.asList(this.e10, this.e20, this.e20, this.e40, this.e30, this.e40));

    exampleWorld3x2.removeBottomEdges();
    t.checkExpect(this.e10.to.hasBottomEdge, true);
    t.checkExpect(this.e10.from.hasBottomEdge, true);

    t.checkExpect(this.e20.to.hasBottomEdge, true);
    t.checkExpect(this.e20.from.hasBottomEdge, true);

    t.checkExpect(this.e30.to.hasBottomEdge, true);
    t.checkExpect(this.e30.from.hasBottomEdge, true);

    t.checkExpect(this.e40.to.hasBottomEdge, true);
    t.checkExpect(this.e40.from.hasBottomEdge, true);

  }

  // tests for the createHashMap method in the MazeWorld class
  void testCreateHashMap(Tester t) {

    // createHashMap is invoked when the constructor is called:
    MazeWorld exampleWorld2x3 = new MazeWorld(2, 3);

    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(0).get(0),
        exampleWorld2x3.grid.get(0).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(0).get(1),
        exampleWorld2x3.grid.get(0).get(1));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(1).get(0),
        exampleWorld2x3.grid.get(1).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(1).get(1),
        exampleWorld2x3.grid.get(1).get(1));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(2).get(0),
        exampleWorld2x3.grid.get(2).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(2).get(1),
        exampleWorld2x3.grid.get(2).get(1));

    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(0).get(0)),
        exampleWorld2x3.grid.get(0).get(0));
    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(0).get(1)),
        exampleWorld2x3.grid.get(0).get(1));
    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(1).get(0)),
        exampleWorld2x3.grid.get(1).get(0));
    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(1).get(1)),
        exampleWorld2x3.grid.get(1).get(1));
    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(2).get(0)),
        exampleWorld2x3.grid.get(2).get(0));
    t.checkExpect(exampleWorld2x3.representatives.get(exampleWorld2x3.grid.get(2).get(1)),
        exampleWorld2x3.grid.get(2).get(1));

  }

  // tests for the findMinSpanTree method in the MazeWorld class
  void testFindMinSpanTree(Tester t) {
    this.initConditions();

    // findMinSpanTree is invoked when the constructor is called:
    MazeWorld exampleWorld2x2 = new MazeWorld(2, 2);

    exampleWorld2x2.edgesInTree = this.sorted40;

    t.checkExpect(exampleWorld2x2.edgesInTree.get(0), new Edge(
        exampleWorld2x2.edgesInTree.get(0).from, exampleWorld2x2.edgesInTree.get(0).to, 10));

    t.checkExpect(exampleWorld2x2.edgesInTree.get(1), new Edge(
        exampleWorld2x2.edgesInTree.get(1).from, exampleWorld2x2.edgesInTree.get(1).to, 20));

    t.checkExpect(exampleWorld2x2.edgesInTree.get(2), new Edge(
        exampleWorld2x2.edgesInTree.get(2).from, exampleWorld2x2.edgesInTree.get(2).to, 30));

    t.checkExpect(exampleWorld2x2.edgesInTree.get(3), new Edge(
        exampleWorld2x2.edgesInTree.get(3).from, exampleWorld2x2.edgesInTree.get(3).to, 40));

  }

  // tests for the union method in the MazeWorld class
  void testUnion(Tester t) {

    MazeWorld exampleWorld4x4 = new MazeWorld(2, 3);

    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(0).get(0),
        exampleWorld4x4.grid.get(0).get(0));
    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(0).get(1),
        exampleWorld4x4.grid.get(0).get(1));
    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(1).get(0),
        exampleWorld4x4.grid.get(1).get(0));

    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(1).get(1),
        exampleWorld4x4.grid.get(1).get(1));
    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(2).get(0),
        exampleWorld4x4.grid.get(2).get(0));

    exampleWorld4x4.representatives.put(exampleWorld4x4.grid.get(2).get(1),
        exampleWorld4x4.grid.get(2).get(1));

    // before union
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(0).get(0)),
        exampleWorld4x4.grid.get(0).get(0));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(0).get(1)),
        exampleWorld4x4.grid.get(0).get(1));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(1).get(0)),
        exampleWorld4x4.grid.get(1).get(0));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(1).get(1)),
        exampleWorld4x4.grid.get(1).get(1));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(2).get(0)),
        exampleWorld4x4.grid.get(2).get(0));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(2).get(1)),
        exampleWorld4x4.grid.get(2).get(1));

    // after union
    exampleWorld4x4.union(exampleWorld4x4.grid.get(0).get(0), exampleWorld4x4.grid.get(0).get(1));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(0).get(0)),
        exampleWorld4x4.grid.get(0).get(1));
    exampleWorld4x4.union(exampleWorld4x4.grid.get(0).get(1), exampleWorld4x4.grid.get(1).get(1));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(0).get(1)),
        exampleWorld4x4.grid.get(1).get(1));
    exampleWorld4x4.union(exampleWorld4x4.grid.get(2).get(0), exampleWorld4x4.grid.get(0).get(1));
    t.checkExpect(exampleWorld4x4.find(exampleWorld4x4.grid.get(2).get(0)),
        exampleWorld4x4.grid.get(1).get(1));

  }

  // tests for the find method in the MazeWorld class
  void testFind(Tester t) {

    MazeWorld exampleWorld2x3 = new MazeWorld(2, 3);

    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(0).get(0),
        exampleWorld2x3.grid.get(0).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(0).get(1),
        exampleWorld2x3.grid.get(0).get(1));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(1).get(0),
        exampleWorld2x3.grid.get(1).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(1).get(1),
        exampleWorld2x3.grid.get(1).get(1));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(2).get(0),
        exampleWorld2x3.grid.get(2).get(0));
    exampleWorld2x3.representatives.put(exampleWorld2x3.grid.get(2).get(1),
        exampleWorld2x3.grid.get(2).get(1));

    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(0).get(0)),
        exampleWorld2x3.grid.get(0).get(0));
    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(0).get(1)),
        exampleWorld2x3.grid.get(0).get(1));
    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(1).get(0)),
        exampleWorld2x3.grid.get(1).get(0));
    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(1).get(1)),
        exampleWorld2x3.grid.get(1).get(1));
    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(2).get(0)),
        exampleWorld2x3.grid.get(2).get(0));
    t.checkExpect(exampleWorld2x3.find(exampleWorld2x3.grid.get(2).get(1)),
        exampleWorld2x3.grid.get(2).get(1));

  }

  // tests the appearance of the maze visualized through big bang
  // (no user interactivity/modifying handlers yet)
  void testBigBang(Tester t) {
    this.initConditions();

    this.mazeWorld4x4.bigBang(this.mazeWorld4x4.mazeWidth * this.mazeWorld4x4.cellSize,
        this.mazeWorld4x4.mazeHeight * this.mazeWorld4x4.cellSize, 0.05);

    this.mazeWorld10x10.bigBang(this.mazeWorld10x10.mazeWidth * this.mazeWorld10x10.cellSize,
        this.mazeWorld10x10.mazeHeight * this.mazeWorld10x10.cellSize, 0.05);

    this.mazeWorld100x60.bigBang(this.mazeWorld100x60.mazeWidth * this.mazeWorld100x60.cellSize,
        this.mazeWorld100x60.mazeHeight * this.mazeWorld100x60.cellSize, 0.05);

    // NOTE to TA: please uncomment the following for the
    // rest of the big bang tests for all the game boards
    /*
     * this.mazeWorld50x50.bigBang(this.mazeWorld50x50.mazeWidth *
     * this.mazeWorld50x50.cellSize, this.mazeWorld50x50.mazeHeight *
     * this.mazeWorld50x50.cellSize, 0.05);
     * 
     * this.mazeWorld20x60.bigBang(this.mazeWorld20x60.mazeWidth *
     * this.mazeWorld20x60.cellSize, this.mazeWorld20x60.mazeHeight *
     * this.mazeWorld20x60.cellSize, 0.05);
     */

    // NOTE to TA: please uncomment the following for tests for
    // vertically/horizontally biased
    // mazes

    MazeWorld horizontalBiased10x10 = new MazeWorld(10, 10, "horizontal");
    horizontalBiased10x10.resetWorld();
    MazeWorld verticalBiased10x10 = new MazeWorld(10, 10, "vertical");
    verticalBiased10x10.resetWorld();

    /*
     * horizontalBiased10x10.bigBang(horizontalBiased10x10.mazeWidth *
     * horizontalBiased10x10.cellSize, horizontalBiased10x10.mazeHeight *
     * horizontalBiased10x10.cellSize, 0.05);
     * 
     * verticalBiased10x10.bigBang(verticalBiased10x10.mazeWidth *
     * verticalBiased10x10.cellSize, verticalBiased10x10.mazeHeight *
     * verticalBiased10x10.cellSize, 0.;05);
     */
  }

}