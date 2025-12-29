export type AuthResponse = { token: string; userId: number; email: string; roles: string[] };

export type Event = {
    id: number;
    title: string;
    description?: string;
    location?: string;
    organizer: string;
    participantA: string;
    participantB: string;
    startAt: string;
    ticketPrice: number;
    totalTickets: number;
    remainingTickets: number;
};

export type Reservation = {
    id: number;
    eventId: number;
    userId: number;
    quantity: number;
    unitPrice: number;
    totalPrice: number;
    status: "PENDING_PAYMENT" | "PAID" | "CANCELED";
    createdAt: string;
};

export type Payment = {
    id: number;
    reservationId: number;
    userId: number;
    amount: number;
    status: "INITIATED" | "SUCCESS" | "FAILED";
    method: string;
    createdAt: string;
};
