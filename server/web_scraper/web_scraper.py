import requests
import re
import product
from bs4 import BeautifulSoup

def filter_price_string(price_string: str) -> str:
    return re.sub(r'[^\d.,]*', '', price_string.strip())

def try_request(http_str: str) -> bytes | None:
    try:
        r = requests.get(http_str)
        return r.content
    except requests.exceptions.ConnectionError:
        return None


content = try_request('https://www.lidl.se/veckans-erbjudanden')
if content == None:
    exit(1)
soup = BeautifulSoup(content, 'html.parser')

#<div class="nuc-a-flex-item nuc-a-flex-item--width-6 nuc-a-flex-item--width-4@sm"

s = soup.find_all('div', class_ = "nuc-a-flex-item nuc-a-flex-item--width-6 nuc-a-flex-item--width-4@sm")
print(len(s))
for el in s:
    #print(el.find('article'))
    product_price_el = el.find('span', class_ = "lidl-m-pricebox__price");
    product_name_el = el.find('h3', class_ = "ret-o-card__headline");

    price_str = filter_price_string(product_price_el.string)

    print("\"","name:",product_name_el.string.strip(),"\"")
    print("\"","price:",price_str,"\"")
    print()




#for link in soup.find_all('a'):
#    print(link.get('href'))
#    print()
#<span class="lidl-m-pricebox__price">99.90</span>
#<h3 class="ret-o-card__headline">Kvibille® Cheddar
#                                                    </h3>
#print(s)
#print(soup)
#print(soup.title)
#print(r);
#print(r.url);

