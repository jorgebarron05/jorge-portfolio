/* ============================================
   JORGE BARRON RIVERO — PORTFOLIO SCRIPTS
   ============================================ */

/* ─── NAVBAR ─────────────────────────────────── */
const navbar = document.getElementById('navbar');
window.addEventListener('scroll', () => {
  navbar.classList.toggle('scrolled', window.scrollY > 40);
});

/* ─── TYPEWRITER ─────────────────────────────── */
const roles = [
  'ETL Architect',
  'Data Engineer',
  'Data Analytics Engineer',
  'Pipeline Architect',
  'Global Builder',
];

let roleIdx = 0, charIdx = 0, deleting = false;
const typeEl = document.getElementById('typewriter');

function type() {
  if (!typeEl) return;
  const word = roles[roleIdx];
  typeEl.textContent = deleting ? word.slice(0, charIdx - 1) : word.slice(0, charIdx + 1);
  deleting ? charIdx-- : charIdx++;

  if (!deleting && charIdx === word.length) {
    deleting = true;
    setTimeout(type, 1800);
    return;
  }
  if (deleting && charIdx === 0) {
    deleting = false;
    roleIdx = (roleIdx + 1) % roles.length;
  }
  setTimeout(type, deleting ? 48 : 88);
}
setTimeout(type, 1200);

/* ─── PARTICLE CANVAS ────────────────────────── */
(function () {
  const canvas = document.getElementById('particle-canvas');
  if (!canvas) return;
  const ctx = canvas.getContext('2d');
  let W, H, pts;

  function resize() {
    W = canvas.width  = canvas.offsetWidth;
    H = canvas.height = canvas.offsetHeight;
  }

  function init() {
    const n = Math.floor((W * H) / 16000);
    pts = Array.from({ length: n }, () => ({
      x: Math.random() * W, y: Math.random() * H,
      r: Math.random() * 1.1 + 0.3,
      vx: (Math.random() - 0.5) * 0.16,
      vy: (Math.random() - 0.5) * 0.16,
      a: Math.random() * 0.45 + 0.1,
    }));
  }

  function draw() {
    ctx.clearRect(0, 0, W, H);
    // Lines between nearby particles
    for (let i = 0; i < pts.length; i++) {
      for (let j = i + 1; j < pts.length; j++) {
        const dx = pts[i].x - pts[j].x, dy = pts[i].y - pts[j].y;
        const d = Math.sqrt(dx * dx + dy * dy);
        if (d < 100) {
          ctx.beginPath();
          // Earthy teal lines
          ctx.strokeStyle = `rgba(77, 124, 138, ${0.07 * (1 - d / 100)})`;
          ctx.lineWidth = 0.5;
          ctx.moveTo(pts[i].x, pts[i].y);
          ctx.lineTo(pts[j].x, pts[j].y);
          ctx.stroke();
        }
      }
    }
    // Dots in lime/green palette
    pts.forEach(p => {
      ctx.beginPath();
      ctx.arc(p.x, p.y, p.r, 0, Math.PI * 2);
      ctx.fillStyle = `rgba(203, 223, 144, ${p.a})`;
      ctx.fill();
      p.x += p.vx; p.y += p.vy;
      if (p.x < 0 || p.x > W) p.vx *= -1;
      if (p.y < 0 || p.y > H) p.vy *= -1;
    });
    requestAnimationFrame(draw);
  }

  resize(); init(); draw();
  window.addEventListener('resize', () => { resize(); init(); });
})();

/* ─── SCROLL REVEAL ──────────────────────────── */
const revealObs = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (!entry.isIntersecting) return;
    const siblings = Array.from(entry.target.parentElement?.querySelectorAll('.reveal') || []);
    const delay = siblings.indexOf(entry.target) * 80;
    setTimeout(() => entry.target.classList.add('visible'), delay);
    revealObs.unobserve(entry.target);
  });
}, { threshold: 0.1, rootMargin: '0px 0px -40px 0px' });

document.querySelectorAll('.reveal').forEach(el => revealObs.observe(el));

