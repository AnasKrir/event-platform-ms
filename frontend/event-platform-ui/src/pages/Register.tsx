import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { API } from "../api";
import type { AuthResponse } from "../types";

export default function Register() {
    const nav = useNavigate();
    const [fullName, setFullName] = useState("User");
    const [email, setEmail] = useState("user@mail.com");
    const [password, setPassword] = useState("secret12");
    const [role, setRole] = useState<"USER" | "ORGANIZER">("USER");
    const [err, setErr] = useState("");

    const submit = async (e: any) => {
        e.preventDefault();
        setErr("");
        try {
            const r = await API.request<AuthResponse>("/api/auth/register", {
                method: "POST",
                body: JSON.stringify({ fullName, email, password, role }),
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
            <h2>Register</h2>
            {err && <p className="error">{err}</p>}
            <form onSubmit={submit} className="form">
                <input value={fullName} onChange={(e) => setFullName(e.target.value)} placeholder="full name" />
                <input value={email} onChange={(e) => setEmail(e.target.value)} placeholder="email" />
                <input value={password} onChange={(e) => setPassword(e.target.value)} placeholder="password" type="password" />
                <select value={role} onChange={(e) => setRole(e.target.value as any)}>
                    <option value="USER">USER</option>
                    <option value="ORGANIZER">ORGANIZER</option>
                </select>
                <button className="btn primary">Create account</button>
            </form>
        </div>
    );
}
