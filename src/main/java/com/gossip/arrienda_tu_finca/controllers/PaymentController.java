package com.gossip.arrienda_tu_finca.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.entities.Payment;
import com.gossip.arrienda_tu_finca.exceptions.PaymentNotFoundException;
import com.gossip.arrienda_tu_finca.services.PaymentService;

import lombok.AllArgsConstructor;


@RestController
@RequestMapping("/payment")
@AllArgsConstructor
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // Obtener el precio de renta para un pago específico
    @GetMapping("/{id}/rentalPrice")
    public ResponseEntity<Double> getRentalPrice(@PathVariable Long id) {
        Double rentalPrice = paymentService.getRentalPrice(id);
        return ResponseEntity.ok(rentalPrice);
    }

    // Actualizar un pago con los datos del formulario
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody PaymentDTO paymentDTO) {
        Payment updatedPayment = paymentService.updatePayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    // Listar todos los bancos disponibles
    @GetMapping("/banks")
    public ResponseEntity<List<String>> getAllBanks() {
        List<String> banks = paymentService.getAllBanks();
        return ResponseEntity.ok(banks);
    }

    // Obtener un pago relacionado con una solicitud de renta por su ID
    @GetMapping("/rentalRequest/{rentalRequestId}")
    public ResponseEntity<Payment> getPaymentByRentalRequestId(@PathVariable Long rentalRequestId) {
        Payment payment = paymentService.getPaymentByRentalRequestId(rentalRequestId);
        return ResponseEntity.ok(payment);
    }


}
