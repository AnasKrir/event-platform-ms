import { Navigate } from "react-router-dom";
import { hasAnyRole, isLoggedIn, type Role } from "../auth";
import type {JSX} from "react";

export function RequireAuth({ children }: { children: JSX.Element }) {
  if (!isLoggedIn()) return <Navigate to="/login" replace />;
  return children;
}

export function RequireRole({
  anyOf,
  children,
}: {
  anyOf: Role[];
  children: JSX.Element;
}) {
  if (!isLoggedIn()) return <Navigate to="/login" replace />;
  if (!hasAnyRole(anyOf)) return <Navigate to="/" replace />;
  return children;
}
