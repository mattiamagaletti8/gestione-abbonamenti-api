# Gestione Abbonamenti API

Progetto didattico di Mattia Magaletti per esercitarsi con Java 17, Spring Boot,
API REST, JPA/Hibernate e PostgreSQL. Realizzato con supporto AI e usato per approfondire
architettura a livelli, validazione e persistenza.

Permette di creare clienti e abbonamenti, consultarli, disattivarli e calcolare
il totale mensile degli abbonamenti attivi. Il database si chiama `mattia_abbonamenti`.

## Requisiti e configurazione

- JDK 17 e variabile `JAVA_HOME` impostata sul JDK.
- PostgreSQL locale avviato, raggiungibile sulla porta 5432.
- Accesso a Internet per scaricare le dipendenze al primo avvio.
- Maven non e' necessario installarlo separatamente: il repository contiene Maven Wrapper.

Creare un database vuoto usando un account PostgreSQL autorizzato, per esempio da DBeaver:

```sql
CREATE DATABASE mattia_abbonamenti;
```

L'utente scelto per l'applicazione deve poter creare e modificare le tabelle di quel database.
Copiare `config/local.properties.example` in `config/local.properties` e sostituire
i segnaposto con le proprie credenziali. Su PowerShell, dalla cartella del progetto:

```powershell
Copy-Item config/local.properties.example config/local.properties
```

Eseguire la copia solo alla prima configurazione, senza sovrascrivere un file locale gia' compilato.
Hibernate crea/aggiorna le tabelle all'avvio. Il database inizialmente e' vuoto:
creare prima un cliente e usare il suo ID per creare un abbonamento.

## Avvio da terminale

Eseguire i comandi dalla cartella che contiene `pom.xml`.

Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

Linux/macOS:

```bash
chmod +x mvnw
./mvnw spring-boot:run
```

