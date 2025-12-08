# Biblioteca Server/Client

## Implementato in collaborazione 
- **Youssouf A. Toure** -Backend/Server
- **Matteo De Patto** - Frontent/Client

## Funzionalità
- Login e registrazione degli utenti
- Ricerca di libri/articoli
- Prestito e restituzione articoli
- Aggiunta e rimozione articoli (solo admin)
- Gestione token e sessioni utente
- DTO response e request

## Tecnologie
- Java 17+
- Socket TCP/IP
- JSON con Jackson
- Maven

## Installazione
1. Clonare il repository https://github.com/youssouf994/youssouf994-BibliotecaDistribuita-
2. Separare l'app client dall'app server vedi cartelle
3. Aprire il progetto nell'IDE
4. Compilare e costruire il progetto
5. Avviare il server ( in caso di funzionamento non viene visuallizzato nessun messaggio)
  https://github.com/youssouf994/youssouf994-BibliotecaDistribuita-/blob/master/BibliotecaSideToSide/src/main/java/it/molinari/server/app/App.java
7. Avviare il client:


# DOCUMENTAZIONE LATO SERVER
<details>
  <summary>Struttura completa del progetto</summary>
<pre>
C:.
|   .classpath
|   .gitignore
|   .project
|   pom.xml
|
+---.settings
|       org.eclipse.jdt.core.prefs
|       org.eclipse.m2e.core.prefs
|
+---.vs
|   |   ProjectSettings.json
|   |   slnx.sqlite
|   |   VSWorkspaceState.json
|   |
|   \---serverBiblioteca
|       \---v16
+---src
|   +---main
|   |   \---java
|   |       \---it
|   |           \---molinari
|   |               \---server
|   |                   +---app
|   |                   |       App.java
|   |                   |
|   |                   +---enums
|   |                   |       ActionType.java
|   |                   |       HttpStatus.java
|   |                   |
|   |                   +---files
|   |                   |       archivio.json
|   |                   |       prestiti.json
|   |                   |       tokens.json
|   |                   |       users.json
|   |                   |
|   |                   +---model
|   |                   |       Cd.java
|   |                   |       Item.java
|   |                   |       ItemPrestato.java
|   |                   |       Libro.java
|   |                   |       Ricercato.java
|   |                   |       Rivista.java
|   |                   |       Token.java
|   |                   |       User.java
|   |                   |
|   |                   +---request
|   |                   |       LoginRequest.java
|   |                   |       RegistrationRequest.java
|   |                   |       Request.java
|   |                   |
|   |                   +---response
|   |                   |       GetBookRes.java
|   |                   |       LoginResponse.java
|   |                   |       RegistrationResponse.java
|   |                   |       Response.java
|   |                   |
|   |                   +---service
|   |                   |       GeneratoreJson.java
|   |                   |       GestioneCollezione.java
|   |                   |       GestioneConnessione.java
|   |                   |       GestioneJson.java
|   |                   |       GestorePrestiti.java
|   |                   |       Payload.java
|   |                   |       Registrazione.java
|   |                   |       Ricerca.java
|   |                   |
|   |                   +---tests
|   |                   |       TestGestConnessione.java
|   |                   |
|   |                   \---token
|   |                           Tokenizer.java
|   |
|   \---test
|       \---java
|           \---it
|               \---molinari
|                       AppTest.java
|
\---target
    +---classes
    |   +---it
    |   |   \---molinari
    |   |       \---server
    |   |           +---app
    |   |           |       App.class
    |   |           |
    |   |           +---enums
    |   |           |       ActionType.class
    |   |           |       HttpStatus.class
    |   |           |
    |   |           +---files
    |   |           |       archivio.json
    |   |           |       prestiti.json
    |   |           |       tokens.json
    |   |           |       users.json
    |   |           |
    |   |           +---model
    |   |           |       Cd.class
    |   |           |       Item.class
    |   |           |       ItemPrestato.class
    |   |           |       Libro.class
    |   |           |       Ricercato.class
    |   |           |       Rivista.class
    |   |           |       Token.class
    |   |           |       User.class
    |   |           |
    |   |           +---request
    |   |           |       LoginRequest.class
    |   |           |       RegistrationRequest.class
    |   |           |       Request.class
    |   |           |
    |   |           +---response
    |   |           |       GetBookRes.class
    |   |           |       LoginResponse.class
    |   |           |       RegistrationResponse.class
    |   |           |       Response.class
    |   |           |
    |   |           +---service
    |   |           |       GeneratoreJson.class
    |   |           |       GestioneCollezione.class
    |   |           |       GestioneConnessione.class
    |   |           |       GestioneJson.class
    |   |           |       GestorePrestiti.class
    |   |           |       Payload.class
    |   |           |       Registrazione.class
    |   |           |       Ricerca.class
    |   |           |
    |   |           +---tests
    |   |           |       TestGestConnessione.class
    |   |           |
    |   |           \---token
    |   |                   Tokenizer.class
    |   |
    |   \---META-INF
    |       |   MANIFEST.MF
    |       |
    |       \---maven
    |           \---it.molinari
    |               \---serverBiblioteca
    |                       pom.properties
    |                       pom.xml
    |
    +---generated-sources
    |   \---annotations
    +---maven-status
    |   \---maven-compiler-plugin
    |       \---compile
    |           \---default-compile
    |                   createdFiles.lst
    |                   inputFiles.lst
    |
    \---test-classes
        \---it
            \---molinari
                    AppTest.class
</pre>


</details>




