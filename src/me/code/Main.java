import me.code.commands.CreateTransactionCommand;
import me.code.commands.DeleteTransactionCommand;
import me.code.commands.ListTransactionsCommand;
import me.code.commands.ShowBalanceCommand;
import me.code.repositories.FileTransactionRepository;
import me.code.services.DefaultTransactionService;
import me.code.services.ICommandService;
import me.code.services.ITransactionService;
import me.code.services.TerminalCommandService;


void main() {

    ICommandService commandService = new TerminalCommandService();
    ITransactionService transactionService = new DefaultTransactionService(
            new FileTransactionRepository()
    );


    commandService.registerCommand(new CreateTransactionCommand(transactionService));
    commandService.registerCommand(new DeleteTransactionCommand(transactionService));
    commandService.registerCommand(new ListTransactionsCommand(transactionService));
    commandService.registerCommand(new ShowBalanceCommand(transactionService));

    if (commandService instanceof TerminalCommandService service) {
        service.start();
    }
}

