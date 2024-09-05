package com.gossip.arrienda_tu_finca.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gossip.arrienda_tu_finca.dto.PaymentDTO;
import com.gossip.arrienda_tu_finca.entities.Payment;
import com.gossip.arrienda_tu_finca.entities.TenantRentalRequest;
import com.gossip.arrienda_tu_finca.exceptions.PaymentNotFoundException;
import com.gossip.arrienda_tu_finca.repositories.PaymentRepository;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    //Obtener precio de renta
    public Double getRentalPrice(Long id) {
        return paymentRepository.findById(id)
                .map(Payment::getRentalPrice)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found by id"));
    }

    //Llenar formulario de pago
    public Payment updatePayment(Long id, PaymentDTO paymentDTO) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment to update not found by id"));

        Double rentalPrice = getRentalPrice(id);
        
        payment.setRentalPrice(rentalPrice);
        payment.setBank(paymentDTO.getBank());
        payment.setAccountNumber(paymentDTO.getAccountNumber());

        Payment updatedPayment = paymentRepository.save(payment);
        TenantRentalRequest rentalRequest = payment.getRentalRequest();
        rentalRequest.setPaid(true); //Cambiar estado de Paid en TenantRentalRequest
    
        return updatedPayment;

    }
    
    //Listar bancos
    public List<String> getAllBanks() {
        return List.of("Banco de Bogotá", "Bancolombia", "BBVA", "Davivienda", "Banco Popular", "Itau", "Banco de Occidente", "Banco AV Villas");
    }

    //Obtener pago de una renta
    public Payment getPaymentByRentalRequestId(Long rentalRequestId) {
        return paymentRepository.findByRentalRequestId(rentalRequestId)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found by rental request id"));
    }
}
