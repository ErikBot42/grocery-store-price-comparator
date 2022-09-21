from database import Database


database = Database()
#database.fillDatabase()
#database.droppAllData()
database.cursor.execute("DROP TABLE Product")
database.cursor.execute("""CREATE TABLE "Product" (
	"Category_ID"	INTEGER,
	"Product_ID"	INTEGER,
	"Product_Name"	TEXT,
	"Store_ID"	INTEGER,
	"Price"	TEXT,
	"URL"	TEXT,
	PRIMARY KEY("Product_ID"),
	FOREIGN KEY("Store_ID") REFERENCES "Store"("Store_ID"),
	FOREIGN KEY("Category_ID") REFERENCES "Category"("Category_ID")
)""")
database.commitToDatabase()
database.Close()
