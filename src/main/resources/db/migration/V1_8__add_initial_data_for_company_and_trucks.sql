INSERT INTO company(name, email, description, url, address, zipcode)
VALUES
('DHL', 'dhl_commercial@mail.com', 'Logistic company from Germany', 'dhl.com', 'Berlin', '580021'),
('Fujiwara Shop', 'takumi86@mail.com', 'Logistic company from Japan', 'bunta.com', 'Tokyo', '580022'),
('Coca-Cola', 'coacacola@mail.com', 'Coca-Cola company', 'cocacola.com', 'USA', '580027');


INSERT INTO truck(name, truck_number, vin_code, fuel_consumption, company_id)
VALUES
  ('Volvo XCX12001', 'CE12441FF', '4Y1SL65848Z411439', 40.2, (SELECT id from company where email = 'dhl_commercial@mail.com' limit 1)),
  ('Scania V8ULTA', 'CE123421XZ', 'WMWZN3C51BT133317', 65.2, (SELECT id from company where email = 'dhl_commercial@mail.com' limit 1)),
  ('Volvo ZQ11090', 'EW15678FB', 'JN1CA21DXXT805880', 56.1, (SELECT id from company where email = 'takumi86@mail.com' limit 1)),
  ('Renault Megane', 'CE1241FY', '1GCHK23D07F136336', 40.2, (SELECT id from company where email = 'coacacola@mail.com' limit 1)),
  ('Renault Megane 2', 'CE12041FF', '1ZVHT85H465102319', 49.2, (SELECT id from company where email = 'coacacola@mail.com' limit 1));
