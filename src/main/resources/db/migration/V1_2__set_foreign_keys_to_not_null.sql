ALTER TABLE issue_statuses
	ALTER COLUMN book_id SET NOT NULL,
	ALTER COLUMN customer_id SET NOT NULL;
