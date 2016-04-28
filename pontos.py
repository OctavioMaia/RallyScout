import math


def gerapontos(raio, totPont):
	raio1 = raio*0.75
	step = 360/(totPont*1.0)
	teta1=0
	for i in xrange(0,totPont):
		teta1+=step
		teta =math.radians(teta1)
		#x = raio *math.cos(teta)+raio1*math.sin(teta)
		#z = -raio *math.sin(teta)+raio1*math.cos(teta)
		x = raio *math.cos(teta)
		z = raio1 *math.sin(teta)
		x = float("{0:.4f}".format(x))
		z = float("{0:.4f}".format(z))
		print "<point X=\"" + str(x) +"\" Y=\"0\" Z=\""+str(z)+"\"/>"
		








raios=[]
#raios.append(1.5)
#raios.append(1.868)
#raios.append(2.583)
raios.append(0.87)
#raios.append(3.936)
#raios.append(1)
#raios.append(1.5)
#raios.append(5.44)
#raios.append(1.2)
#raios.append(1.8)
#raios.append(8.6)
#raios.append(0.9)
#raios.append(1)
#raios.append(10.62)
#raios.append(1)
#raios.append(12.72)
#raios.append(1)




for raio in raios:
	gerapontos(raio,8)
	print "\n\n\n"