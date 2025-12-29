import { Routes, Route, Link, useNavigate } from "react-router-dom";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Events from "./pages/Events";
import Reserve from "./pages/Reserve";
import Pay from "./pages/Pay";
import My from "./pages/My";
import AdminDashboard from "./pages/AdminDashboard";
import ManageEvents from "./pages/ManageEvents";
import EventEditor from "./pages/EventEditor";
import { getRoles, hasAnyRole } from "./auth";
import { RequireRole } from "./components/Guards";

export default function App() {
  const nav = useNavigate();
  const token = localStorage.getItem("token");
  const roles = getRoles();

  const logout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("roles");
    localStorage.removeItem("userId");
    nav("/login");
  };

  const canManage = token && hasAnyRole(["ORGANIZER", "ADMIN"]);
  const isAdmin = token && hasAnyRole(["ADMIN"]);

  return (
    <div className="container">
      <header className="topbar">
        <Link to="/" className="brand">
          Event Platform
        </Link>
        <nav className="nav">
          <Link to="/">Events</Link>
          {token && <Link to="/my">My</Link>}
          {canManage && <Link to="/organizer/events">Organizer</Link>}
          {isAdmin && <Link to="/admin">Admin</Link>}
          {!token && <Link to="/login">Login</Link>}
          {!token && <Link to="/register">Register</Link>}
          {token && (
            <button className="btn" onClick={logout}>
              Logout
            </button>
          )}
        </nav>
      </header>

      {token && roles.length > 0 && (
        <p className="muted" style={{ marginTop: 12 }}>
          Connected as: <b>{roles.join(", ")}</b>
        </p>
      )}

      <Routes>
        {/* USER */}
        <Route path="/" element={<Events />} />
        <Route path="/reserve/:eventId" element={<Reserve />} />
        <Route path="/pay/:reservationId" element={<Pay />} />
        <Route path="/my" element={<My />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

        {/* ORGANIZER / ADMIN */}
        <Route
          path="/organizer/events"
          element={
            <RequireRole anyOf={["ORGANIZER", "ADMIN"]}>
              <ManageEvents />
            </RequireRole>
          }
        />
        <Route
          path="/organizer/events/new"
          element={
            <RequireRole anyOf={["ORGANIZER", "ADMIN"]}>
              <EventEditor />
            </RequireRole>
          }
        />
        <Route
          path="/organizer/events/:id/edit"
          element={
            <RequireRole anyOf={["ORGANIZER", "ADMIN"]}>
              <EventEditor />
            </RequireRole>
          }
        />

        {/* ADMIN */}
        <Route
          path="/admin"
          element={
            <RequireRole anyOf={["ADMIN"]}>
              <AdminDashboard />
            </RequireRole>
          }
        />
      </Routes>
    </div>
  );
}
