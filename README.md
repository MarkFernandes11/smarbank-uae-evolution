# smartbank-uae-evolution
Smart Bank Project

This Project Documents my transition from Senior Java Developer to Cloud-Native Architect, evolving from a CLI tool to a Microservice platform.

Phase 1 :

- Created a CLI application that will be able to initialize an account, deposit, withdraw, get transaction history and check balance from the Wallet.
- Exception handled in case of insufficient balance.
- Input values handled in scanner to select a menu option or to enter amount.
- Added options in account menu to maintain multiple accounts, login to an account and list all accounts.
- Created a wallet service and moved all accounts related logic to it.
- Used a transaction record instead of a string to maintain transactions.
- Updated the project structure to move the code from Main to a Menu Handler.
- In account added the functionality to transfer funds and handled exceptions for it.
- TODO : Need to create account with a unique ID and need to handle concurrency for Map 

How to Run : First execute -> javac Main.java followed by -> java Main

Phase 2 :

- Created a Postgre Connection Utility class to connect to the database.
- Removed the accounts hashmap and instead created a table in database.
- Added transactions database to store transactions of each user.
- Added accounts and transactions repository to commit data to database.