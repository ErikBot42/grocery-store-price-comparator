from database import Database


database = Database()
database.recreateDatabase()
database.fillDatabase()
database.commitToDatabase()
database.close()
