package exception;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, final IOException e){
        //нам необходимо чтобы переменные были неизменными на протяжении выполнения всего кода
        //если подменить текст в переменной message, то будет сложно найти ошибку
        super(message, e);
    }
}
