import bs4
from urllib.request import urlopen as uReq
from bs4 import BeautifulSoup as soup

my_ulr = "https://www.newegg.com/Video-Cards-Video-Devices/Category/ID-38"

uClient = uReq(my_ulr)
page_html = uClient.read()
uClient.close()

page_soup = soup(page_html, "html.parser")
print(page_soup.h1)