const CACHE_NAME = "aetherlearn-shell-v21";
const SHELL_ASSETS = [
  "./",
  "./index.html",
  "./styles.css",
  "./app.js?v=21",
  "./idb.js?v=21",
  "./manifest.webmanifest",
  "./content/manifest.json",
];

self.addEventListener("install", (event) => {
  event.waitUntil(caches.open(CACHE_NAME).then((cache) => cache.addAll(SHELL_ASSETS)).then(() => self.skipWaiting()));
});

self.addEventListener("activate", (event) => {
  event.waitUntil(
    caches.keys().then((keys) => Promise.all(keys.filter((key) => key.startsWith("aetherlearn-shell-") && key !== CACHE_NAME).map((key) => caches.delete(key))))
      .then(() => self.clients.claim()),
  );
});

self.addEventListener("fetch", (event) => {
  const request = event.request;
  if (request.method !== "GET") return;
  const url = new URL(request.url);
  if (url.origin !== self.location.origin) return;
  const cachedResponse = request.cache === "no-store" ? Promise.resolve(undefined) : caches.match(request);
  event.respondWith(
    cachedResponse.then((cached) => cached || fetch(request).then((response) => {
      if (response.ok && (url.pathname.endsWith(".md") || url.pathname.endsWith(".json"))) {
        const copy = response.clone();
        caches.open(CACHE_NAME).then((cache) => cache.put(request, copy));
      }
      return response;
    }).catch(() => caches.match("./index.html"))),
  );
});
