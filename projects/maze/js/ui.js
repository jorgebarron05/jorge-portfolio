/* ============================================
   MAZE — Canvas UI & Input
   ============================================ */

const canvas = document.getElementById('maze-canvas');
const ctx    = canvas.getContext('2d');
let CELL_PX  = 28;

/* ─── Sizing ────────────────────────────────── */
function resize() {
  const maxW = Math.min(700, window.innerWidth - 32);
  CELL_PX = Math.max(12, Math.floor(maxW / MAZE_W));
  canvas.width  = MAZE_W * CELL_PX;
  canvas.height = MAZE_H * CELL_PX;
}

/* ─── Render ────────────────────────────────── */
function render() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // ── Cell fill pass ──────────────────────────
  const solveSet = new Set(solvePath);
  for (let r = 0; r < MAZE_H; r++) {
    for (let c = 0; c < MAZE_W; c++) {
      const v = grid[r][c];
      const x = c * CELL_PX;
      const y = r * CELL_PX;

      // Base fill
      let fill = '#0a1628';
      if (visitedSet.has(v))  fill = '#0e2040';
      if (solveSet.has(v))    fill = 'rgba(77,124,138,0.55)';
      // Start and end
      if (r === 0 && c === 0)               fill = 'rgba(143,173,136,0.55)';
      if (r === MAZE_H - 1 && c === MAZE_W - 1) fill = 'rgba(212,168,53,0.55)';

      ctx.fillStyle = fill;
      ctx.fillRect(x, y, CELL_PX, CELL_PX);
    }
  }

  // ── Player ──────────────────────────────────
  const px = player.col * CELL_PX;
  const py = player.row * CELL_PX;
  const pad = Math.max(2, Math.floor(CELL_PX * 0.15));
  ctx.fillStyle = '#CBDF90';
  ctx.beginPath();
  ctx.roundRect(px + pad, py + pad, CELL_PX - pad * 2, CELL_PX - pad * 2, 3);
  ctx.fill();

  // ── Wall pass ───────────────────────────────
  ctx.strokeStyle = '#4D7C8A';
  ctx.lineWidth   = Math.max(1, CELL_PX * 0.08);

  for (let r = 0; r < MAZE_H; r++) {
    for (let c = 0; c < MAZE_W; c++) {
      const v = grid[r][c];
      const x = c * CELL_PX;
      const y = r * CELL_PX;

      // Right inner wall
      if (v.hasRightWall && c < MAZE_W - 1) {
        ctx.beginPath();
        ctx.moveTo(x + CELL_PX, y);
        ctx.lineTo(x + CELL_PX, y + CELL_PX);
        ctx.stroke();
      }
      // Bottom inner wall
      if (v.hasBottomWall && r < MAZE_H - 1) {
        ctx.beginPath();
        ctx.moveTo(x, y + CELL_PX);
        ctx.lineTo(x + CELL_PX, y + CELL_PX);
        ctx.stroke();
      }
    }
  }

  // ── Outer border (with entrance/exit gaps) ──
  const W = MAZE_W * CELL_PX;
  const H = MAZE_H * CELL_PX;
  ctx.strokeStyle = '#CBDF90';
  ctx.lineWidth   = Math.max(1.5, CELL_PX * 0.1);

  // Top: skip gap at col 0 (entrance)
  line(CELL_PX, 0, W, 0);
  // Bottom: skip gap at last col (exit)
  line(0, H, W - CELL_PX, H);
  // Left full
  line(0, 0, 0, H);
  // Right full
  line(W, 0, W, H);

  // ── Start / End labels ───────────────────────
  const sz = Math.max(8, Math.floor(CELL_PX * 0.38));
  ctx.font      = `700 ${sz}px 'Space Mono', monospace`;
  ctx.textAlign = 'center';
  ctx.fillStyle = '#8FAD88';
  ctx.fillText('S', CELL_PX / 2, CELL_PX / 2 + sz * 0.35);
  ctx.fillStyle = '#D4A835';
  ctx.fillText('E', W - CELL_PX / 2, H - CELL_PX / 2 + sz * 0.35);

  // ── HUD ─────────────────────────────────────
  document.getElementById('step-val').textContent  = stepCount;
  document.getElementById('size-val').textContent  = `${MAZE_W}×${MAZE_H}`;
  updateBest();

  // ── Win overlay ──────────────────────────────
  if (gameWon) drawWinOverlay();
}

