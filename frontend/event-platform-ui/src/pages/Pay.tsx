import { useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { API } from "../api";
import type { Payment } from "../types";

export default function Pay() {
    const { reservationId } = useParams();
    const nav = useNavigate();
    const [forceFail, setForceFail] = useState(false);
    const [err, setErr] = useState("");
    const [done, setDone] = useState<Payment | null>(null);

    const submit = async (e: any) => {
        e.preventDefault();
        setErr("");
        try {
            const p = await API.request<Payment>("/api/payments", {
                method: "POST",
                body: JSON.stringify({ reservationId: Number(reservationId), method: "SIMULATED_CARD", forceFail }),
            });
            setDone(p);
            setTimeout(() => nav("/my"), 500);
        } catch (ex: any) {
            setErr(ex.message);
        }
    };

    return (
        <div className="card">
            <h2>Pay (simulation)</h2>
            {err && <p className="error">{err}</p>}
            {done && <p className="ok">Payment: {done.status}</p>}
            <form onSubmit={submit} className="form">
                <label>
                    <input type="checkbox" checked={forceFail} onChange={(e) => setForceFail(e.target.checked)} />
                    Force FAIL (test)
                </label>
                <button className="btn primary">Pay</button>
            </form>
        </div>
    );
}
