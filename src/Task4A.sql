SELECT customer_id, first_name, last_name, email
FROM customers
WHERE last_name = 'Smith' AND YEAR(created_at) = 2023;
