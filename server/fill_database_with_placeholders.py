from database import Database


database = Database()
database.fillDatabase()
#database.droppAllData()
database.commitToDatabase()
database.Close()
