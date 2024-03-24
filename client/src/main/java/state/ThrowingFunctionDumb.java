package state;

import exceptionclient.ClientException;

public interface ThrowingFunctionDumb<T, R> {
    R apply(T t) throws ClientException;
}