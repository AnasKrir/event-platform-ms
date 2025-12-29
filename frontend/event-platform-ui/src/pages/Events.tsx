import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { API } from "../api";
import type { Event } from "../types";

export default function Events() {
    const [items, setItems] = useState<Event[]>([]);
    const [err, setErr] = useState("");

    useEffect(() => {
        API.request<Event[]>("/api/events")
            .then(setItems)
            .catch((e) => setErr(e.message));
    }, []);

    const token = localStorage.getItem("token");

    return (
        <div className="card">
            <h2>Events</h2>
            {err && <p className="error">{err}</p>}
            <div className="grid">
                {items.map((e) => (
                    <div key={e.id} className="event">
                        <h3>{e.title}</h3>
                        <p className="muted">
                            {e.organizer} — {e.participantA} vs {e.participantB}
                        </p>
                        <p className="muted">Tickets: {e.remainingTickets}/{e.totalTickets} — Price: {e.ticketPrice}</p>
                        {token ? (
                            <Link className="btn" to={`/reserve/${e.id}`}>Reserve</Link>
                        ) : (
                            <p className="muted">Login to reserve</p>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}
