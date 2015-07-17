cont = [True]
def t(v):
	a = "3"
	def j(x):
		print "f anidada: ", x
	while cont[0]:
		print "Recursion: "+str(v)+" -- ", str(j(a))+" ", j(str(3))
		if v == 10:
			cont[0] = False
		t(v = v+1)
print t(0)