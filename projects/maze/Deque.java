import tester.*;
import java.util.function.*;

// to represent a deque
class Deque<T> {
  Sentinel<T> header;

  Deque() {
    this.header = new Sentinel<T>();
  }

  // convenience constructor
  Deque(Sentinel<T> header) {
    this.header = header;
  }

  // does the deque contain this item?
  boolean contains(T data) {
    return this.header.contains(data);
  }

  // is the deque empty?
  boolean isEmpty() {
    return this.header.isEmpty();
  }

  // counts the number of nodes in this deque,
  // excluding the header node
  int size() {
    return this.header.size();
  }

  // EFFECT:
  // adds the given data to the front of this deque
  void addAtHead(T data) {
    this.header.addAtHead(data);
  }

  // EFFECT:
  // adds the given data to the tail of this deque
  void addAtTail(T data) {
    this.header.addAtTail(data);
  }

  // removes the first node from this Deque
  T removeFromHead() {
    return this.header.removeFromHead();
  }

  // removes the last node from this Deque
  T removeFromTail() {
    return this.header.removeFromTail();
  }

  // produces the first node in this Deque
  // that returns true for the given predicate
  ANode<T> find(Predicate<T> pred) {
    return this.header.find(pred);
  }

  // EFFECT:
  // removes the given node from this Deque
  void removeNode(ANode<T> given) {
    given.removeNodeHelper();
  }
}

// determines whether two strings are the same
class SameString implements Predicate<String> {
  String str;

  SameString(String str) {
    this.str = str;
  }

  public boolean test(String given) {
    return this.str.equals(given);
  }
}

// determines whether two Integers are the same
class SameInteger implements Predicate<Integer> {
  Integer num;

  SameInteger(Integer num) {
    this.num = num;
  }

  public boolean test(Integer given) {
    return this.num == given;
  }
}

// to represent a generic node 
abstract class ANode<T> {
  ANode<T> next;
  ANode<T> prev;

  // counts the nodes in the Deque
  abstract int sizeHelper();

  // removes the node from the Deque
  abstract T removeHelper();

  // removes the last node from the Deque
  abstract T removeTailHelper();

  // produces the first node in the Deque
  // that returns true for the given predicate
  abstract ANode<T> findHelper(Predicate<T> pred);

  // EFFECT:
  // removes the node from the Deque
  abstract void removeNodeHelper();

  // is the given data present in the node
  abstract boolean containsHelper(T data);

}

// to represent a sentinel node
class Sentinel<T> extends ANode<T> {

  Sentinel() {
    // initializes the next and prev fields of
    // the Sentinel to the Sentinel itself
    this.next = this;
    this.prev = this;
  }

  // does the sentinel contain this item?
  public boolean contains(T data) {
    return this.containsHelper(data);
  }

  // does the sentinel contain this item?
  public boolean containsHelper(T data) {
    return this.next.containsHelper(data);
  }

  // is the sentinel empty?
  public boolean isEmpty() {
    // if the next is a sentinel, then it is empty
    return this.next.equals(this);
  }

  // counts the nodes in the Deque, excluding this Sentinel
  public int size() {
    return this.next.sizeHelper();
  }

  // counts nodes excluding this Sentinel
  public int sizeHelper() {
    return 0;
  }

  // EFFECT:
  // adds the given data to the front of the Sentinel
  public void addAtHead(T data) {
    new Node<T>(data, this.next, this);
  }

  // EFFECT:
  // adds the given data to the tail of the Deque
  public void addAtTail(T data) {
    new Node<T>(data, this, this.prev);
  }

  // removes the first node from the Deque
  public T removeFromHead() {
    return this.next.removeHelper();
  }

  // cannot remove a node from a Sentinel
  public T removeHelper() {
    throw new RuntimeException("Can't try to remove on a Sentinel!");
  }

  // removes the last node from the Deque
  public T removeFromTail() {
    return this.prev.removeTailHelper();
  }

  // cannot remove a tail from a Sentinel
  public T removeTailHelper() {
    throw new RuntimeException("Can't try to remove on a Sentinel!");
  }

  // produces the first node in this Deque
  // that returns true for the given predicate
  public ANode<T> find(Predicate<T> pred) {
    return this.next.findHelper(pred);
  }

  // returns the header of this deque if the
  // predicate never returns true for any value (base case)
  public ANode<T> findHelper(Predicate<T> pred) {
    return this;
  }

  // EFFECT:
  // method does nothing if it is a Sentinel
  public void removeNodeHelper() {
    return;
  }
}

// to represent a node
class Node<T> extends ANode<T> {
  T data;

  Node(T data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }

  // convenience constructor
  Node(T data, ANode<T> next, ANode<T> prev) {
    this(data);
    this.next = next;
    this.prev = prev;

    // ensures that the given nodes are not null
    if (next == null || prev == null) {
      throw new IllegalArgumentException("Given node cannot be null");
    }
    else {
      // updates the given nodes to refer back to this node
      next.prev = this;
      prev.next = this;
    }
  }

