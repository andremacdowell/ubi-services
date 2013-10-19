import play.*;

public class Global extends GlobalSettings {
    public void onStart(Application app) {
        System.out.println("Inicializando servidor e cliente...");
        CoreThreads.initialize();
    }
}
