-- Przykładowe dane dla aplikacji stock-app

-- Akcje (Stock)
INSERT INTO stock (symbol, company_name, current_price, last_update) VALUES
('PKO', 'PKO Bank Polski', 35.50, NOW()),
('CCC', 'CCC S.A.', 15.20, NOW()),
('KGHM', 'KGHM Polska Miedź', 85.40, NOW()),
('PGE', 'Polska Grupa Energetyczna', 12.30, NOW()),
('CDR', 'CD Projekt', 125.80, NOW()),
('ALE', 'Allegro.eu', 45.60, NOW()),
('PEO', 'Bank Pekao', 89.20, NOW()),
('JSW', 'Jastrzębska Spółka Węglowa', 28.40, NOW()),
('LPP', 'LPP S.A.', 8650.00, NOW()),
('DNP', 'Dino Polska', 290.50, NOW());

-- Klienci (Client)
INSERT INTO client (first_name, last_name, email, balance, registration_date) VALUES
('Jan', 'Kowalski', 'jan.kowalski@example.com', 10000.00, NOW()),
('Anna', 'Nowak', 'anna.nowak@example.com', 25000.00, NOW()),
('Piotr', 'Wiśniewski', 'piotr.wisniewski@example.com', 15000.00, NOW()),
('Maria', 'Kaczmarek', 'maria.kaczmarek@example.com', 30000.00, NOW()),
('Tomasz', 'Lewandowski', 'tomasz.lewandowski@example.com', 12000.00, NOW());

-- Indeksy giełdowe (Index)
INSERT INTO stock_index (symbol, name, description, current_value, last_update) VALUES
('WIG20', 'Warszawski Indeks Giełdowy 20', '20 największych spółek na GPW', 2250.50, NOW()),
('WIG-BANKI', 'WIG Banki', 'Indeks spółek bankowych', 8650.30, NOW()),
('WIG-IT', 'WIG Informatyka', 'Indeks spółek informatycznych', 1845.70, NOW()),
('mWIG40', 'mWIG40', 'Indeks średnich spółek', 4320.80, NOW()),
('sWIG80', 'sWIG80', 'Indeks małych spółek', 18450.20, NOW());

-- Relacje między akcjami a indeksami (Index_Stocks)
-- WIG20 - największe spółki
INSERT INTO index_stocks (index_id, stock_id) VALUES
(1, 1), -- PKO -> WIG20
(1, 3), -- KGHM -> WIG20
(1, 4), -- PGE -> WIG20
(1, 5), -- CDR -> WIG20
(1, 6), -- ALE -> WIG20
(1, 7), -- PEO -> WIG20
(1, 9), -- LPP -> WIG20
(1, 10); -- DNP -> WIG20

-- WIG-BANKI - banki
INSERT INTO index_stocks (index_id, stock_id) VALUES
(2, 1), -- PKO -> WIG-BANKI
(2, 7); -- PEO -> WIG-BANKI

-- WIG-IT - informatyka
INSERT INTO index_stocks (index_id, stock_id) VALUES
(3, 5), -- CDR -> WIG-IT
(3, 6); -- ALE -> WIG-IT

-- mWIG40 - średnie spółki
INSERT INTO index_stocks (index_id, stock_id) VALUES
(4, 2), -- CCC -> mWIG40
(4, 8); -- JSW -> mWIG40

-- Przykładowe transakcje (Transaction)
INSERT INTO transaction (client_id, stock_id, type, quantity, price, transaction_date) VALUES
(1, 1, 'BUY', 100, 35.00, NOW() - INTERVAL '1 day'),
(2, 5, 'BUY', 50, 120.00, NOW() - INTERVAL '5 hours'),
(3, 3, 'BUY', 25, 80.00, NOW() - INTERVAL '2 days'),
(4, 6, 'BUY', 200, 44.50, NOW() - INTERVAL '3 hours'),
(5, 1, 'BUY', 150, 36.20, NOW() - INTERVAL '6 hours'),
(1, 2, 'BUY', 300, 15.50, NOW() - INTERVAL '4 days'),
(2, 7, 'BUY', 75, 88.00, NOW() - INTERVAL '1 hour'),
(3, 9, 'BUY', 5, 8600.00, NOW() - INTERVAL '7 days'),
(4, 10, 'BUY', 40, 285.00, NOW() - INTERVAL '2 hours'),
(1, 1, 'SELL', 50, 35.80, NOW() - INTERVAL '30 minutes');