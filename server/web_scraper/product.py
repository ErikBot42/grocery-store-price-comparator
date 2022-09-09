# Product from the perspective of the web scraper

from enum import Enum, unique
@unique
class Store(Enum):
    LIDL = 1
    COOP = 2
    def __str__(self):
        match self:
            case self.LIDL:
                return "Lidl"
            case self.COOP:
                return "Coop"


class Product:
    def __init__(self, name: str, price: float, store: Store):
        assert name != ""
        assert price > 0
        self.price = price
        self.name = name
        self.store = store
    def __str__(self):
        return self.name + ": " + str(self.price) + " kr at " + str(self.store)
    def __repr__(self):
        return self.name + ": " + str(self.price) + " kr at " + str(self.store)

