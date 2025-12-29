import { useEffect, useState } from "react";
import { API } from "../api";

type AdminStats = {
  totalEvents: number;
  ticketsSold: number;
  revenue: number;
  paymentsSuccess: number;
  paymentsFailed: number;
};

export default function AdminDashboard() {
  const [stats, setStats] = useState<AdminStats | null>(null);
  const [err, setErr] = useState("");

  useEffect(() => {
    API.request<AdminStats>("/api/payments/admin/stats")
      .then(setStats)
      .catch((e) => setErr(e.message));
  }, []);

  return (
    <div className="card">
      <h2>Admin Dashboard</h2>
      {err && (
        <p className="error">
          {err}
          {err.toLowerCase().includes("403") && (
            <span className="muted"> (Tu dois être connecté en ADMIN)</span>
          )}
        </p>
      )}

      {!err && !stats && <p className="muted">Chargement…</p>}

      {stats && (
        <div className="grid">
          <div className="event">
            <h3>Total events</h3>
            <p className="big">{stats.totalEvents}</p>
          </div>
          <div className="event">
            <h3>Tickets sold</h3>
            <p className="big">{stats.ticketsSold}</p>
          </div>
          <div className="event">
            <h3>Revenue</h3>
            <p className="big">{stats.revenue}</p>
          </div>
          <div className="event">
            <h3>Payments success</h3>
            <p className="big">{stats.paymentsSuccess}</p>
          </div>
          <div className="event">
            <h3>Payments failed</h3>
            <p className="big">{stats.paymentsFailed}</p>
          </div>
        </div>
      )}

      <p className="muted" style={{ marginTop: 16 }}>
        API: <code>/api/payments/admin/stats</code>
      </p>
    </div>
  );
}
