package com.ilkan.domain.pay.controller;

import com.ilkan.domain.pay.dto.ReservationPayReqDto;
import com.ilkan.domain.pay.dto.ReservationPayResDto;
import com.ilkan.domain.pay.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/credit/spaces")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/{reservationId}")
    public ResponseEntity<ReservationPayResDto> payReservation(
            @PathVariable Long reservationId,
            @RequestBody ReservationPayReqDto req) {
        ReservationPayResDto response = paymentService.processFakePayment(reservationId, req);
        return ResponseEntity.ok(response);
    }

}
