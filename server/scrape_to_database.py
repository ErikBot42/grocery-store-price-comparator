from web_scraper import add_all_to_database
from database import Database
data = Database()
add_all_to_database(data)
#data.commitToDatabase() #Lägg till för att spara data till databasen
data.Close() 