@echo OFF
echo Please choose a client to execute:
echo 1 - Context client
echo 2 - Token client
echo 3 - Access Token client
Set /p option=
if %option% == 1 (
  mvn clean install spring-boot:run -f demoservice-context-client
  ) 
)
if %option% == 2 (
 mvn clean install spring-boot:run -f demoservice-token-client
  )
  
if %option% == 3 (
 mvn clean install spring-boot:run -f demoservice-accesstoken-client
  )

