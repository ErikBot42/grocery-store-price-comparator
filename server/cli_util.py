#!/usr/bin/python3
#utility to run web scraper and manipulate database from command line.

import sys

usage: str = """
Avaliable args (can be combined):
--scrape: scrapes to database
--clear-database: remove everything from database
--add-placeholder: add placeholders to database
--webserver: start the webserver
--recreate: regenerate (empty) database
--flask-server: run flask server
"""

if len(sys.argv) == 1:
    print(usage)

from database import Database
for command in sys.argv[1:]:
    match command:
        case "--scrape":
            print("scraping to database")
            database = Database()
            from web_scraper import add_all_to_database
            add_all_to_database(database)
            database.commitToDatabase()
            database.close()
        case "--clear-database":
            print("clearing database")
            database = Database()
            database.droppAllData()
            database.commitToDatabase()
            database.close()
        case "--add-placeholder":
            print("add placeholders to database")
            database = Database()
            database.fillDatabase()
            database.commitToDatabase()
            database.close()
        case "--recreate":
            database = Database()
            database.recreateDatabase()
            database.close()
        case "--webserver":
            import webserver
            webserver.startWebServer()
        case "--flask-server":
            import flask_server
            flask_server.runServer()
        case _:
            print(usage)
            exit(0)
            




