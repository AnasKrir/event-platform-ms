import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API } from "../api";
import type { AuthResponse } from "../types";

export default function Login() {
    const nav = useNavigate();
    const [email, setEmail] = useState("admin@event.com");
    const [password, setPassword] = useState("Admin123!");
    const [err, setErr] = useState("");

    const submit = async (e: any) => {
        e.preventDefault();
        setErr("");
        try {
            const r = await API.request<AuthResponse>("/api/auth/login", {
                method: "POST",
                body: JSON.stringify({ email, password }),
            });
            localStorage.setItem("token", r.token);
            localStorage.setItem("roles", JSON.stringify(r.roles));
            localStorage.setItem("userId", String(r.userId));
            nav("/");
        } catch (ex: any) {
            setErr(ex.message);
        }
    };

    return (
        <div className="card">
            <h2>Login</h2>
            {err && <p className="error">{err}</p>}
            <form onSubmit={submit} className="form">
                <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="email" />
                <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="password" type="password" />
                <button className="btn primary">Login</button>
            </form>
            <p className="muted">Admin par défaut : admin@event.com / Admin123!</p>
        </div>
    );
}
