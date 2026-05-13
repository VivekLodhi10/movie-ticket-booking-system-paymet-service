package com.movie.ticket.booking.system.payment.service.services.impl;

import com.movie.ticket.booking.system.payment.service.dtos.BookingDTO;
import com.movie.ticket.booking.system.payment.service.dtos.BookingStatus;
import com.movie.ticket.booking.system.payment.service.entities.PaymentEntity;
import com.movie.ticket.booking.system.payment.service.entities.PaymentStatus;
import com.movie.ticket.booking.system.payment.service.repositories.PaymentRepository;
import com.movie.ticket.booking.system.payment.service.services.PaymentService;
import com.movie.ticket.booking.system.payment.service.services.StripeApiPaymentGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StripeApiPaymentGateway stripeApiPaymentGateway;

    @Override
    @Transactional
    public BookingDTO makePayment(BookingDTO bookingDTO) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .bookingId(bookingDTO.getBookingId())
                .paymentStatus(PaymentStatus.PENDING)
                .paymentAmount(bookingDTO.getBookingAmount())
                .build();
        this.paymentRepository.save(paymentEntity);
        bookingDTO = this.stripeApiPaymentGateway.processPayment(bookingDTO);
        if (bookingDTO.getBookingStatus().equals(BookingStatus.CONFIRMED)){
            paymentEntity.setPaymentStatus(PaymentStatus.APPROVED);
            paymentEntity.setPaymentDateTime(LocalDateTime.now());
//            paymentRepository.save(paymentEntity); //use transactional
        }else {
            paymentEntity.setPaymentStatus(PaymentStatus.FAILED);
            paymentEntity.setPaymentDateTime(LocalDateTime.now());
//            paymentRepository.save(paymentEntity); //use transactional
        }
        return bookingDTO;
    }
}
