package com.movie.ticket.booking.system.payment.service.services;

import com.movie.ticket.booking.system.payment.service.dtos.BookingDTO;

public interface PaymentService {
    public BookingDTO makePayment(BookingDTO bookingDTO);
}
