package ru.otus.hw.exceptions;

public class BookIsNotInCollectionException extends RuntimeException {
    public BookIsNotInCollectionException(String message) {
        super(message);
    }
}
