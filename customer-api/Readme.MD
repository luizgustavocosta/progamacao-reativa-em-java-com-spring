
### H2 UI
<kbd>
    <img src="https://raw.githubusercontent.com/luizgustavocosta/progamacao-reativa-em-java-com-spring/main/customer-api/H2-ui.png">
</kbd>

|   URL	|   JDBC URL	|   User Name	| Password   	|   	
|---	|---	|---	|---	
|   http://localhost:8082	|   jdbc:h2:mem:customers	|  admin 	|  admin    	|

```sql
SELECT * FROM CUSTOMERS 
```
```sql
INSERT INTO CUSTOMERS (name, middleName, lastName, becamecustomer) VALUES ('John', null, 'Doe', '2021-09-19') 
```