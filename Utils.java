import java.util.ArrayList;
import java.util.Random;

// utility class to implement assorted range of useful methods
class Utils {
  // gets a random integer with the supplied maximum value
  int randMaker(int limit) {
    return new Random().nextInt(limit);
  }

  // gets a random integer by implementing a seed
  // (for testing purposes)
  int randMaker(int limit, int seed) {
    Random rand = new Random(seed);
    return rand.nextInt(limit);
  }
}

// to represent useful methods on arrays that are not built-in
class ArrayUtils {
  // gets a value from a random index (at the given limit) in the given array list
  <T> T getRandArrayVal(int randLimit, ArrayList<T> arr) {
    int randNum = new Utils().randMaker(randLimit);
    return arr.get(randNum);
  }

  // second version of getRandArrayVal with a random seed
  // (for testing purposes)
  <T> T getRandArrayVal(int randLimit, ArrayList<T> arr, int seed) {
    int randNum = new Utils().randMaker(randLimit, seed);
    return arr.get(randNum);
  }
}
