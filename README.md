# Notes

## Default configurations
- If you want to start the spring boot app, the project will run with PersonRepositorySource = File as default
- To run the project: in the Greetings-Reminders-project folder, you can run in terminal: java -jar greetings-reminders-0.0.1-SNAPSHOT.jar
- The application will start and execute BirthdayGreetingUseCase and BirthdayReminderUseCase (just for testing and didactics purposes)
- Sql table will be populated with default data, see scripts in import.sql file

## Switch to SQLite/File repo
- Run in terminal: java -jar greetings-reminders-0.0.1-SNAPSHOT.jar --person.repository.source=SQLite (or File)
- You will see in logs "Receiving source from config: SQLite" or "Receiving source from config: File" depending on your selection

## Database config
- To change the database config, you can run in terminal: java -jar greetings-reminders-0.0.1-SNAPSHOT.jar "spring.datasource.url=jdbc:sqlite:{your/database/directory}?date_string_format=yyyy-MM-dd" --person.repository.source=SQLite
Please add the "date_string_format=yyyy-MM-dd" due to SQLite limitations

## File config
- Add your custom file in resources folder and run in terminal: java -jar greetings-reminders-0.0.1-SNAPSHOT.jar person.repository.file.directory=friend-list.txt
The format is: last_name, first_name, date_of_birth ("-" separated), phone_number. Headers are not needed.
e.g.:
Doe, John, 1982-10-08, john.doe@foobar.com, +5901233

## Test cases
- Into the project folder you can run in terminal: mvn test. You will see logs results for File and SQLite repositories.
- Person table is populated with the same import.sql script
- File repository for test's is in test/resources folder

