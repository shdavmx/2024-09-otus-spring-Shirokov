package ru.otus.hw.exceptions;

public class BookAlreadyExistInCollectionException extends RuntimeException {
    public BookAlreadyExistInCollectionException(String message) {
        super(message);
    }
}
