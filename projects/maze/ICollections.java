// to represent a mutable collection of items
interface ICollection<T> {
  // Is this collection empty?
  boolean isEmpty();

  // EFFECT: adds the item to the collection
  void add(T item);

  // Returns the first item of the collection
  // EFFECT: removes that first item
  T remove();
}

// to represent a stack
// (used for depth-first search)
class Stack<T> implements ICollection<T> {
  Deque<T> contents;

  Stack() {
    this.contents = new Deque<T>();
  }

  // is the stack empty?
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // removes the item at the head of the contents
  public T remove() {
    return this.contents.removeFromHead();
  }

  // adds the given item to the head of the contents
  // of this stack
  public void add(T item) {
    this.contents.addAtHead(item);
  }
}

// to represent a deque
// (used for breadth-first search)
class Queue<T> implements ICollection<T> {
  Deque<T> contents;

  Queue() {
    this.contents = new Deque<T>();
  }

  // is the queue empty?
  public boolean isEmpty() {
    return this.contents.isEmpty();
  }

  // removes the item at the head of the contents
  public T remove() {
    return this.contents.removeFromHead();
  }

  // adds the given item at the tail of the contents
  // of this queue
  public void add(T item) {
    this.contents.addAtTail(item);
  }
}