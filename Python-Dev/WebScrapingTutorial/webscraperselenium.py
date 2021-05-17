from selenium import webdriver

url = "https://www.fiverr.com/search/gigs?query=python&source=main_banner&search_in=everywhere&search-autocomplete-original-term=python"
browser = webdriver.Chrome()
browser.get(url)

browser.find_element_by_xpath("//*[@id=\"perseus-app\"]/div/div/div[5]/div/div/div[1]/div/div/a").click()