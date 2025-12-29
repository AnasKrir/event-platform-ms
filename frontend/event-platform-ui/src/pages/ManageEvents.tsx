import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { API } from "../api";
import type { Event } from "../types";

export default function ManageEvents() {
  const [items, setItems] = useState<Event[]>([]);
  const [q, setQ] = useState("");
  const [err, setErr] = useState("");
  const [loading, setLoading] = useState(false);

  const load = () => {
    setLoading(true);
    setErr("");
    API.request<Event[]>("/api/events")
      .then(setItems)
      .catch((e) => setErr(e.message))
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    load();
  }, []);

  const filtered = useMemo(() => {
    const s = q.trim().toLowerCase();
    if (!s) return items;
    return items.filter(
      (e) =>
        e.title.toLowerCase().includes(s) ||
        e.organizer.toLowerCase().includes(s) ||
        e.location?.toLowerCase().includes(s)
    );
  }, [items, q]);

  const onDelete = async (id: number) => {
    if (!confirm(`Supprimer l'événement #${id} ?`)) return;
    try {
      await API.request<void>(`/api/events/${id}`, { method: "DELETE" });
      setItems((prev) => prev.filter((x) => x.id !== id));
    } catch (e: any) {
      alert(e.message);
    }
  };

  return (
    <div className="card">
      <div style={{ display: "flex", justifyContent: "space-between", gap: 12, flexWrap: "wrap" }}>
        <h2 style={{ margin: 0 }}>Espace Organisateur — Gestion des events</h2>
        <Link className="btn primary" to="/organizer/events/new">
          + Ajouter event
        </Link>
      </div>

      <div style={{ display: "flex", gap: 10, marginTop: 12, alignItems: "center", flexWrap: "wrap" }}>
        <input
          value={q}
          onChange={(e) => setQ(e.target.value)}
          placeholder="Rechercher (title / organizer / location)…"
          style={{ minWidth: 280 }}
        />
        <button className="btn" onClick={load} disabled={loading}>
          {loading ? "Loading…" : "Refresh"}
        </button>
      </div>

      {err && <p className="error" style={{ marginTop: 12 }}>{err}</p>}

      <div style={{ overflowX: "auto", marginTop: 12 }}>
        <table className="table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Title</th>
              <th>Organizer</th>
              <th>Location</th>
              <th>Start</th>
              <th>Price</th>
              <th>Tickets</th>
              <th style={{ width: 160 }}>Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((e) => (
              <tr key={e.id}>
                <td>{e.id}</td>
                <td>{e.title}</td>
                <td>{e.organizer}</td>
                <td>{e.location || "—"}</td>
                <td>{new Date(e.startAt).toLocaleString()}</td>
                <td>{e.ticketPrice}</td>
                <td>
                  {e.remainingTickets}/{e.totalTickets}
                </td>
                <td>
                  <div style={{ display: "flex", gap: 8 }}>
                    <Link className="btn" to={`/organizer/events/${e.id}/edit`}>
                      Edit
                    </Link>
                    <button className="btn danger" onClick={() => onDelete(e.id)}>
                      Delete
                    </button>
                  </div>
                </td>
              </tr>
            ))}

            {!loading && filtered.length === 0 && (
              <tr>
                <td colSpan={8} className="muted" style={{ padding: 16 }}>
                  Aucun event.
                </td>
              </tr>
            )}
          </tbody>
        </table>
      </div>

      <p className="muted" style={{ marginTop: 12 }}>
        Permissions requises: <b>ORGANIZER</b> ou <b>ADMIN</b> (API: <code>POST/PUT/DELETE /api/events</code>).
      </p>
    </div>
  );
}
