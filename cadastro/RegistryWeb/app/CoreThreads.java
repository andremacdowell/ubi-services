import br.pucrio.inf.lac.registry.*;

public class CoreThreads {
    private static class StartRegistryCoreServer implements Runnable {
        public void run() {
            new RegistryCoreServer();
        }
    }

    private static class StartRegistryCoreClient implements Runnable {
        public void run() {
            new RegistryCoreClient();
        }
    }

    public static void initialize() {
        StartRegistryCoreServer coreserver = new StartRegistryCoreServer();
        Thread coreserver_thread = new Thread(coreserver);
        System.out.print("Iniciando thread do RegistryCoreServer... ");
        coreserver_thread.start();
        System.out.println("Pronto.");

//        StartRegistryCoreClient coreclient = new StartRegistryCoreClient();
//        Thread coreclient_thread = new Thread(coreclient);
//        System.out.print("Iniciando thread do RegistryCoreClient... ");
//        coreclient_thread.start();
//        System.out.println("Pronto.");
    }
}
