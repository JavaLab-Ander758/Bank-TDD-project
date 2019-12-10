# ITE-1901 2019 TDD Project

TDD Project in ITE1901 course. Developed program is a Bank application simulator that writes and reads transfers from file.

## Project Description

`Bank` class is the main logic driver for this program. It uses the other classes in this project to simulate a bank application. Using `Account` class to represent a bank account it keeps control of who owns a given account, as well as the balance of account. `Transfer` class represents a money transfer from one account to the other. `Bank` class uses also two classes for transfer handling, a `TransferWriter` and a `TransferReader` class. Those classes are used to split each transfer into two parts. They are as follows:

* Firstly `Bank.newTransfer` method creates a `Transfer` object, it gets in two account numbers as arguments, along with transfer amount. It then checks if both accounts exist and if the transfer amount is legal. If everything is in order it calls on `TransferCreator.writeTransfer` method which writes the transfer into a CSV file, and returns `true` if the transfer was legal, and was successfully written to file. Otherwise it throws an exception.
* When bank is ready to commit pending transfers it executes them using `Bank.executePendingTransfers` method. It gets all pending transfers using `TransferReader.readTransfers` method. This method reads all transfers in file and saves them as `Transfer` objects to a container, and clears the contents of the file. `Bank` can then execute all of the transfers in order, returning `true` if everything succeeded, or throw and exception otherwise.

All errors in reading from or writing to file won't throw an exception, instead they will write an error to file with useful information about the error. As such only errors with transactions will throw any exceptions.

`Bank`class is also able to create a new bank account. Rules for all bank accounts are as follows:

* Account holder name must be legal, this means that it must be a non empty `String`. It also needs to contain two or three words in it that represent first name, middle name, and last name. Middle name is optional.
* Start balance must be a non-negative `double` value.
* Account number is generated randomly, but it follows a few rules:
  * It must be 9 digits long.
  * It must be an even number.
  * It needs to start with 4.

`Bank.newTransfer` and `Bank.executePendingTransfers` check if the account number they got exist. These methods don’t need to check legality of the account number directly, in can be done indirectly in for example `TransferReader`, it’s up to you how you do it.

`Transfer File`is a CSV file with a following column structure:

> `SenderAccountID,ReceiverAccountID,Amount,TransferDate`

So a row in `TransferFile` can look as follows.

> `423879562,471091284,142.45,2019-10-17 12:34:12`

As the `TransferFile` only contains an account number it’s necessary to get a hold of an `Account` object in order to create a `Transfer` file, this can be done using  `Bank::getAccountByID` method. If there’s no account in the bank with a given account number it will be written to `Error Log File`. An `Error Log File` is a normal plain text file containing relevant error information, such as where the error has occurred, what went wrong, and a copy of data that caused the error to occur.

## Installation

Download the project and put it anywhere on your computer. Open it in your preferred IDE, for example [IntelliJ IDEA](https://www.jetbrains.com/idea/). Run the application file.

### Dependencies

This project depends on several testing libraries.

Currently used testing libraries are as follows:

* [Junit Jupiter](https://junit.org/junit5/) testing library. Maven link: `org.junit.jupiter:junit-jupiter:5.5.1`
* [Hamcrest](http://hamcrest.org/) matcher library. Maven link: `org.hamcrest:hamcrest:2.1`
* [Mockito](https://site.mockito.org/) mocking library. Maven link: `org.mockito:mockito-core:3.0.0`
* [Mockito Jupiter](https://site.mockito.org/)  integration library. Maven link: `org.mockito:mockito-junit-jupiter:3.0.0`


Here’s an UML diagram showing how the project hangs together:

![Package bank diagram.png](https://github.com/JavaLab-Ander758/Bank-TDD-project/blob/master/README.assets/Package%20bank%20diagram.png)
