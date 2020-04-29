package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

@EXClassNullAway
public class ProcessUtils {
    public static int executeCommand(String command, long timeout) throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command);
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null) {
                int intValue = worker.exit.intValue();
                process.destroy();
                return intValue;
            }
            throw new TimeoutException();
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        } catch (Throwable th) {
            process.destroy();
            throw th;
        }
    }

    private static class Worker extends Thread {
        /* access modifiers changed from: private */
        public Integer exit;
        private final Process process;

        private Worker(Process process2) {
            this.process = process2;
        }

        public void run() {
            try {
                this.exit = Integer.valueOf(this.process.waitFor());
            } catch (InterruptedException e) {
            }
        }
    }

    public static boolean isReachable(String host, int timeout) throws UnknownHostException, IOException {
        return InetAddress.getByName(host).isReachable(timeout);
    }
}
