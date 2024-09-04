package com.gossip.arrienda_tu_finca.exceptions;

public class InvalidTenantRentalRequestException extends RuntimeException  {
    public InvalidTenantRentalRequestException(String message) {
        super(message);
    }
}
