/* ============================================
   MAPS — Helpers · Global Journey Map · NYC Map
   ============================================ */

/* ─── MARKER ICON ────────────────────────────── */
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

/* ─── POPUP HTML ─────────────────────────────── */
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
