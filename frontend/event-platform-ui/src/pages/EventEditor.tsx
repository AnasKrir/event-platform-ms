import { useEffect, useMemo, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import { API } from "../api";
import type { Event } from "../types";

const empty: Partial<Event> = {
  title: "",
  description: "",
  location: "",
  organizer: "",
  participantA: "",
  participantB: "",
  startAt: new Date().toISOString().slice(0, 16),
  ticketPrice: 0,
  totalTickets: 40000,
};

function toInputDatetime(iso: string) {
  // ISO -> "YYYY-MM-DDTHH:mm" (datetime-local)
  const d = new Date(iso);
  const pad = (n: number) => String(n).padStart(2, "0");
  return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}T${pad(d.getHours())}:${pad(d.getMinutes())}`;
}

function fromInputDatetime(v: string) {
  // "YYYY-MM-DDTHH:mm" -> ISO string with timezone
  const d = new Date(v);
  return d.toISOString();
}

export default function EventEditor() {
  const { id } = useParams();
  const editingId = id ? Number(id) : null;
  const nav = useNavigate();

  const [form, setForm] = useState<Partial<Event>>(empty);
  const [err, setErr] = useState("");
  const [saving, setSaving] = useState(false);

  const isEdit = useMemo(() => Boolean(editingId), [editingId]);

  useEffect(() => {
    if (!isEdit) return;
    API.request<Event>(`/api/events/${editingId}`)
      .then((e) =>
        setForm({
          ...e,
          startAt: toInputDatetime(e.startAt),
        })
      )
      .catch((e) => setErr(e.message));
  }, [isEdit, editingId]);

  const set = (k: keyof Event) => (e: any) => {
    setForm((p) => ({ ...p, [k]: e.target.value }));
  };

  const setNumber = (k: keyof Event) => (e: any) => {
    const v = e.target.value;
    setForm((p) => ({ ...p, [k]: v === "" ? ("" as any) : Number(v) }));
  };

  const submit = async (e: any) => {
    e.preventDefault();
    setErr("");
    setSaving(true);
    try {
      const payload = {
        title: form.title,
        description: form.description,
        location: form.location,
        organizer: form.organizer,
        participantA: form.participantA,
        participantB: form.participantB,
        startAt: fromInputDatetime(String(form.startAt || "")),
        ticketPrice: Number(form.ticketPrice || 0),
        totalTickets: Number(form.totalTickets || 1),
      };

      if (isEdit) {
        await API.request(`/api/events/${editingId}`, {
          method: "PUT",
          body: JSON.stringify(payload),
        });
      } else {
        await API.request(`/api/events`, {
          method: "POST",
          body: JSON.stringify(payload),
        });
      }
      nav("/organizer/events");
    } catch (ex: any) {
      setErr(ex.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="card">
      <div style={{ display: "flex", justifyContent: "space-between", gap: 12, flexWrap: "wrap" }}>
        <h2 style={{ margin: 0 }}>{isEdit ? `Modifier event #${editingId}` : "Ajouter un event"}</h2>
        <Link className="btn" to="/organizer/events">
          Retour
        </Link>
      </div>

      {err && <p className="error" style={{ marginTop: 12 }}>{err}</p>}

      <form onSubmit={submit} className="form" style={{ marginTop: 12 }}>
        <input value={form.title ?? ""} onChange={set("title")} placeholder="Title" required />
        <input value={form.organizer ?? ""} onChange={set("organizer")} placeholder="Organizer" required />

        <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
          <input value={form.participantA ?? ""} onChange={set("participantA")} placeholder="Participant A" required style={{ flex: 1, minWidth: 220 }} />
          <input value={form.participantB ?? ""} onChange={set("participantB")} placeholder="Participant B" required style={{ flex: 1, minWidth: 220 }} />
        </div>

        <input value={form.location ?? ""} onChange={set("location")} placeholder="Location" />
        <textarea
          value={form.description ?? ""}
          onChange={set("description")}
          placeholder="Description"
          rows={4}
          style={{ resize: "vertical" }}
        />

        <div style={{ display: "flex", gap: 10, flexWrap: "wrap" }}>
          <label style={{ display: "flex", flexDirection: "column", gap: 6, flex: 1, minWidth: 240 }}>
            <span className="muted">Start (datetime)</span>
            <input
              type="datetime-local"
              value={String(form.startAt ?? "")}
              onChange={set("startAt")}
              required
            />
          </label>

          <label style={{ display: "flex", flexDirection: "column", gap: 6, flex: 1, minWidth: 200 }}>
            <span className="muted">Ticket price</span>
            <input type="number" step="0.01" value={form.ticketPrice ?? 0} onChange={setNumber("ticketPrice")} required />
          </label>

          <label style={{ display: "flex", flexDirection: "column", gap: 6, flex: 1, minWidth: 200 }}>
            <span className="muted">Total tickets</span>
            <input type="number" min={1} value={form.totalTickets ?? 1} onChange={setNumber("totalTickets")} required />
          </label>
        </div>

        <button className="btn primary" disabled={saving}>
          {saving ? "Saving…" : isEdit ? "Update" : "Create"}
        </button>
      </form>

      <p className="muted" style={{ marginTop: 12 }}>
        Corps envoyé: <code>title, organizer, participantA, participantB, startAt, ticketPrice, totalTickets</code>
      </p>
    </div>
  );
}