L'applicazione ascolta solo sul PC locale, su `http://127.0.0.1:8085`.
Aprire [Swagger UI](http://127.0.0.1:8085/swagger-ui/index.html) per provare le API.
Il percorso `/` non contiene una pagina iniziale: usare Swagger o gli endpoint `/api/...`.
Fermare l'esecuzione con Ctrl+C. Se la porta 8085 e' occupata da un'altra istanza,
fermare quella precedente prima di riavviare.

## Avvio da Eclipse

Aprire il progetto Maven e avviare `GestioneAbbonamentiApplication` con **Run As > Java Application**.
La directory di lavoro deve essere la cartella del progetto (impostazione predefinita).
Le API rispondono su **http://127.0.0.1:8085**; non sono esposte alla rete.
Se un'istanza e' gia' in esecuzione, fermarla prima di avviarne una seconda.

Le credenziali della connessione locale sono in `config/local.properties`, escluso da Git.
Il file di esempio contiene solo segnaposto, non credenziali reali.
Hibernate crea/aggiorna le due tabelle, senza cancellare i dati ad ogni avvio.

## Struttura come HakuStore

Il codice segue le convenzioni organizzative del progetto formativo HakuStore:

- `controllers`: ClienteController, AbbonamentoController ed ExceptionManager.
- `dto/input`: ClienteReq e AbbonamentoReq raccolgono e validano i dati ricevuti.
- `dto/output`: ClienteDTO, AbbonamentoDTO, TotaleMensileDTO ed ErroreDTO descrivono le risposte.
- `mapping`: ClienteMap e AbbonamentoMap trasformano le entita' in DTO con metodi statici e builder.
- `services/interfaces`: IClienteServices e IAbbonamentoServices dichiarano le operazioni.
- `services/implementations`: ClienteImpl e AbbonamentoImpl implementano le regole applicative.
- `repositories`: IClienteRepository e IAbbonamentoRepository accedono al database.
- `models`: Cliente e Abbonamento rappresentano le tabelle.
- `exceptions`: ApiException segnala il cliente o abbonamento non trovato.

Come HakuStore, usiamo classi DTO con Lombok (`@Getter`, `@Setter`, `@Builder`) e
`@RequiredArgsConstructor` per l'iniezione tramite costruttore. Non usiamo piu' i record annidati in DatiApi.
I nomi dei package sono cambiati; gli URL, i JSON, i codici HTTP e le tabelle sono gli stessi.
La query JPQL del totale si trova in `repositories/IAbbonamentoRepository.java`.
Le regole di creazione e disattivazione sono in AbbonamentoImpl.

Esempio: Swagger invia un JSON -> il controller riceve AbbonamentoReq -> AbbonamentoImpl
verifica il cliente e prepara l'entita' -> IAbbonamentoRepository la salva ->
AbbonamentoMap costruisce AbbonamentoDTO -> il controller restituisce il JSON con HTTP 201.

In Eclipse aggiornare il progetto con F5 e poi Maven > Update Project per caricare Lombok.

## Percorso della richiesta

`Controller -> Service -> Repository -> Entity -> PostgreSQL`

- **controllers**: riceve le richieste HTTP e restituisce JSON.
- **dto**: definisce i dati di ingresso/uscita; Bean Validation controlla i campi.
- **services**: contiene le regole e i confini delle transazioni.
- **repositories**: Spring Data JPA genera le operazioni di accesso al database.
- **models**: Cliente e Abbonamento; un cliente puo' avere piu' abbonamenti.

I prezzi sono `BigDecimal`. La disattivazione ripetuta non cambia la prima data di disattivazione.
Il totale mensile somma i prezzi degli abbonamenti attivi: non e' un pagamento o una fattura.

## Endpoint

| Metodo | URL | Funzione |
|---|---|---|
| GET | /api/clienti | Elenco clienti |
| POST | /api/clienti | Crea cliente |
| GET | /api/clienti/{id} | Cerca cliente |
| GET | /api/abbonamenti | Elenco abbonamenti |
| GET | /api/abbonamenti?attivo=true | Solo attivi |
| POST | /api/abbonamenti | Crea abbonamento attivo |
| GET | /api/abbonamenti/{id} | Cerca abbonamento |
| PATCH | /api/abbonamenti/{id}/disattiva | Disattiva |
| GET | /api/abbonamenti/totale-mensile | Totale mensile attivi |

Creare un cliente con POST /api/clienti, header `Content-Type: application/json`:

```json
{"nome":"Mario Rossi","email":"mario.nuovo@example.com"}
```

Usare l'id restituito per POST /api/abbonamenti:

```json
{"clienteId":1,"piano":"Base","prezzoMensile":15.00}
```

Risposte: **201** creazione, **200** lettura/disattivazione, **400** dati o JSON non validi,
**404** cliente/abbonamento inesistente, **409** email duplicata.

## Swagger UI: provare le API dal browser

Con l'applicazione avviata, aprire http://127.0.0.1:8085/swagger-ui/index.html.
La descrizione OpenAPI in JSON si trova su http://127.0.0.1:8085/v3/api-docs.

Per la prima prova, espandere **GET /api/abbonamenti**, premere **Try it out** e poi **Execute**.
La sezione **Response body** mostra il JSON restituito; **Code 200** indica che la richiesta e' riuscita.
Per GET /api/abbonamenti/{id}, inserire un ID esistente prima di Execute.
POST crea dati e PATCH disattiva un abbonamento: queste richieste modificano davvero il database locale.

Swagger UI e' integrato tramite springdoc-openapi; legge i controller e genera la lista delle API.
Se Eclipse non riconosce la nuova dipendenza: tasto destro sul progetto > Maven > Update Project.
Fermare e riavviare l'applicazione dopo l'aggiornamento.

## Postman

Importare `GestioneAbbonamenti.postman_collection.json` e lanciare le richieste nell'ordine indicato.
La collection genera un'email diversa ogni volta e memorizza automaticamente gli ID creati.
Le richieste negative verificano errori 400 e 404. I dati di esempio restano nel database.

## Esercizi SQL

Collegarsi a PostgreSQL locale e selezionare `mattia_abbonamenti`.
Eseguire le query di `esercizi.sql` per esercitarsi con join, filtri e aggregazioni.

## Compilazione

Eseguire `.\mvnw.cmd package` su Windows oppure `./mvnw package` su Linux/macOS.
Il progetto non include test automatici JUnit; le API si possono provare manualmente con Swagger UI o Postman.

Questo e' un progetto didattico locale: non include ancora autenticazione, fatturazione o pagamenti reali.
