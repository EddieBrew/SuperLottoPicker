# SuperLottoPicker
The SuperLottoPicker app  generates the California Lottery Super Lotto numbers.
The app retrieves the past 52 weeks lotto numbers, using webscraping methods with the help of Volley
and Jsoup libraries and  and stores the values.

The apps allows the user to input the min and max frequency ranges the numbers have been drawn, and
from that pool of number, four(4) lottery ticket quick picts are generated and displayed on the UI

The apps consists of three(3) activities  that assists the user in generating
quickpick lottery numbers:

	    GeneratorFragment: Allows the user to input the min and max frequency ranges the lotto
                           and mega  numbers have been drawn, and from that pool of number, four(4)
                           lottery ticket quick picts are generated and displayed on the UI

	    PastLottoNumbersFragment: Display the past 52 week drawn lottery numbers

	    FrequencyFragment: Display the number of time each SuperLotto and Mega numbers have been
	                       drawn the past 52 weeks.
