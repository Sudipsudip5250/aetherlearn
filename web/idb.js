const DB_NAME = "aetherlearn-web";
const DB_VERSION = 1;
const ACTIVE_PACK_KEY = "core-active";
const STAGING_PACK_KEY = "core-staging";
const STATE_KEY = "learning-state";

function openDatabase() {
  return new Promise((resolve, reject) => {
    const request = indexedDB.open(DB_NAME, DB_VERSION);
    request.onupgradeneeded = () => {
      const database = request.result;
      if (!database.objectStoreNames.contains("packs")) database.createObjectStore("packs", { keyPath: "key" });
      if (!database.objectStoreNames.contains("state")) database.createObjectStore("state", { keyPath: "key" });
    };
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error || new Error("IndexedDB could not open."));
  });
}

function requestResult(request) {
  return new Promise((resolve, reject) => {
    request.onsuccess = () => resolve(request.result);
    request.onerror = () => reject(request.error || new Error("IndexedDB request failed."));
  });
}

export async function readActivePack() {
  const database = await openDatabase();
  try {
    return await requestResult(database.transaction("packs", "readonly").objectStore("packs").get(ACTIVE_PACK_KEY));
  } finally {
    database.close();
  }
}

export async function readLearningState() {
  const database = await openDatabase();
  try {
    const value = await requestResult(database.transaction("state", "readonly").objectStore("state").get(STATE_KEY));
    return value?.data || { version: 1, progress: {}, notes: {}, bookmarks: {}, quiz: {} };
  } finally {
    database.close();
  }
}

export async function writeLearningState(data) {
  const database = await openDatabase();
  try {
    await requestResult(database.transaction("state", "readwrite").objectStore("state").put({ key: STATE_KEY, data: { ...data, version: 1 } }));
  } finally {
    database.close();
  }
}

export async function replaceActivePack(pack) {
  const database = await openDatabase();
  try {
    const transaction = database.transaction("packs", "readwrite");
    const store = transaction.objectStore("packs");
    store.put({ ...pack, key: STAGING_PACK_KEY });
    store.put({ ...pack, key: ACTIVE_PACK_KEY });
    store.delete(STAGING_PACK_KEY);
    await new Promise((resolve, reject) => {
      transaction.oncomplete = resolve;
      transaction.onerror = () => reject(transaction.error || new Error("Content cache transaction failed."));
      transaction.onabort = () => reject(transaction.error || new Error("Content cache transaction was aborted."));
    });
  } finally {
    database.close();
  }
}

export async function clearActivePack() {
  const database = await openDatabase();
  try {
    const transaction = database.transaction("packs", "readwrite");
    const store = transaction.objectStore("packs");
    store.delete(ACTIVE_PACK_KEY);
    store.delete(STAGING_PACK_KEY);
    await new Promise((resolve, reject) => {
      transaction.oncomplete = resolve;
      transaction.onerror = () => reject(transaction.error || new Error("Content cache could not be cleared."));
      transaction.onabort = () => reject(transaction.error || new Error("Content cache clear was aborted."));
    });
  } finally {
    database.close();
  }
}

export async function clearLearningState() {
  const database = await openDatabase();
  try {
    await requestResult(database.transaction("state", "readwrite").objectStore("state").delete(STATE_KEY));
  } finally {
    database.close();
  }
}

export function activePackKey() { return ACTIVE_PACK_KEY; }
