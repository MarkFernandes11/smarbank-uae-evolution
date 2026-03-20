# smartbank-uae-evolution
<h2>Smart Bank Project</h2>

<p>This Project Documents my transition from Senior Java Developer to Cloud-Native Architect, evolving from a CLI tool to a Microservice platform.</p>

<h3>Phase 1 :</h3>
<h4>Branch name: feature-wallet-cli</h4>

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

<h3>Phase 2 :</h3>
<h4>Branch name: phase2/feature-db-connection</h4>

- Created a Postgre Connection Utility class to connect to the database.
- Removed the accounts hashmap and instead created a table in database.
- Added transactions database to store transactions of each user.
- Added accounts and transactions repository to commit data to database.
- Updated documentation of the methods in wallet service and account
- Refactored Account.java as a simple POJO and moved the logic to wallet service.
- Used BigDecimal for amount in transactions and account in place of double.
- TODO : Didn't check if there were any transactions made from the execute query and handle exceptions. 


<h3>Phase 3 :</h3>
<h4>Branch name: phase3/switch-to-spring-boot-api</h4>

- Added pom.xml, application.properties and Application runner to transform it from a CLI to Spring Boot Application. 
- Added annotations in <b>Account</b> and <b>Transaction</b> model classes to link them to db.
- Also added builders in model classes to avoid multiple different arg constructors.
- Added repositories for models to publish and fetch data from db.
- Removed Main.java, MenuHandler instead introduced controllers to communicate with server.
- Got tomcat server running with database initialized.
- Fixed and modified the logic for existing services to work with existing spring boot code structure.
- TODO : Exceptions to be handled globally to avoid 500 error codes, Documentation to be updated 