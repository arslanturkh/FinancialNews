The software development project focused on creating a financial news consultation portal. This comprehensive project, structured around PostgreSQL 12.5, encompassed three critical components: Ingestion, API, and Portal, each tailored to meet specific functionalities within the system.

Ingestion Component: Successfully implemented, this component utilizes the FinNews library for daily ingestion of news articles from sources like Yahoo. The articles are stored in a well-designed database, adhering to a schema that efficiently organizes the data for easy retrieval.

API Component: A robust Spring/Java API backend has been developed and deployed. This API provides extensive search capabilities, allowing users to filter articles by words in titles and content, dates, stock codes, topics, and keywords. An advanced full-text search feature with weighted results has also been integrated, enhancing the search accuracy and relevance.

Portal Component: The frontend, crafted in Angular, offers a user-friendly interface with a search bar and filter form. It efficiently displays news articles matching the selected filters and allows users to sort and filter content by stock code, search score, and dates. The integration of fusioncharts to visualize the daily volume of news articles adds an insightful analytical dimension to the portal.


To run this project you will need to install necessary libaries first.

1. Ingestion
	- Set up a PostgreSQL
	- Create a table with the following schema: run "CreateDB.sql"
	- Install the required libraries: FinNews and psycopg2 "pip install finnews psycopg2"
	- Install SQLAlchemy: pip install sqlalchemy
	- install the schedule library: pip install schedule
	- Load data using scheduler (python scheduler.py) or load data from existing data by runing ("LoadData") instead of "CreateDB.sql"
	
	Notes:
		- Scheduler runs every day at 1 am and loads only previous days news.
		- "Factory Method" design pattern is applied. This pattern provides a way to create objects without specifying the exact class of object that will be created. In this case, it allows you to create different news fetcher instances for each channel without having to know their exact implementations. With this design pattern in place, you can easily add support for more news channels by implementing new NewsFetcher subclasses and updating the newsFetcherFactory function. This keeps the code modular and easy to maintain.
	
	
2. API
	- Open the src/main/resources/application.properties file and edit configuration to set up the connection to your PostgreSQL database
	
	Notes: 
		- index was creaed to improve the performance of full-text search queries.
		
