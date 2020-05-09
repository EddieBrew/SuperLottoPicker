# SuperLottoPicker
Initial commit

The SuperLottoPicker app  generates the California Lottery Super Lotto numbers.
The app uses the California State Lottery and Mega numbers, up to the number 47 and 27 respectively. The app retrieves the past 52
weeks SuperLotto numbers, using webscraping methods with the help of Volley and Jsoup libraries and  and stores the values. The apps stores previous lotto
numbers, in a Hashmap, where the key is the number and the value is the number of times the number has been drawn.

The apps allows the user to input the min and max frequency ranges the numbers have been drawn, and
from that pool of number, four(4) lottery ticket quick picks are generated and displayed on the UI

