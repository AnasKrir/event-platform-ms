import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { API } from "../api";
import type { Reservation } from "../types";

export default function Reserve() {
    const { eventId } = useParams();
    const nav = useNavigate();
    const [qty, setQty] = useState(1);
    const [err, setErr] = useState("");

    const submit = async (e: any) => {
        e.preventDefault();
        setErr("");
        try {
            const r = await API.request<Reservation>("/api/reservations", {
                method: "POST",
                body: JSON.stringify({ eventId: Number(eventId), quantity: qty }),
            });
            nav(`/pay/${r.id}`);
        } catch (ex: any) {
            setErr(ex.message);
        }
    };

    return (
        <div className="card">
            <h2>Reserve tickets</h2>
            {err && <p className="error">{err}</p>}
            <form onSubmit={submit} className="form">
                <label>Quantity (max 4)</label>
                <input type="number" min={1} max={4} value={qty} onChange={(e) => setQty(Number(e.target.value))} />
                <button className="btn primary">Reserve</button>
            </form>
        </div>
    );
}
