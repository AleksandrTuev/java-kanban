package exception;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(final String message, final IOException e){
        //16/01/24
        //нам необходимо чтобы переменные были неизменными на протяжении выполнения всего кода
        //если подменить текст в переменной message, то будет сложно найти ошибку

        //21/01/2024
        //Семён привет!
        //Ок.С материалом ознакомился.
        super(message, e);
    }
}
