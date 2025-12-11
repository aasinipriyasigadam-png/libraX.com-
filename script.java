// Sample client-side data for books. Add more books or load from server later.
const BOOKS = [
  {
    id: "jungle-book",
    title: "The Jungle Book",
    author: "Rudyard Kipling (sample)",
    year: 1894,
    description: "A collection of stories set in the Indian jungle. Below are sample chapters (shortened for example).",
    chapters: [
      {
        number: 1,
        title: "Mowgli's Brothers",
        content:
`When the human child was left in the wolf-pack he was accepted by the wolves as one of themselves. The fierce tigress Shere Khan hated the very smell of him and vowed to have him when he was old enough. Akela the leader and Bagheera the black panther helped raise the boy, called Mowgli.

Bagheera brought him gifts and taught stealth; Baloo taught law and the ways of the jungle. When the council decided Mowgli must leave the Seeonee pack to avoid jealousy, he began his long journey among men and beasts.`
      },
      {
        number: 2,
        title: "Kaa's Hunting",
        content:
`Baloo and Bagheera escort Mowgli through the jungle to safety but he is captured by the Bandar-log, the monkey-people, and taken to the cold Lair of the monkeys. Baloo and Bagheera seek Kaa the python to help rescue Mowgli.

Kaa weaves a hypnotic coil; with cunning and courage they recover the boy, and Mowgli learns bravery, obedience, and the cunning of the jungle.`
      },
      {
        number: 3,
        title: "Tiger! Tiger!",
        content:
When he returns to man-village, Mowgli faces Shere Khan. Through clever planning, trap and fire, and with his knowledge of both men and beasts, Mowgli triumphs over the tiger. Yet he realizes his place lies between both worlds, never entirely of either.
      }
    ]
  }
];

// Helper: find book by title (case-insensitive, contains)
function findBookByName(query) {
  if (!query) return null;
  const q = query.trim().toLowerCase();
  return BOOKS.find(b => b.title.toLowerCase().includes(q));
}

// DOM refs
const searchInput = document.getElementById('searchInput');
const searchBtn = document.getElementById('searchBtn');
const resultArea = document.getElementById('resultArea');

function renderHint() {
  resultArea.innerHTML = <p class="hint">Search for a book to see its details and chapters here.</p>;
}

function renderNotFound(q) {
  resultArea.innerHTML = <p class="hint">No book found for "<strong>${escapeHtml(q)}</strong>". Try "Jungle Book".</p>;
}

// Escape HTML for safety
function escapeHtml(s){
  return (s+'').replace(/[&<>"']/g, m => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"}[m]));
}

// Render book details and chapters
function renderBook(book) {
  const metaHtml = `
    <div class="book-header">
      <div class="book-meta">
        <h2 class="book-title">${escapeHtml(book.title)}</h2>
        <div class="book-author">By ${escapeHtml(book.author)} — ${escapeHtml(book.year)}</div>
        <div class="book-desc">${escapeHtml(book.description)}</div>
        <div class="actions">
          <button id="showAllBtn" class="primary">Show all chapters</button>
          <button id="collapseAllBtn">Collapse all</button>
        </div>
      </div>
    </div>
  `;

  const chaptersHtml = book.chapters.map(ch => `
    <article class="chapter" data-number="${ch.number}">
      <div class="chapter-title" role="button" tabindex="0">
        <h3>${ch.number}. ${escapeHtml(ch.title)}</h3>
        <div class="chapter-controls">
          <small class="muted">Click to toggle</small>
        </div>
      </div>
      <div class="chapter-body collapsed">
        <p>${escapeHtml(ch.content).replace(/\n/g, '<br>')}</p>
      </div>
    </article>
  `).join('');

  resultArea.innerHTML = metaHtml + <div class="chapters">${chaptersHtml}</div>;

  // Add event listeners for toggles
  const chapterEls = resultArea.querySelectorAll('.chapter');
  chapterEls.forEach(el => {
    const titleBar = el.querySelector('.chapter-title');
    const body = el.querySelector('.chapter-body');
    function toggle() {
      body.classList.toggle('collapsed');
    }
    titleBar.addEventListener('click', toggle);
    titleBar.addEventListener('keydown', (e) => {
      if (e.key === 'Enter' || e.key === ' ') { e.preventDefault(); toggle(); }
    });
  });

  // Show all / collapse all buttons
  const showAllBtn = document.getElementById('showAllBtn');
  const collapseAllBtn = document.getElementById('collapseAllBtn');
  showAllBtn.addEventListener('click', () => {
    resultArea.querySelectorAll('.chapter-body').forEach(b => b.classList.remove('collapsed'));
    // smooth scroll to top of chapters
    const firstChapter = resultArea.querySelector('.chapter');
    if (firstChapter) firstChapter.scrollIntoView({behavior:'smooth'});
  });
  collapseAllBtn.addEventListener('click', () => {
    resultArea.querySelectorAll('.chapter-body').forEach(b => b.classList.add('collapsed'));
  });
}

// Handle search action
function doSearch() {
  const q = searchInput.value.trim();
  if (!q) {
    renderHint();
    return;
  }
  const book = findBookByName(q);
  if (!book) {
    renderNotFound(q);
    return;
  }
  renderBook(book);
}

// Wire events
searchBtn.addEventListener('click', doSearch);
searchInput.addEventListener('keydown', (e) => {
  if (e.key === 'Enter') doSearch();
});

// initial
renderHint();
