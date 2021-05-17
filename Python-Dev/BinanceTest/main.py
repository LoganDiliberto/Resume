from binance.client import Client
client = Client("1FHhQfGXdOcCFnbhHDVXGyTYdz2g4hwIBrUXbXy3DETcW5AziKPjuNsJOAs1uMoX" , "t5Ivc3YpWy73fURGnX5wAP7zmeZqTrjfNhXNXU448PboUDj3TzYjXdSrbvHRlf54")

prices = client.get_all_tickers()

print(prices)