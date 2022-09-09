# Product from the perspective of the web scraper
import numbers

class Product:
    def __init__(self, name: str, price: numbers.Real):
        self.price = price;
        self.name = name


