/* ============================================
   FLOOD IT — Game Logic
   Ported from Java (CS2510, Jorge Barron & Tri Watanasuparp)
   Step limit: (boardSize/2) * (numColors/2) + numColors
   ============================================ */

const BOARD_SIZE = 14;
const NUM_COLORS = 6;
const STEP_LIMIT = Math.floor(BOARD_SIZE / 2) * Math.floor(NUM_COLORS / 2) + NUM_COLORS;

const PALETTES = {
  light: ['#CBDF90', '#4D7C8A', '#8FAD88', '#D4845A', '#A07CBA', '#D4A835'],
  dark:  ['#D2691E', '#0E4D92', '#228B22', '#CC3333', '#7B2FBE', '#DAA520'],
};

let palette   = PALETTES.light;
let board     = [];   // flat array of Cell objects
let curColor  = '';
let steps     = 0;
let gameState = 'playing'; // 'playing' | 'win' | 'lose'
let startTime = 0;
let elapsed   = 0;
let timerHandle = null;

/* ─── Cell ──────────────────────────────────── */
class Cell {
  constructor(col, row, color) {
    this.col     = col;
    this.row     = row;
    this.color   = color;
    this.flooded = false;
    this.top    = null;
    this.bottom = null;
    this.left   = null;
    this.right  = null;
  }
}

/* ─── Board construction ────────────────────── */
function buildBoard() {
  const grid = [];
  for (let r = 0; r < BOARD_SIZE; r++) {
    grid[r] = [];
    for (let c = 0; c < BOARD_SIZE; c++) {
      const color = palette[Math.floor(Math.random() * NUM_COLORS)];
      grid[r][c] = new Cell(c, r, color);
    }
  }
  // Link neighbours
  for (let r = 0; r < BOARD_SIZE; r++) {
    for (let c = 0; c < BOARD_SIZE; c++) {
      const cell    = grid[r][c];
      cell.top    = r > 0              ? grid[r - 1][c] : null;
      cell.bottom = r < BOARD_SIZE - 1 ? grid[r + 1][c] : null;
      cell.left   = c > 0              ? grid[r][c - 1] : null;
      cell.right  = c < BOARD_SIZE - 1 ? grid[r][c + 1] : null;
    }
  }
  board = grid.flat();
  board[0].flooded = true;
  curColor = board[0].color;
}

/* ─── Flood fill (iterative BFS) ────────────── */
function floodFill(newColor) {
  if (newColor === curColor || gameState !== 'playing') return;
  steps++;
  curColor = newColor;

  // Recolour all currently flooded cells and seed the queue
  const queue = [];
  for (const cell of board) {
    if (cell.flooded) {
      cell.color = newColor;
      queue.push(cell);
    }
  }

  // BFS: expand flood to neighbours that already match the new colour
  while (queue.length > 0) {
    const cell = queue.shift();
    for (const n of [cell.top, cell.bottom, cell.left, cell.right]) {
      if (n && !n.flooded && n.color === newColor) {
        n.flooded = true;
        queue.push(n);
      }
    }
  }

  // Check end conditions (mirrors Java: allFlooded + step limit)
  if (board.every(c => c.flooded)) {
    gameState = 'win';
    clearInterval(timerHandle);
  } else if (steps >= STEP_LIMIT) {
    gameState = 'lose';
    clearInterval(timerHandle);
  }
}

/* ─── Reset ─────────────────────────────────── */
function resetGame() {
  clearInterval(timerHandle);
  steps     = 0;
  elapsed   = 0;
  gameState = 'playing';
  buildBoard();
  startTime = Date.now();
  timerHandle = setInterval(() => {
    elapsed = Math.floor((Date.now() - startTime) / 1000);
  }, 500);
}

function setPalette(name) {
  palette = PALETTES[name];
  resetGame();
}
