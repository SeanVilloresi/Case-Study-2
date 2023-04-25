import numpy as np
import matplotlib.pyplot as plt
import os
import glob
import csv


## Creating Graphs for Average Cost
k_values = [4,6,8,10,12]
random_averages = []
ourSeed_averages = []

def clusterroundAverages():
    #acquired just by running code
    random1= [18485 ,12293 ,2122 ,1827 ,1265 ,978 ,996 ,1032 ,975 ,998 ,973 ,984 ,986 ,1011 ,1009]
    random2 = [11242 ,6481 ,975 ,946 ,958 ,960 ,989 ,1010 ,1010 ,1010 ,1036 ,1008 ,954 ,983 ,972]
    random3 = [18513 ,7079 ,2667 ,2676 ,2692 ,2684 ,2687 ,2712 ,2675 ,2680 ,2687 ,2675 ,2701 ,2690 ,2695]
    random4 = [17179 ,6225 ,1867 ,1195 ,1169 ,1131 ,1174 ,1180 ,1143 ,1175 ,1145 ,1105 ,1134 ,1175 ,1124]
    random5 = [17113 ,4218 ,1812 ,1147 ,1188 ,1158 ,1179 ,1167 ,1195 ,1180 ,1128 ,1164 ,1140 ,1138 ,1178]
    ourseedrun1 = [218 ,202 ,255 ,247 ,190 ,188 ,224 ,222 ,173 ,217 ,224 ,261 ,249 ,214 ,228]
    ourseedrun2 = [276 ,309 ,321 ,293 ,314 ,292 ,291 ,288 ,282 ,291 ,319 ,332 ,323 ,278 ,289]
    ourseedrun3 = [241 ,237 ,219 ,249 ,211 ,254 ,249 ,205 ,249 ,248 ,219 ,231 ,246 ,245 ,257]
    ourseedrun4 =[188 ,201 ,199 ,218 ,221 ,221 ,234 ,228 ,249 ,252 ,249 ,229 ,234 ,243 ,211]
    ourseedrun5 = [218 ,212 ,223 ,191 ,251 ,249 ,246 ,250 ,251 ,249 ,258 ,250 ,248 ,246 ,260]

    ouravg = []
    randomavg =[]
    xes = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15]
    for i in range(15):
        temp1 = (ourseedrun1[i]+ourseedrun2[i]+ourseedrun3[i]+ourseedrun4[i]+ourseedrun5[i])/5
        temp2 = (random1[i]+random2[i]+random3[i]+random4[i]+random5[i])/5
        ouravg.append(temp1)
        randomavg.append(temp2)
    plt.plot(xes, randomavg, '-o', label = "Random Averages")
    plt.plot(xes, ouravg, '-o', label = "Our Seeds Averages")

def graph_Averages():
    with open("randomAverages.txt", 'r') as my_file:

        for line in my_file:
            if len(line)!=0:
                values = line.strip().split('=')
                
                avg = float(values[2])
                random_averages.append(avg)
    with open("ourseedAverages.txt", 'r') as my_file:

        for line in my_file:
            if len(line)!=0:
                values = line.strip().split('=')
                
                avg = float(values[2])
                ourSeed_averages.append(avg)
    plt.plot(k_values, random_averages, '-o', label = "Random Averages")
    plt.plot(k_values, ourSeed_averages, '-o', label = "Our Seeds Averages")
    


def graphk10Centers():
    trajectories = {}
    with open("k10centers.txt", 'r') as my_file:
            #print(file)
            
        for line in my_file:
            if len(line) != 0: 
                values = line.strip().split(',')
                id = float(values[0].replace('center', ''))
                x = float(values[1])
                y = float(values[2])
                if id not in trajectories:
                    trajectories[id] = ([x],[y])
                else:
                    trajectories[id][0].append(x)
                    trajectories[id][1].append(y)

    ##Plots center trajectories
    for i in range(10):
        plt.plot(trajectories[i][0], trajectories[i][1], '-o', alpha = .5)




##Plots a scatterplot of all the points, just used as a check
def graphAll():
    x_vals = []
    y_vals = []
    with open('geolife-cars-upd8.csv', newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader)
        for row in reader:
            # Assuming the ID column is not needed
            _, x, y = row
            #print(x)
            x_vals.append(float(x))
            y_vals.append(float(y))
    plt.scatter(x_vals, y_vals, alpha=.1)

#graph_Averages()
#clusterroundAverages()
#plt.legend()

graphk10Centers()
#graphAll()
plt.show()