  // does the node contain this item?
  public boolean containsHelper(T data) {
    return this.equals(data) || this.next.containsHelper(data);
  }

  // counts the nodes in the Deque
  public int sizeHelper() {
    return 1 + this.next.sizeHelper();
  }

  // removes the node from the Deque
  public T removeHelper() {
    prev.next = this.next;
    next.prev = this.prev;
    return this.data;
  }

  // removes the tail from the Deque
  public T removeTailHelper() {
    return this.removeHelper();
  }

  // produces the first node in the Deque
  // that returns true for the given predicate
  public ANode<T> findHelper(Predicate<T> pred) {
    if (pred.test(this.data)) { // if predicate matches,
      return this; // node was found
    }
    else {
      return this.next.findHelper(pred); // otherwise, recur on the rest of nodes to find match
    }
  }

  // EFFECT:
  // removes the node from the Deque
  public void removeNodeHelper() {
    this.removeHelper();
  }
}

// examples and tests for the Deque class as well as any other
// relevant classes (e.g: Node<T>, Sentinel<T>)
class ExamplesDeque {
  ExamplesDeque() {
  }

  String penguin;
  String fundies;
  String khoury;

  Integer six;
  Integer twenty;

  Sentinel<String> sentString;
  Sentinel<Integer> sentInteger;

  Node<String> nodeABC;
  Node<String> nodeBCD;
  Node<String> nodeCDE;
  Node<String> nodeDEF;

  Node<Integer> node1;
  Node<Integer> node2;
  Node<Integer> node3;
  Node<Integer> node4;
  Node<Integer> node5;

  Deque<String> deque1;
  Deque<String> deque2;
  Deque<Integer> deque3;

  // sets up the initial conditions for each test, redefining objects as needed
  void initData() {
    this.penguin = "penguin";
    this.fundies = "fundies";
    this.khoury = "khoury";

    this.six = 6;
    this.twenty = 20;

    // empty list of values
    this.sentString = new Sentinel<String>();
    this.sentInteger = new Sentinel<Integer>();

    // list of string values with abc, bcd, cde, def
    this.nodeABC = new Node<String>("abc", this.sentString, this.sentString);
    this.nodeBCD = new Node<String>("bcd", this.sentString, this.nodeABC);
    this.nodeCDE = new Node<String>("cde", this.sentString, this.nodeBCD);
    this.nodeDEF = new Node<String>("def", this.sentString, this.nodeCDE);

    // list of integers that are not in lexicographic order
    this.node1 = new Node<Integer>(1, this.sentInteger, this.sentInteger);
    this.node3 = new Node<Integer>(3, this.sentInteger, this.node1);
    this.node4 = new Node<Integer>(4, this.sentInteger, this.node3);
    this.node2 = new Node<Integer>(2, this.sentInteger, this.node4);
    this.node5 = new Node<Integer>(5, this.sentInteger, this.node2);

    this.deque1 = new Deque<String>();
    this.deque2 = new Deque<String>(this.sentString);
    this.deque3 = new Deque<Integer>(this.sentInteger);
  }

  // tests for the constructor exception in the class Node
  void checkConstructorException(Tester t) {
    t.checkConstructorException(new IllegalArgumentException("Given node cannot be null"), "Node",
        "'abc'", null, null);

    t.checkConstructorException(new IllegalArgumentException("Given node cannot be null"), "Node",
        "3", null, null);
  }

  // tests for the size method in the Deque class
  void testSize(Tester t) {
    this.initData();
    t.checkExpect(this.deque1.size(), 0);
    t.checkExpect(this.deque2.size(), 4);
    t.checkExpect(this.deque3.size(), 5);

    this.deque1.addAtHead("xyz");
    t.checkExpect(this.deque1.size(), 1);

    this.deque1.removeFromHead();
    t.checkExpect(this.deque1.size(), 0);

    this.deque3.addAtHead(10);
    t.checkExpect(this.deque3.size(), 6);

    this.deque3.removeFromHead();
    t.checkExpect(this.deque3.size(), 5);

    t.checkExpect(this.sentString.size(), 4);
    t.checkExpect(this.sentInteger.size(), 5);
  }

  // tests the addAtHead method in the class Deque
  void testAddAtHead(Tester t) {
    this.initData();

    this.deque1.addAtHead("def");
    this.deque1.addAtHead("cde");
    this.deque1.addAtHead("bcd");
    this.deque1.addAtHead("abc");
    this.deque3.addAtHead(this.six);

    t.checkExpect(this.deque1, this.deque2);

    this.deque2.addAtHead("fgh");

    t.checkExpect(this.deque2.size(), 5);
    t.checkExpect(this.deque2.removeFromHead(), "fgh");
    t.checkExpect(this.deque1.removeFromHead(), "abc");
    t.checkExpect(this.deque1.removeFromHead(), "bcd");
    t.checkExpect(this.deque1.removeFromHead(), "cde");
    t.checkExpect(this.deque1.removeFromHead(), "def");
    t.checkExpect(this.deque3.removeFromHead(), 6);

  }

