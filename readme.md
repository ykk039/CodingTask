# Light version Warehouse Inventory System
The simple Warehouse Inventory System using Spring-boot framework for the back-end and ReactJs for the front-end

# Features
  - able to store product data via csv file consumption
  - able to store quantities of such products in different locations via csv file consumption
  - UI to show inventory level of given product code
  - able to transfer inventory from one location to another given amount of quantity and product code via UI

# Setup
In this application, I use the MySQL server as the database, so you may have to follow the instruction as down below first:

 - First thing to do is open and login the mysql in your cmd, and the port number of the server is 3306.
 - Then run the following commands at the mysql prompt:

```sh
mysql> create database demo;
mysql> create database demo_test;
mysql> create user 'test_user'@'%' identified by 'test123'; 
mysql> grant all on demo.* to 'test_user'@'%';
mysql> grant all on demo_test.* to 'test_user'@'%'; 
```
Or you can create the own database and user. Then chanage the setting in "src\main\resources\application.properties" and "src\test\resources\application.properties"

# Test
Run the following commands to test:
```sh
cd src/test
mvn test
```

# Complie & Run
Run the following commands to compile the web application.

```sh
mvnw spring-boot:run
```
After compile it successfully, browser [localhost:8080] on browser to access the web application.

# Navbar
You can simply click the button in the navbar to go to the page you want, products or stocks page



# Upload CSV
As considered there may be a huge amount of data needed to upload, it is impossible to do it one by one. Therefore, you can upload csv to upload the data at once. There are two sample csv files in /sample_test for products and stocks.

- CSV format for product: code,name,weight
- CSV format for stocks: location,code,quantity

# Products 

### Search Bar
Search products by both name or code is avaliable in this application.

### Download CSV
Click the "Download as CSV" to download the csv file of all the products data.

### Add Product
Click the New Product and fill the form poped out, then press save button to create the new record.

### Edit Product
Click the pen icon and fill the form poped out, then press save button to change the new record.

### Delete Product
Click the rubbish bin icon to delete the new record.

# Stock Data

### Search
Search products by both location or code is avaliable in this application.

### Download CSV
Click the "Download as CSV" to download the csv file of all the stock data.

### Add Stock
Click the New Stock Data and fill the form poped out, then press save button to create the new record.

### Edit Stock
Click the pen icon and fill the form poped out, then press save button to change the new record.

### Delete Stock
Click the rubbish bin icon to delete the new record.

### Transer Stock
Click the rubbish bin icon to delete the new record.
