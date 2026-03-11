/* ============================================
   MAZE — Game Logic
   Ported from Java (CS2510, Jorge Barron & Tri Watanasuparp)

   Algorithm:
     - Kruskal's MST to generate the maze
     - Union-Find with path compression
     - BFS (breadth-first) and DFS (depth-first) solvers
   ============================================ */

let MAZE_W = 25;   // columns
let MAZE_H = 18;   // rows

let grid      = [];  // grid[row][col] → Vertex
let player    = null;
let visitedSet = new Set();
let solvePath  = [];
let stepCount  = 0;
let gameWon    = false;

/* ─── Vertex ────────────────────────────────── */
class Vertex {
  constructor(col, row) {
    this.col = col;
    this.row = row;
    // Walls: present by default, removed by Kruskal's
    this.hasRightWall  = true;
    this.hasBottomWall = true;
  }
}

/* ─── Union-Find ────────────────────────────── */
const parent = new Map();

function find(v) {
  if (parent.get(v) === v) return v;
  const root = find(parent.get(v));
  parent.set(v, root); // path compression
  return root;
}

function union(a, b) {
  parent.set(find(a), find(b));
}

/* ─── Maze generation (Kruskal's) ───────────── */
function buildMaze() {
  // Build vertex grid
  grid = [];
  for (let r = 0; r < MAZE_H; r++) {
    grid[r] = [];
    for (let c = 0; c < MAZE_W; c++) {
      grid[r][c] = new Vertex(c, r);
    }
  }

  // Create all candidate edges with random weights
  const edges = [];
  for (let r = 0; r < MAZE_H; r++) {
    for (let c = 0; c < MAZE_W; c++) {
      if (c < MAZE_W - 1) edges.push({ from: grid[r][c], to: grid[r][c + 1], w: Math.random() });
      if (r < MAZE_H - 1) edges.push({ from: grid[r][c], to: grid[r + 1][c], w: Math.random() });
    }
  }
  edges.sort((a, b) => a.w - b.w);

  // Init union-find: every vertex is its own representative
  parent.clear();
  for (let r = 0; r < MAZE_H; r++)
    for (let c = 0; c < MAZE_W; c++)
      parent.set(grid[r][c], grid[r][c]);

  // Kruskal's: add edge if it joins two separate components
  for (const e of edges) {
    if (find(e.from) !== find(e.to)) {
      union(e.from, e.to);
      // Remove the wall between from and to
      if (e.from.row === e.to.row) {
        // Horizontal edge → remove right wall of the left vertex
        e.from.hasRightWall = false;
      } else {
        // Vertical edge → remove bottom wall of the top vertex
        e.from.hasBottomWall = false;
      }
    }
  }
}

/* ─── Neighbour lookup (uses wall state) ─────── */
function getNeighbors(v) {
  const ns = [];
  const { row, col } = v;
  if (col < MAZE_W - 1 && !v.hasRightWall)                   ns.push(grid[row][col + 1]); // right
  if (col > 0          && !grid[row][col - 1].hasRightWall)  ns.push(grid[row][col - 1]); // left
  if (row < MAZE_H - 1 && !v.hasBottomWall)                  ns.push(grid[row + 1][col]); // down
  if (row > 0          && !grid[row - 1][col].hasBottomWall) ns.push(grid[row - 1][col]); // up
  return ns;
}

/* ─── BFS / DFS solvers ─────────────────────── */
function solve(useDFS) {
  const start = grid[0][0];
  const end   = grid[MAZE_H - 1][MAZE_W - 1];
  const worklist  = [start];
  const explored  = new Set([start]);
  const cameFrom  = new Map();

  while (worklist.length > 0) {
    const cur = useDFS ? worklist.pop() : worklist.shift();
    if (cur === end) {
      // Reconstruct path from end back to start
      const path = [];
      let node = end;
      while (node) { path.push(node); node = cameFrom.get(node); }
      return path.reverse();
    }
    for (const n of getNeighbors(cur)) {
      if (!explored.has(n)) {
        explored.add(n);
        cameFrom.set(n, cur);
        worklist.push(n);
      }
    }
  }
  return [];
}

/* ─── Player ────────────────────────────────── */
function initPlayer() {
  player     = grid[0][0];
  visitedSet = new Set([player]);
  solvePath  = [];
  stepCount  = 0;
  gameWon    = false;
}

function movePlayer(key) {
  if (gameWon) return;
  let dr = 0, dc = 0;
  if      (key === 'ArrowRight') dc =  1;
  else if (key === 'ArrowLeft')  dc = -1;
  else if (key === 'ArrowDown')  dr =  1;
  else if (key === 'ArrowUp')    dr = -1;
  else return;

  const nr = player.row + dr;
  const nc = player.col + dc;
  if (nr < 0 || nr >= MAZE_H || nc < 0 || nc >= MAZE_W) return;

  // Check that no wall blocks this move
  let passable = false;
  if (dc ===  1) passable = !player.hasRightWall;
  if (dc === -1) passable = !grid[player.row][player.col - 1].hasRightWall;
  if (dr ===  1) passable = !player.hasBottomWall;
  if (dr === -1) passable = !grid[player.row - 1][player.col].hasBottomWall;

  if (!passable) return;

  player = grid[nr][nc];
  visitedSet.add(player);
  stepCount++;
  solvePath = []; // clear any displayed solution on manual move
  if (player.row === MAZE_H - 1 && player.col === MAZE_W - 1) gameWon = true;
}

/* ─── Reset ─────────────────────────────────── */
function resetMaze() {
  buildMaze();
  initPlayer();
}
