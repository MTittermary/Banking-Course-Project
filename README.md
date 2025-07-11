# Banking System

A comprehensive Java-based banking system that supports multiple account types, transaction processing, and command validation.

## Overview

This banking system provides an interface for managing bank accounts with support for checking, savings, and certificate of deposit (CD) accounts. The system processes commands for account creation, deposits, withdrawals, transfers, and time progression while maintaining transaction history and enforcing business rules.

## Features

- **Multiple Account Types**: Checking, Savings, and CD accounts with different rules and limits
- **Transaction Processing**: Deposits, withdrawals, and transfers between accounts
- **Command Validation**: Comprehensive validation of all commands before processing
- **Time Progression**: Simulate months passing with interest calculations and account maintenance
- **Transaction History**: Track all transactions for each account
- **Account Management**: Automatic removal of zero-balance accounts

## Account Types

### Checking Account
- **Deposit Limit**: $1,000 per transaction
- **Withdrawal Limit**: $400 per transaction
- **Monthly Withdrawals**: Unlimited
- **Interest**: Applied monthly

### Savings Account
- **Deposit Limit**: $2,500 per transaction
- **Withdrawal Limit**: $1,000 per transaction
- **Monthly Withdrawals**: 1 per month
- **Interest**: Applied monthly

### Certificate of Deposit (CD)
- **Initial Deposit**: $1,000 - $10,000 (required at creation)
- **Additional Deposits**: Not allowed
- **Withdrawals**: Only full balance after 12 months
- **Interest**: Applied quarterly (4 times per month)

## Architecture

The system follows a modular design with clear separation of concerns:

### Core Components

- **`Bank`**: Central repository for accounts and transaction history
- **`BankAccount`**: Abstract base class for all account types
- **`MasterControl`**: Main orchestrator that coordinates validation, processing, and output

### Command Processing

- **`CommandValidator`**: Validates commands before processing
- **`CommandProcessor`**: Routes valid commands to appropriate processors
- **`CommandStore`**: Stores invalid commands for reporting

### Validation Framework

- **`AbstractCommandValidator`**: Base class for all command validators
- Specific validators for each command type (Create, Deposit, Withdraw, Transfer, PassTime)

### Processing Framework

- **`AbstractCommandProcessor`**: Base class for all command processors
- Specific processors for each command type

## Supported Commands

### Create Account
```
create checking <8-digit-id> <apr>
create savings <8-digit-id> <apr>
create cd <8-digit-id> <apr> <initial-balance>
```

### Deposit Money
```
deposit <account-id> <amount>
```

### Withdraw Money
```
withdraw <account-id> <amount>
```

### Transfer Money
```
transfer <from-account-id> <to-account-id> <amount>
```

### Pass Time
```
pass <months>
```

## Business Rules

### Account Creation
- Account IDs must be exactly 8 digits
- APR must be between 0% and 10%
- CD accounts require initial balance between $1,000 and $10,000

### Deposits
- Cannot deposit to CD accounts after creation
- Amount cannot exceed account-specific deposit limits

### Withdrawals
- Cannot withdraw more than account balance
- Cannot exceed account-specific withdrawal limits
- Savings accounts limited to 1 withdrawal per month
- CD accounts can only be withdrawn in full after 12 months

### Transfers
- Only between checking and savings accounts
- Subject to both withdrawal and deposit limits
- Counts toward savings account monthly withdrawal limit

### Time Progression
- Interest calculated monthly (quarterly for CDs)
- Accounts with balance < $100 charged $25 fee
- Zero-balance accounts automatically closed
- Monthly withdrawal limits reset

## Output Format

The system generates output in the following order:
1. Account information (in creation order) for open accounts
2. Transaction history for each account
3. Invalid commands that were rejected

Account format: `<Type> <ID> <Balance> <APR>`

## Example Usage

```java
Bank bank = new Bank();
CommandValidator validator = new CommandValidator(bank);
CommandProcessor processor = new CommandProcessor(bank);
CommandStore store = new CommandStore();
MasterControl control = new MasterControl(validator, processor, store, bank);

List<String> commands = Arrays.asList(
    "create checking 12345678 1.5",
    "deposit 12345678 500",
    "withdraw 12345678 100"
);

List<String> output = control.start(commands);
```

## Error Handling

The system validates all commands before processing:
- Invalid commands are stored and reported in the output
- Account operations are only performed on valid, existing accounts
- All numeric inputs are validated for proper format and ranges

## Dependencies

- Java 8 or higher
- No external dependencies required

## File Structure

```
banking/
├── AbstractCommandProcessor.java
├── AbstractCommandValidator.java
├── Bank.java
├── BankAccount.java
├── CheckingAccount.java
├── SavingsAccount.java
├── CdAccount.java
├── CommandProcessor.java
├── CommandValidator.java
├── CommandStore.java
├── MasterControl.java
├── OutputGenerator.java
├── CreateAccountProcessor.java
├── CreateCommandValidator.java
├── DepositProcessor.java
├── DepositCommandValidator.java
├── WithdrawProcessor.java
├── WithdrawCommandValidator.java
├── TransferProcessor.java
├── TransferCommandValidator.java
├── PassTimeProcessor.java
└── PassTimeCommandValidator.java
```

## Contributing

When extending the system:
1. Follow the existing pattern of separate validation and processing classes
2. Extend `AbstractCommandValidator` for new command validators
3. Extend `AbstractCommandProcessor` for new command processors
4. Update `CommandValidator` and `CommandProcessor` to include new commands
5. Add comprehensive validation for all inputs
