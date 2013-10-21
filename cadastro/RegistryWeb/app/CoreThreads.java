import br.pucrio.inf.lac.login.LoginCoreClient;
import br.pucrio.inf.lac.login.LoginCoreServer;
import br.pucrio.inf.lac.registry.RegistryCoreClient;
import br.pucrio.inf.lac.registry.RegistryCoreServer;

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

	private static class StartLoginCoreServer implements Runnable {
		public void run() {
			new LoginCoreServer();
		}
	}

	private static class StartLoginCoreClient implements Runnable {
		public void run() {
			new LoginCoreClient();
		}
	}

	public static void initialize() {
//		StartRegistryCoreServer coreserver = new StartRegistryCoreServer();
//		Thread coreserver_thread = new Thread(coreserver);
//		System.out.print("Iniciando thread do RegistryCoreServer... ");
//		coreserver_thread.start();
//		System.out.println("Pronto.");

//		StartRegistryCoreClient coreclient = new StartRegistryCoreClient();
//		Thread coreclient_thread = new Thread(coreclient);
//		System.out.print("Iniciando thread do RegistryCoreClient... ");
//		coreclient_thread.start();
//		System.out.println("Pronto.");

		StartLoginCoreServer coreserverLogin = new StartLoginCoreServer();
		Thread coreserver_thread_login = new Thread(coreserverLogin);
		System.out.print("Iniciando thread do LoginCoreServer... ");
		coreserver_thread_login.start();
		System.out.println("Pronto.");

//		StartLoginCoreClient coreclientLogin = new StartLoginCoreClient();
//		Thread coreclient_thread_Login = new Thread(coreclientLogin);
//		System.out.print("Iniciando thread do LoginCoreClient... ");
//		coreclient_thread_Login.start();
//		System.out.println("Pronto.");
	}
}
