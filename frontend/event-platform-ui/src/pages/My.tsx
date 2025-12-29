import { useEffect, useState } from "react";
import { API } from "../api";
import type { Reservation, Payment } from "../types";

export default function My() {
    const [res, setRes] = useState<Reservation[]>([]);
    const [pay, setPay] = useState<Payment[]>([]);
    const [err, setErr] = useState("");

    useEffect(() => {
        Promise.all([
            API.request<Reservation[]>("/api/reservations/my"),
            API.request<Payment[]>("/api/payments/my"),
        ])
            .then(([r, p]) => { setRes(r); setPay(p); })
            .catch((e) => setErr(e.message));
    }, []);

    return (
        <div className="grid2">
            <div className="card">
                <h2>My reservations</h2>
                {err && <p className="error">{err}</p>}
                {res.map((r) => (
                    <div key={r.id} className="row">
                        <b>#{r.id}</b> event={r.eventId} qty={r.quantity} total={r.totalPrice} status={r.status}
                    </div>
                ))}
            </div>

            <div className="card">
                <h2>My payments</h2>
                {pay.map((p) => (
                    <div key={p.id} className="row">
                        <b>#{p.id}</b> res={p.reservationId} amount={p.amount} status={p.status}
                    </div>
                ))}
            </div>
        </div>
    );
}
