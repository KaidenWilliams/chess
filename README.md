# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`     | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

### Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

- My Phase2 URL: 
https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWOZVYSnfoccKQCLAwwAIIgQKAM4TMAE0HAARsAkoYMhZkwB6LQEZMAcygQArthgBiNMCoBPGACUUBpBLBRBSCGksB3ABZIYByIqKQAtAB8LNSUAFwwANoACgDyZAAqALowWiYqUAA6aADe+ZTWALYoADQwuFK+0DJ1KJXASAgAvpgUsbBRMWziCVDOrsFQABTlUFW19coSTVAtMG0dCACUvazsMIMCQqLiUgkGKGAAqgUzBfM7RyJikhIH0eoJZACiADLfcAyMFm8xgADNjJVgQVME8Tq8DoM+sMUAk0CYEAhdtR9od5PCzjAQGNBCgbpQ7hVgNU6g1ls1HviXlJ3moFAkAJIAOR+DiBIOpCzpKzWG06MG5GVS0NoyNx0ThzIkCWJKFJwhMYH8lLmgsZxyVrM+Ep53z5Mt1NJgwE1-gyEAA1ugTVLrbbYUzTm9wki9iM3Vr7U60NiUYjosj4gG7Y70KHKOH4Mh0GAEgAmAAMGeKJRtgdjaB66Bkmm0WjThmMZks0F4FxgvwgLh8FgCQRCyYiEdYUZS6WyuRUUi8aBzAqtwuaPUjA27OP9CCbSDQOvmtKWIp2cvErMVXvOl0bzdXgvXjQZHoNXqN7Pg-hQIAdMAAsoIQP4LWvFufVuDIRbL2ea8fTnFEEgyKATA4bdVDxK9XgSOASWCDUtRPapHkAglvQ+W8OTQApmDzGNgxgZdIBgVD-CeGAAkoVRx1UABeT9BSww0QKGdgEmIoM4xg8MZx420+JDGdw1CFN0yzGBcxEgsek0YtSx0ABmStTHMCwjBQZ0j1MZhW0CYJMEkrshl7Pg-m+DJvhyLQhwkEdil4gtNHE3151RGBFwMAyplc4Mtz9WCFU9BC1BQBBLhQUSAvkoL2OA3CEhkKKYujUS-wgKEqJouixkygsYBYwK4z3BFOJghIJBMJVQ1xKIhJgWr6o8qIzNTGBMwzJS0BLTQdAAFg06sLDGGQGwmGAAHFBTeIz21MztmE8mgoASRIZps+yDEFFyErjeNZy4hcJjm6oJHi-NEoEuCgIii5rnuQVrpI9B9QellBmNJ7WOqbLcttfL70KsqfFKw6Qwq761u4lq6q9Bqd3u7CDzAC6UHQlBPuwm90etTEYD2y7AdmwUniSyq4f9EoSckTa6cFDk+DqNtgnJS0FgUBBQAdTmv3prlBS6LJpxCwSew22T6eVJImeqFm2eMskXqtHm+YF09icFYXqlF8X+gklbNp6nIFYZ+X6aV2iVa19Xecfe2FiFkWxcwZTBq0ABWUatOwEwoGwaL4GQ1RMb8FXlrCVbQN7NJMl2-brChnNXf10sPNA+HVVJTG3tEup05x5HQpgGG5ae52C4LXGOJS4nLn+1QIRyyjgfkW36KK0jIZu8rwth7P-VapG7siCuVTD-Pi7r5K2UQsPyYB5dl5QJ5WnW2wtWXAwYHRImUAAD3EbBmGtvhrX6nXqj1jgK8TaqZeZvhDbDEDmotlm34TEDOtNrM5sL6KQ9v1FSWgABsftLC2CiouXwMAABSEBV4RwsBrR80cUysmaikK4A4tD0wOv3UcpROpwAgIuKARcX6KSzqdbyAArFBaB87gzqCAFaFCqE0MVnwYKXldyD0rpcau4M56VQbn9RiZM8qdwKqocGJUe4D3gkPBhNVEaVRgGMMAgcCLN1Lu8SejcMavQvhI76kRfqXDePTGACh7AXypkPJ+o9XhGKiCY5hy5MbJF5rA6YF86iMTqOwokK0QCUOgFsA4ZEwQRJjtw6ALicIL2tDISaMjlCySiVQrozsyI+ExpTB+H8QqaLahLLxwiEg+NYYKMg2AHxCEgEEl+IS1YLHYbE8I8T94QGYJwpJ0SoCpPxhkrJXTrRvAkM08AggUllJpt5dxUhPFNSlpUseUtjYxwARmLIoCBplgAOzQO0h4ewiA1SwGANgYOhBPDeEjktTqOCtlJCsv8Wy9l1DuV2Ss1K6UUKYimDsTxYU1FyzStFDmBQnjgvGT9W8sKMoACoyac1KcIx+FSEZVMEajJUwK4UoBKfIJFZSG5ouCDATFrcoQUqEMioFBKdlEqhV9GFIKUByKEFS3FKLSUYtkR3Flyzh6rK0eszO1TNn9G2R49qkR-5JDNqWZSQA
