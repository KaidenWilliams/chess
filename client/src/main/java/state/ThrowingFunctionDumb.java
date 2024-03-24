package state;

import exceptionclient.ClientException;

// THis literally exist just so my methods in my commandMethods map will not error
public interface ThrowingFunctionDumb<T, R> {
    R apply(T t) throws ClientException;
}