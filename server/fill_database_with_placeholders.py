from database import Database


database = Database()
#database.recreateDatabase()
#database.droppAllData()
database.fillDatabase()
database.commitToDatabase()
database.close()
