import com.bank.services.WalletService;
import com.bank.ui.MenuHandler;

public class Main {
    public static void main(String[] args) {
        WalletService service = new WalletService();
        MenuHandler menuHandler = new MenuHandler();
        menuHandler.run(service);
    }

}