import string

wordFile = []

with open('wordlist.txt', 'r' ) as infile:
	wordFile = infile.readlines()
    
for firstLetter in xrange(97, 123):
    for secondLetter in xrange(97,123):
        prefix = chr(firstLetter) + chr(secondLetter)
        for line in wordFile:
            if line.startswith(prefix):
                print 'prefix found'
                with open(prefix + 'File.txt', 'a') as outfile:
                    outfile.write(line)
                    
                    
                        
                        
                        
                        





