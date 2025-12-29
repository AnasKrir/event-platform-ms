export type Role = "USER" | "ORGANIZER" | "ADMIN";

export function getRoles(): Role[] {
  const raw = localStorage.getItem("roles");
  if (!raw) return [];
  try {
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? (parsed as Role[]) : [];
  } catch {
    return [];
  }
}

export function hasAnyRole(rolesToCheck: Role[]): boolean {
  const current = getRoles();
  return rolesToCheck.some((r) => current.includes(r));
}

export function isLoggedIn(): boolean {
  return Boolean(localStorage.getItem("token"));
}
