-- Selezionare il database PostgreSQL mattia_abbonamenti prima di eseguire.
SELECT * FROM clienti ORDER BY id;
SELECT * FROM abbonamenti ORDER BY id;

-- Clienti e relativi abbonamenti.
SELECT c.nome, c.email, a.piano, a.prezzo_mensile, a.attivo
FROM clienti c JOIN abbonamenti a ON a.cliente_id = c.id;

-- Totale mensile dei soli abbonamenti attivi.
SELECT COALESCE(SUM(prezzo_mensile), 0) AS totale_mensile
FROM abbonamenti WHERE attivo = true;

-- Conteggio e valore degli abbonamenti per cliente.
SELECT c.id, c.nome, COUNT(a.id) AS numero_abbonamenti,
       COALESCE(SUM(a.prezzo_mensile), 0) AS importo
FROM clienti c LEFT JOIN abbonamenti a ON a.cliente_id = c.id
GROUP BY c.id, c.nome ORDER BY c.id;

-- Clienti senza abbonamenti.
SELECT c.* FROM clienti c
LEFT JOIN abbonamenti a ON a.cliente_id = c.id WHERE a.id IS NULL;
