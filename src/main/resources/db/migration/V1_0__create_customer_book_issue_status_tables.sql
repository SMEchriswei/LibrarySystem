CREATE TABLE customers(
	id BIGSERIAL PRIMARY KEY,
	name TEXT,
	address TEXT
);

CREATE TABLE books(
	id BIGSERIAL PRIMARY KEY,
	title TEXT,
	category TEXT,
	rental_price NUMERIC,
	status BOOLEAN DEFAULT 't'
);

CREATE TABLE issue_statuses(
	id BIGSERIAL PRIMARY KEY,
	issue_date DATE,
	return_date DATE,
	customer_id INTEGER REFERENCES customers(id),
	book_id INTEGER REFERENCES books(id)
);

