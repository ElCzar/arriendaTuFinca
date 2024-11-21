package com.gossip.arrienda_tu_finca.exceptions;

public class JWTNotGeneratedException extends RuntimeException {
    public JWTNotGeneratedException(String message) {
        super(message);
    }
}