function line(x1, y1, x2, y2) {
  ctx.beginPath(); ctx.moveTo(x1, y1); ctx.lineTo(x2, y2); ctx.stroke();
}

function drawWinOverlay() {
  ctx.fillStyle = 'rgba(9,22,40,0.78)';
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.textAlign = 'center';
  const big = Math.max(14, Math.floor(CELL_PX * 1.1));
  const sm  = Math.max(10, Math.floor(CELL_PX * 0.58));
  ctx.font      = `700 ${big}px 'Space Mono', monospace`;
  ctx.fillStyle = '#CBDF90';
  ctx.fillText(`You won in ${stepCount} steps!`, canvas.width / 2, canvas.height / 2 - CELL_PX * 0.5);
  ctx.font      = `${sm}px 'Space Mono', monospace`;
  ctx.fillStyle = 'rgba(223,238,221,0.6)';
  ctx.fillText('Press R to generate a new maze', canvas.width / 2, canvas.height / 2 + CELL_PX);
}

/* ─── Input ─────────────────────────────────── */
document.addEventListener('keydown', e => {
  switch (e.key) {
    case 'ArrowUp': case 'ArrowDown': case 'ArrowLeft': case 'ArrowRight':
      e.preventDefault();
      movePlayer(e.key);
      break;
    case 'r': case 'R':
      resetMaze();
      break;
    case 'b': case 'B':
      solvePath = solve(false); // BFS
      showSolverStat('bfs', solvePath);
      break;
    case 'd': case 'D':
      solvePath = solve(true);  // DFS
      showSolverStat('dfs', solvePath);
      break;
  }
});

/* ─── Personal best ─────────────────────────── */
function getBestKey() { return `maze-best-${MAZE_W}-${MAZE_H}`; }

function updateBest() {
  const stored = localStorage.getItem(getBestKey());
  const best = stored ? parseInt(stored, 10) : null;
  if (gameWon) {
    if (best === null || stepCount < best) {
      localStorage.setItem(getBestKey(), stepCount);
      document.getElementById('best-val').textContent = stepCount + ' ✦';
    } else {
      document.getElementById('best-val').textContent = best;
    }
  } else {
    document.getElementById('best-val').textContent = best !== null ? best : '—';
  }
}

/* ─── Solver stats ───────────────────────────── */
function showSolverStat(type, path) {
  const el = document.getElementById(type === 'bfs' ? 'bfs-len' : 'dfs-len');
  const stats = document.getElementById('solver-stats');
  if (el) el.textContent = path.length > 0 ? `${path.length} steps` : 'no path';
  if (stats) stats.style.display = 'flex';
}

// On-screen buttons for mobile
document.getElementById('btn-bfs')?.addEventListener('click', () => {
  solvePath = solve(false);
  showSolverStat('bfs', solvePath);
});
document.getElementById('btn-dfs')?.addEventListener('click', () => {
  solvePath = solve(true);
  showSolverStat('dfs', solvePath);
});
document.getElementById('btn-reset')?.addEventListener('click', () => {
  resetMaze();
  const stats = document.getElementById('solver-stats');
  if (stats) stats.style.display = 'none';
  document.getElementById('bfs-len').textContent = '—';
  document.getElementById('dfs-len').textContent = '—';
});

/* ─── Loop ──────────────────────────────────── */
function loop() {
  render();
  requestAnimationFrame(loop);
}

/* ─── Settings sliders ──────────────────────── */
document.getElementById('maze-w-input')?.addEventListener('input', function () {
  MAZE_W = parseInt(this.value, 10);
  document.getElementById('maze-w-display').textContent = MAZE_W;
  resize();
  resetMaze();
});

document.getElementById('maze-h-input')?.addEventListener('input', function () {
  MAZE_H = parseInt(this.value, 10);
  document.getElementById('maze-h-display').textContent = MAZE_H;
  resize();
  resetMaze();
});

/* ─── Init ──────────────────────────────────── */
(function init() {
  resize();
  window.addEventListener('resize', () => { resize(); });
  resetMaze();
  loop();
})();
