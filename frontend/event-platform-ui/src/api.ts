export const API = {
    async request<T>(path: string, options: RequestInit = {}): Promise<T> {
        const token = localStorage.getItem("token");
        const headers: Record<string, string> = {
            "Content-Type": "application/json",
            ...(options.headers as any),
        };
        if (token) headers["Authorization"] = `Bearer ${token}`;

        const res = await fetch(path, { ...options, headers });
        if (!res.ok) {
            const txt = await res.text();
            throw new Error(txt || `HTTP ${res.status}`);
        }

        // Certains endpoints (ex: DELETE) peuvent retourner un body vide.
        // res.json() échoue alors avec "Unexpected end of JSON input".
        if (res.status === 204) return undefined as T;

        const text = await res.text();
        if (!text) return undefined as T;

        const ct = (res.headers.get("content-type") || "").toLowerCase();
        if (ct.includes("application/json")) {
            return JSON.parse(text) as T;
        }

        // fallback (rare): retourner le texte brut
        return text as unknown as T;
    },
};