/* ─── SECTION LINE DRAW ──────────────────────── */
const lineObs = new IntersectionObserver((entries) => {
  entries.forEach(entry => {
    if (entry.isIntersecting) {
      entry.target.classList.add('animated');
      lineObs.unobserve(entry.target);
    }
  });
}, { threshold: 0.5 });

document.querySelectorAll('.section-line').forEach(el => lineObs.observe(el));

/* ─── SMOOTH NAV ─────────────────────────────── */
document.querySelectorAll('a[href^="#"]').forEach(a => {
  a.addEventListener('click', e => {
    const t = document.querySelector(a.getAttribute('href'));
    if (t) { e.preventDefault(); t.scrollIntoView({ behavior: 'smooth' }); }
  });
});

/* ─── MAP HELPERS ────────────────────────────── */
function markerIcon(type, current = false) {
  const glyphs = { school: '🏫', university: '🎓', work: '💼', home: '🏠' };
  return L.divIcon({
    className: '',
    html: `<div class="map-marker ${type}${current ? ' current' : ''}">
      <span>${glyphs[type] || '📍'}</span>
      ${current ? '<div class="marker-pulse"></div>' : ''}
    </div>`,
    iconSize: [36, 36],
    iconAnchor: [18, 18],
    popupAnchor: [0, -22],
  });
}

function popupHtml(loc) {
  let h = `<div class="map-popup">
    <h3>${loc.name}</h3>
    <p class="popup-location">${loc.location}</p>
    <p class="popup-years">${loc.years}</p>`;
  if (loc.role)        h += `<p class="popup-role">${loc.role}</p>`;
  if (loc.achievement) h += `<p class="popup-achievement">${loc.achievement}</p>`;
  if (loc.note)        h += `<p class="popup-note">${loc.note}</p>`;
  h += `</div>`;
  return h;
}

/* ─── GLOBAL MAP DATA ────────────────────────── */
const globalLocations = [
  {
    lat: 51.5074, lng: -0.1278,
    name: 'London, England',
    location: '🏴󠁧󠁢󠁥󠁮󠁧󠁿 London, England',
    years: 'Early childhood',
    type: 'school',
  },
  {
    lat: 57.1497, lng: -2.0943,
    name: 'International School Aberdeen',
    location: '🏴󠁧󠁢󠁳󠁣󠁴󠁿 Aberdeen, Scotland',
    years: '2007 – 2008',
    type: 'school',
  },
  {
    lat: -17.7834, lng: -63.1821,
    name: 'Santa Cruz, Bolivia',
    location: '🇧🇴 Santa Cruz, Bolivia',
    years: 'Hometown',
    type: 'home',
    achievement: '🏠 Where I am from',
  },
  {
    lat: -34.6037, lng: -58.3816,
    name: 'Asociación Escuelas Lincoln',
    location: '🇦🇷 Buenos Aires, Argentina',
    years: '2009 – 2010',
    type: 'school',
  },
  {
    lat: 23.1136, lng: -82.3666,
    name: 'International School of Havana',
    location: '🇨🇺 Havana, Cuba',
    years: '2010 – 2012',
    type: 'school',
  },
  {
    lat: -8.8368, lng: 13.2343,
    name: 'Luanda International School',
    location: '🇦🇴 Luanda, Angola',
    years: '2013 – 2014',
    type: 'school',
  },
  {
    lat: -22.9068, lng: -43.1729,
    name: 'EARJ – Escola Americana do Rio de Janeiro',
    location: '🇧🇷 Rio de Janeiro, Brazil',
    years: '2015 – 2016',
    type: 'school',
  },
  {
    lat: 40.4168, lng: -3.7038,
    name: 'American School of Madrid',
    location: '🇪🇸 Madrid, Spain',
    years: '2016 – 2020',
    type: 'school',
    achievement: '🎓 IB Diploma · International Baccalaureate',
  },
  {
    lat: 42.3384, lng: -71.1032,
    name: 'Northeastern University',
    location: '🇺🇸 Boston, MA',
    years: '2020 – 2024',
    type: 'university',
    achievement: 'B.S. Computer Science & Economics · GPA 3.55',
  },
  {
    lat: 42.3684, lng: -71.1740,
    name: 'Perkins School for the Blind',
    location: '🇺🇸 Watertown, MA',
    years: '2023',
    type: 'work',
    role: 'Research & Data Analyst Co-op',
  },
  {
    lat: 40.7128, lng: -74.0060,
    name: 'New York City',
    location: '🇺🇸 New York, NY',
    years: '2022 – Present',
    type: 'work',
    role: 'Data Analytics Engineer',
    note: 'Annaly · Avenue One · Freestone Grove',
  },
];

