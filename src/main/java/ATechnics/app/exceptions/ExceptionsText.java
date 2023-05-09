package ATechnics.app.exceptions;

public enum ExceptionsText {
    FILE_NOT_FOUND("Файл не найден!"),
    ERROR_CLOSING_STREAM("Ошибка закрытия потока!"),
    TASK_NOT_FOUND("Указанный task не найден!");
    private String msg;

    ExceptionsText(String msg) {
        this.msg = msg;
    }
}