export const request = (...[url, ...args]: Parameters<typeof fetch>) =>
  fetch(String(process.env.EXPO_PUBLIC_API_URL) + url, ...args);
