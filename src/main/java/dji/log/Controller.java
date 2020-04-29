package dji.log;

public class Controller {
    public static final int WARN_CONSOLE = 1;
    public static final int WARN_NONE = 0;
    public static final int WARN_TOAST = 2;
    boolean encrypt = false;
    boolean print = true;
    int priority = 3;
    boolean save = true;
    int warn = 0;

    Controller() {
    }

    public Controller priority(int priority2) {
        this.priority = priority2;
        return this;
    }

    public Controller print(boolean print2) {
        this.print = print2;
        return this;
    }

    public Controller save(boolean save2) {
        this.save = save2;
        return this;
    }

    public Controller encrypt(boolean encrypt2) {
        this.encrypt = encrypt2;
        return this;
    }

    public Controller warn(int warn2) {
        this.warn = warn2;
        return this;
    }
}