  // tests the addAtTail method in the class Deque
  void testAddAtTail(Tester t) {
    this.initData();

    this.deque1.addAtTail("abc");
    this.deque1.addAtTail("bcd");
    this.deque1.addAtTail("cde");
    this.deque1.addAtTail("def");
    this.deque3.addAtTail(this.six);

    t.checkExpect(this.deque1, this.deque2);

    this.deque2.addAtTail("fgh");

    t.checkExpect(this.deque2.size(), 5);
    t.checkExpect(this.deque2.removeFromTail(), "fgh");
    t.checkExpect(this.deque1.removeFromTail(), "def");
    t.checkExpect(this.deque1.removeFromTail(), "cde");
    t.checkExpect(this.deque1.removeFromTail(), "bcd");
    t.checkExpect(this.deque1.removeFromTail(), "abc");
    t.checkExpect(this.deque3.removeFromTail(), 6);
  }

  // tests the removeFromHead method in the class Deque
  void testRemoveFromHead(Tester t) {

    Sentinel<String> sentString2 = new Sentinel<String>();
    Sentinel<Integer> sentInteger2 = new Sentinel<Integer>();

    t.checkException(new RuntimeException("Can't try to remove on a Sentinel!"), sentString2,
        "removeFromHead");
    t.checkException(new RuntimeException("Can't try to remove on a Sentinel!"), sentInteger2,
        "removeFromHead");

    this.initData();

    this.deque2.removeFromHead();
    t.checkExpect(this.sentString.next, this.nodeBCD);

    this.deque2.removeFromHead();
    t.checkExpect(this.sentString.next, this.nodeCDE);

    this.deque2.removeFromHead();
    t.checkExpect(this.sentString.next, this.nodeDEF);

    this.deque2.removeFromHead();
    t.checkExpect(this.sentString.next, this.sentString);

    t.checkExpect(this.deque2, this.deque1);

    this.deque3.addAtHead(100);
    t.checkExpect(this.deque3.removeFromHead(), 100);

  }

  // tests the removeFromTail method in the class Deque
  void testRemoveFromTail(Tester t) {

    Sentinel<String> sentString3 = new Sentinel<String>();
    Sentinel<Integer> sentInteger3 = new Sentinel<Integer>();

    t.checkException(new RuntimeException("Can't try to remove on a Sentinel!"), sentString3,
        "removeFromTail");
    t.checkException(new RuntimeException("Can't try to remove on a Sentinel!"), sentInteger3,
        "removeFromTail");

    this.initData();

    this.deque2.removeFromTail();
    t.checkExpect(this.sentString.prev, this.nodeCDE);

    this.deque2.removeFromTail();
    t.checkExpect(this.sentString.prev, this.nodeBCD);

    this.deque2.removeFromTail();
    t.checkExpect(this.sentString.prev, this.nodeABC);

    this.deque2.removeFromTail();
    t.checkExpect(this.sentString.prev, this.sentString);

    t.checkExpect(this.deque2, this.deque1);

    this.deque3.addAtTail(100);
    t.checkExpect(this.deque3.removeFromTail(), 100);
  }

  // tests for method find in the class Deque
  void testFind(Tester t) {
    this.initData();

    t.checkExpect(this.deque1.find(new SameString("fundies")), new Sentinel<String>());
    t.checkExpect(this.deque2.find(new SameString("abc")), this.nodeABC);
    t.checkExpect(this.deque2.find(new SameString("xyz")), this.sentString);
    t.checkExpect(this.deque3.find(new SameInteger(3)), this.node3);
    t.checkExpect(this.deque3.find(new SameInteger(7)), this.sentInteger);
  }

  // tests for the method removeNode in the class Deque
  void testRemoveNode(Tester t) {
    this.initData();

    this.deque2.removeNode(this.nodeDEF);
    this.deque2.removeNode(this.nodeBCD);
    this.deque2.removeNode(this.nodeCDE);
    this.deque2.removeNode(this.nodeABC);

    t.checkExpect(this.deque2.header, this.sentString);
    t.checkExpect(this.deque2, this.deque1);

    this.deque1.removeNode(this.nodeABC);
    t.checkExpect(this.deque1, this.deque1);

    this.deque3.removeNode(this.node5);
    this.deque3.removeNode(this.node3);
    this.deque3.removeNode(this.node2);
    this.deque3.removeNode(this.node1);
    this.deque3.removeNode(this.node4);

    t.checkExpect(this.deque3, new Deque<Integer>());
    t.checkExpect(this.deque3.header, this.sentInteger);

  }

  // tests for the method test in the classes SameInteger and SameString
  // (implementing predicates)
  void testPredicateTest(Tester t) {
    t.checkExpect(new SameString("abc").test("penguin"), false);
    t.checkExpect(new SameString("abc").test("abc"), true);

    t.checkExpect(new SameInteger(3).test(5), false);
    t.checkExpect(new SameInteger(3).test(3), true);
  }
}