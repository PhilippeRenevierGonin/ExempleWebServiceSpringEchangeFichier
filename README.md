C'est un exemple avec Spring de WebService qui reçoit un ou plusieurs fichiers. 
```
mvn clean package
mvn spring-boot:run@server 
# et dans un autre terminal
mvn spring-boot:run@client
```

# que fait le Client ? 

Il fait 5 requêtes, son code est le même selon que le serveur soir en RestController (RC) ou en Router+Handler (RH). Normal, c'est le traitement de la requête qui change, pas la requête. Les fichiers envoyés sont des ressources.   
1. il envoie un fichier (sur RH)
2. il envoie un fichier (sur RC) mais sur un chemin ne pouvant recevoir qu'un fichier
3. il envoie un fichier (sur RC) mais sur un chemin pouvant recevoir plusieurs fichiers
4. il envoie plusieurs fichiers (sur RH) sur le même chemin qu'en 1
5. il envoie plusieurs fichiers (sur RC) sur le même chemin qu'en 3

Il est aussi possible d'essayer via un formulaire web pour envoyer un ou plusieurs fichiers (c'est l'attribut "multiple" de l'input de type file qui change alors)


# que fait le serveur ? 

Il reçoit les fichiers, les place dans le dossier "courant" de l'exécution du programme. Il propose deux implémentations : une en paire Router / Handler, une autre en RestController. 

