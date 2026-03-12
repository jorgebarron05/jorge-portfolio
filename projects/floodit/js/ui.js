/* ============================================
   FLOOD IT — Canvas UI & Input
   ============================================ */

const canvas = document.getElementById('game-canvas');
const ctx    = canvas.getContext('2d');
let CELL_PX  = 36;

/* ─── Sizing ────────────────────────────────── */
function resize() {
  const maxW = Math.min(504, window.innerWidth - 48);
  CELL_PX = Math.max(18, Math.floor(maxW / BOARD_SIZE));
  canvas.width  = BOARD_SIZE * CELL_PX;
  canvas.height = BOARD_SIZE * CELL_PX;
}

/* ─── Render ────────────────────────────────── */
function render() {
  ctx.clearRect(0, 0, canvas.width, canvas.height);

  // Draw cells
  for (const cell of board) {
    const x = cell.col * CELL_PX;
    const y = cell.row * CELL_PX;
    ctx.fillStyle = cell.color;
    ctx.fillRect(x, y, CELL_PX, CELL_PX);
    // Subtle grid lines
    ctx.strokeStyle = 'rgba(0,0,0,0.12)';
    ctx.lineWidth = 0.5;
    ctx.strokeRect(x, y, CELL_PX, CELL_PX);
  }

  // Highlight the boundary of the flooded region
  ctx.strokeStyle = 'rgba(255,255,255,0.3)';
  ctx.lineWidth = 2;
  for (const cell of board) {
    if (!cell.flooded) continue;
    const x = cell.col * CELL_PX;
    const y = cell.row * CELL_PX;
    if (!cell.top    || !cell.top.flooded)    drawSeg(x,           y,           x + CELL_PX, y          );
    if (!cell.bottom || !cell.bottom.flooded) drawSeg(x,           y + CELL_PX, x + CELL_PX, y + CELL_PX);
    if (!cell.left   || !cell.left.flooded)   drawSeg(x,           y,           x,           y + CELL_PX);
    if (!cell.right  || !cell.right.flooded)  drawSeg(x + CELL_PX, y,           x + CELL_PX, y + CELL_PX);
  }

  updateHUD();
  if (gameState !== 'playing') drawEndOverlay();
}

function drawSeg(x1, y1, x2, y2) {
  ctx.beginPath();
  ctx.moveTo(x1, y1);
  ctx.lineTo(x2, y2);
  ctx.stroke();
}

function drawEndOverlay() {
  ctx.fillStyle = 'rgba(9,22,40,0.78)';
  ctx.fillRect(0, 0, canvas.width, canvas.height);
  ctx.textAlign = 'center';
  const big = Math.max(14, Math.floor(CELL_PX * 0.85));
  const sm  = Math.max(11, Math.floor(CELL_PX * 0.52));
  ctx.font      = `700 ${big}px 'Space Mono', monospace`;
  ctx.fillStyle = gameState === 'win' ? '#CBDF90' : '#D4845A';
  const msg = gameState === 'win'
    ? `You won in ${steps} step${steps === 1 ? '' : 's'}!`
    : 'Out of moves!';
  ctx.fillText(msg, canvas.width / 2, canvas.height / 2 - CELL_PX * 0.6);
  ctx.font      = `${sm}px 'Space Mono', monospace`;
  ctx.fillStyle = 'rgba(223,238,221,0.65)';
  ctx.fillText('Press R to play again', canvas.width / 2, canvas.height / 2 + CELL_PX * 0.6);
}

function getBestKey() { return `floodit-best-${BOARD_SIZE}-${NUM_COLORS}`; }

function updateBest() {
  const stored = localStorage.getItem(getBestKey());
  const best = stored ? parseInt(stored, 10) : null;
  if (gameState === 'win') {
    if (best === null || steps < best) {
      localStorage.setItem(getBestKey(), steps);
      document.getElementById('best-val').textContent = steps + ' ✦';
    } else {
      document.getElementById('best-val').textContent = best;
    }
  } else {
    document.getElementById('best-val').textContent = best !== null ? best : '—';
  }
}

function updateHUD() {
  document.getElementById('steps-val').textContent   = `${steps} / ${STEP_LIMIT}`;
  document.getElementById('elapsed-val').textContent = `${elapsed}s`;
  updateBest();
  // Keep active button in sync with curColor
  document.querySelectorAll('.color-btn').forEach(btn => {
    btn.classList.toggle('active', btn.dataset.color === curColor);
  });
}

/* ─── Color buttons ─────────────────────────── */
function buildColorButtons() {
  const container = document.getElementById('color-buttons');
  container.innerHTML = '';
  for (const color of palette) {
    const btn = document.createElement('button');
    btn.className        = 'color-btn';
    btn.dataset.color    = color;
    btn.style.background = color;
    btn.setAttribute('aria-label', `Flood with ${color}`);
    btn.addEventListener('click', () => floodFill(color));
    container.appendChild(btn);
  }
}

/* ─── Input ─────────────────────────────────── */
document.addEventListener('keydown', e => {
  switch (e.key.toLowerCase()) {
    case 'r': resetGame(); buildColorButtons(); break;
    case 'd': setPalette('dark');  buildColorButtons(); break;
    case 'l': setPalette('light'); buildColorButtons(); break;
  }
});

/* ─── Loop ──────────────────────────────────── */
function loop() {
  render();
  requestAnimationFrame(loop);
}

/* ─── Settings sliders ──────────────────────── */
document.getElementById('board-size-input')?.addEventListener('input', function () {
  BOARD_SIZE = parseInt(this.value, 10);
  document.getElementById('board-size-display').textContent = BOARD_SIZE;
  resize();
  resetGame();
  buildColorButtons();
});

document.getElementById('colors-input')?.addEventListener('input', function () {
  NUM_COLORS = parseInt(this.value, 10);
  document.getElementById('colors-display').textContent = NUM_COLORS;
  resetGame();
  buildColorButtons();
});

/* ─── Init ──────────────────────────────────── */
(function init() {
  resize();
  window.addEventListener('resize', () => { resize(); });
  resetGame();
  buildColorButtons();
  loop();
})();
