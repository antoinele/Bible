BibleApp
========

Notes
-----
The multithreading implementation is currently only a partial one; it's only there to test whether file parsing is faster when threaded or not. (spoilers: for our case, it's not)

It should work in most cases, however as there's no synchronisation for the return of the WordMap object, there's an edge case where you can can get synchronisation problems by immediately querying for a word after the program loads. 

It is worth looking into threaded file reading, however, as it seems performing sequential reads without the delay of parsing is faster.  