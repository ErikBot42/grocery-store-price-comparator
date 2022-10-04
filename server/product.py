# Product from the perspective of the web scraper
# This WILL have to be parsed later to provide useful info
# TODO: images
# from server import Database

from enum import Enum, unique
@unique
class Store(Enum):
    LIDL = 1
    COOP = 2
    ICA = 3
    WILLYS = 4
    def __str__(self):
        match self:
            case self.LIDL:
                return "LIDL"
            case self.COOP:
                return "COOP"
            case self.ICA:
                return "ICA"
            case self.WILLYS:
                return "WILLYS"


class ExtractedInfo:
    def __init__(self):
        self.price = ""
        pass
    def try_read(self, string: str):
        import ply.lex as lex

        # List of token names.   This is always required
        tokens = (
            #'NUMBER',
           'PRICE',
           'PER_KG',
           #'MINUS',
           #'TIMES',
           #'DIVIDE',
           #'LPAREN',
           #'RPAREN',
        )
        
        # Regular expression rules for simple tokens
        t_PRICE = r'(\d+[ .]\d\d)|(\d+:-)'
        t_PER_KG
        #t_MINUS   = r'-'
        #t_TIMES   = r'\*'
        #t_DIVIDE  = r'/'
        #t_LPAREN  = r'\('
        #t_RPAREN  = r'\)'
        
        # A regular expression rule with some action code
        #def t_NUMBER(t):
        #    r'\d+'
        #    t.value = int(t.value)
        #    return t
        
        # Define a rule so we can track line numbers
        #def t_newline(t):
        #    r'\n+'
        #    t.lexer.lineno += len(t.value)
        
        # A string containing ignored characters (spaces and tabs)
        t_ignore  = ' \t\n'
        
        # Error handling rule
        def t_error(t):
            print("Illegal character '%s'" % t.value[0])
            t.lexer.skip(1)
        
        # Build the lexer
        lexer = lex.lex()

        print() 
        print(string)
        # Give the lexer some input
        lexer.input(string)
        
        # Tokenize
        while True:
            tok = lexer.token()
            if not tok:
                break      # No more input

            print(tok.type, ": " , tok.value, sep="")




class Product:
    #TODO: create function that may return product if it's valid
    def __init__(self,
            name: str,
            price: str,
            store: Store,
            description: str = "",
            category: str = "",
            image_url: str = "",
            #product_url: str = "",
            amount: str = "",
            modifier: str = "",
            ):
        self.amount = amount
        self.category = category
        self.description = description
        self.image_url = image_url
        self.modifier = modifier
        self.name = name
        self.price = price
        #self.product_url = product_url
        self.store = store

    def __str__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description

    def __repr__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description

    def print(self):
        attrs = vars(self)
        print('\n'.join("%s: %s" % item for item in attrs.items()))


