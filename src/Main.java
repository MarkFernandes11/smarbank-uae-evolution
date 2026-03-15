import main.java.com.bank.api.service.WalletService;
import main.java.com.bank.api.ui.MenuHandler;

public class Main {
    public static void main(String[] args) {
        WalletService service = new WalletService();
        MenuHandler menuHandler = new MenuHandler();
        menuHandler.run(service);
    }

}