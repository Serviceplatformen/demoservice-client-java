## PURPOSE

The intent of this project is to:
1. Make it easy to verify the connection to [KOMBIT Serviceplatformen](https://www.serviceplatformen.dk), using either of its supported security models.
2. Serve as an example on how to implement and configure a client to KOMBIT Serviceplatformen for each of its supported
   security models.

## TARGET AUDIENCE

This file is targeted at Java developers who implement integrations to Serviceplatformen.

## INSTRUCTIONS

### PREREQUISITES

 * Java Development Kit 7 installed.
 * Apache Maven 3.2.5 or higher is installed.
 
The client is fully configured and should work as is. The client is tested with Java 7.

### EXECUTION

1. Run the client by executing `run.bat` or `run.sh`
2. Follow the instructions written in the console.

### BUILD

The following information should be sufficient in order to compile and run the source code, independently of the choice of IDE:

1. To build application, execute: `mvn clean install`
2. To run the application from command line, execute: `mvn spring-boot:run`

### SETUP INFORMATION

* Password for current keystore `wRFsRP63H3kNEhDU`

### CHANGE CERTIFICATE

It may be desirable for a new Serviceplatformen user system to verify that a connection is possible using their own certificate.
To replace the dedicated DemoService certificate with a different one, with another tool than Keystore-Explorer the steps will vary,
but for Keystore-Explorer do the following:

1. Open `src/main/resources/client.jks` for the relevant client with KeyStore Explorer or your favorite JKS manipulation tool.
2. Remove the existing private-public key pair.
3. Import your own key pair.
    3. Tools -> Import Key Pair
    
        ![alt tag](image/import.png)
    4. Choose the key type you are importing.
    
        ![alt tag](image/type.png)
        
    5. Set a password and make sure the password for the key pair is the same as the password for the entire Java KeyStore file.
    
        ![alt tag](image/password.png)
        
    6. If you have changed the client.jks password, set the new password in the following places:
        * In cxf.xml for both client types: 
            ```
            <http:conduit name="{http://serviceplatformen.dk/xml/wsdl/soap11/SP/Demo/1/}DemoPort.http-conduit">
                <http:tlsClientParameters>
                    <sec:keyManagers keyPassword="**REPLACE_PASSWORD**">
                        <sec:keyStore type="JKS" password="**REPLACE_PASSWORD**" resource="client.jks"/>
                    </sec:keyManagers>
                    <sec:trustManagers>
                        <sec:keyStore type="JKS" password="Test1234" resource="trust.jks"/>
                    </sec:trustManagers>
                </http:tlsClientParameters>
                <http:client AutoRedirect="true" Connection="Keep-Alive"/>
            </http:conduit>
            ```
        * In client.properties for token client only:
            ```
            org.apache.ws.security.crypto.merlin.keystore.password=**REPLACE_PASSWORD**
            ```    

7. Rebuild by running `mvn clean install`

## CONTENT
* `readme.md`: This file
* `run.sh`/`run.bat`: Runs the Java application
* `/demoservice-context-client`: Contains the source code, contracts and resources of the InvocationContext and AuthorityContext client.
* `/demoservice-token-client`: Contains the source code, contracts and resources of the token client.
- `image\*` - images used for this readme.