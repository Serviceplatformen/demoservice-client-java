#!/bin/bash
echo "Please choose a client to execute:"
echo "1 - Context client"
echo "2 - Token client"
read option
if [ "$option" == "1" ]; then
  mvn clean install spring-boot:run -f demoservice-context-client
elif [ "$option" == "2" ]; then
  mvn clean install spring-boot:run -f demoservice-token-client
fi
