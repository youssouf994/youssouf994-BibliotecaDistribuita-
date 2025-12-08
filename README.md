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

## Esempi di utilizzo

### Log di client non autenticato manda request:
## Esempio di log console

```text
Connessione con client: Socket[addr=/127.0.0.1,port=51278,localport=1057]
=== SERVER IN ASCOLTO ===
In attesa di dati dal client...
=== CLIENT - INVIO LOGIN_REQUEST ===
{"token":"gHzmjrYHwc","actionType":"SEARCH_ITEMS_REQUEST","listaData":[{"valore":"Libro","modalita":2}]}
=== SERVER RICEVUTO ===
Stringa ricevuta: {"token":"gHzmjrYHwc","actionType":"SEARCH_ITEMS_REQUEST","listaData":[{"valore":"Libro","modalita":2}]}
Lunghezza: 104
======================
Tentativo di deserializzazione JSON...
JSON deserializzato con successo!
gHzmjrYHwc [{valore=Libro, modalita=2}]
In attesa di dati dal client...
=== CLIENT - RISPOSTA ===
404    Risorsa non trovata    null    SEARCH_ITEMS_RESPONSE    Accesso al sistema non consentito verifica validità sessione
Comando di chiusura ricevuto
'''

### Log di client autenticato manda request:
## Esempio di log console
'''text
Connessione con client: Socket[addr=/127.0.0.1,port=57041,localport=1057]
=== SERVER IN ASCOLTO ===
In attesa di dati dal client...
=== CLIENT - INVIO LOGIN_REQUEST ===
{"token":"Oukimcq4Nf","actionType":"SEARCH_ITEMS_REQUEST","listaData":[{"valore":"Libro","modalita":2}]}
=== SERVER RICEVUTO ===
Stringa ricevuta: {"token":"Oukimcq4Nf","actionType":"SEARCH_ITEMS_REQUEST","listaData":[{"valore":"Libro","modalita":2}]}
Lunghezza: 104
======================
Tentativo di deserializzazione JSON...
JSON deserializzato con successo!
Oukimcq4Nf [{valore=Libro, modalita=2}]
In attesa di dati dal client...
=== CLIENT - RISPOSTA ===
200	Successo	Oukimcq4Nf	SEARCH_ITEMS_RESPONSE	[{"nome":"Il Signore degli Anelli","autore":"J.R.R. Tolkien","tipologia":"Libro","id":1,"quanti":1,"isbn":"9780544003415","genere":"Fantasy"},{"nome":"1984","autore":"George Orwell","tipologia":"Libro","id":2,"quanti":0,"isbn":"9780451524935","genere":"Distopico"},{"nome":"Harry Potter e la Pietra Filosofale","autore":"J.K. Rowling","tipologia":"Libro","id":7,"quanti":6,"isbn":"9780747532743","genere":"Fantasy"},{"nome":"Il Nome della Rosa","autore":"Umberto Eco","tipologia":"Libro","id":10,"quanti":4,"isbn":"9788807900925","genere":"Storico"},{"nome":"pincopallo","autore":"ciao","tipologia":"Libro","id":1,"quanti":4,"isbn":null,"genere":null},{"nome":"pincopallo","autore":"ciao","tipologia":"Libro","id":15,"quanti":4,"isbn":null,"genere":null}]
Comando di chiusura ricevuto
scriviJson OK [tokens.json] 1 elementi salvati
'''


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