/* ISO 3166-1 numeric codes for countries lived in */
const livedISO = new Set([24, 32, 68, 76, 192, 826, 724, 840]);

/* ─── GLOBAL MAP INIT ────────────────────────── */
const globalMap = L.map('global-map', {
  center: [20, 10], zoom: 2,
  scrollWheelZoom: false,
  zoomControl: true,
  minZoom: 1.5, maxZoom: 8,
});

L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
  attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors © <a href="https://carto.com/attributions">CARTO</a>',
  subdomains: 'abcd', maxZoom: 19,
}).addTo(globalMap);

// Country highlight layer via TopoJSON
fetch('https://cdn.jsdelivr.net/npm/world-atlas@2/countries-110m.json')
  .then(r => r.json())
  .then(world => {
    const countries = topojson.feature(world, world.objects.countries);
    L.geoJSON(countries, {
      style: f => {
        const lived = livedISO.has(+f.id);
        return lived
          ? { fillColor: '#8FAD88', fillOpacity: 0.28, color: '#CBDF90', weight: 1.5, opacity: 0.65 }
          : { fillColor: 'transparent', fillOpacity: 0, color: '#4D7C8A', weight: 0.3, opacity: 0.25 };
      },
      interactive: false,
    }).addTo(globalMap);
  })
  .catch(() => { /* silently skip highlight layer — markers still render */ })
  .finally(() => {
    // Add markers regardless of whether topojson loaded
    globalLocations.forEach(loc => {
      L.marker([loc.lat, loc.lng], { icon: markerIcon(loc.type) })
        .bindPopup(popupHtml(loc), { className: 'custom-popup', maxWidth: 260 })
        .addTo(globalMap);
    });
  });

/* ─── NYC MAP DATA ───────────────────────────── */
const nycLocations = [
  {
    lat: 40.7580, lng: -73.9855,
    name: 'Annaly Capital Management',
    location: '📍 1211 Avenue of the Americas, Midtown',
    years: 'Jul – Dec 2022',
    role: 'PM & Application Support Co-op',
    type: 'work',
  },
  {
    lat: 40.7484, lng: -73.9820,
    name: 'Avenue One',
    location: '📍 Midtown, New York',
    years: 'May 2024 – May 2025',
    role: 'Data Analytics Engineer',
    type: 'work',
    note: '(neighborhood)',
  },
  {
    lat: 40.7614, lng: -73.9706,
    name: 'Freestone Grove Partners',
    location: '📍 Midtown, New York',
    years: 'Jul 2025 – Present',
    role: 'Data Analytics Engineer',
    type: 'work',
    current: true,
    note: '(neighborhood)',
  },
];

/* ─── NYC MAP INIT ───────────────────────────── */
const nycMap = L.map('nyc-map', {
  center: [40.7540, -73.9770], zoom: 13,
  scrollWheelZoom: false,
  zoomControl: true,
});

L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {
  attribution: '© <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors © <a href="https://carto.com/attributions">CARTO</a>',
  subdomains: 'abcd', maxZoom: 19,
}).addTo(nycMap);

nycLocations.forEach(loc => {
  L.marker([loc.lat, loc.lng], { icon: markerIcon('work', loc.current) })
    .bindPopup(popupHtml(loc), { className: 'custom-popup', maxWidth: 240 })
    .addTo(nycMap);
});
