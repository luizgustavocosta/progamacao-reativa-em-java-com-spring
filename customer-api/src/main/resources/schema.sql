CREATE TABLE IF NOT EXISTS public.customers
(
    id             integer auto_increment,
    name           varchar(25),
    middlename     varchar(25),
    lastname       varchar(25),
    becamecustomer date,
    CONSTRAINT "pk_customers" PRIMARY KEY (id)
);