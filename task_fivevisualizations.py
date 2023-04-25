import numpy as np
import matplotlib.pyplot as plt
import os
import glob
import csv


## Creating Graphs for Average Cost
k_values = [4,6,8,10,12]
random_averages = []
ourSeed_averages = []

def clusterroundAverages(method):
    #acquired just by running code
     

    random1= [8561, 7190, 7781, 7151, 8053, 5144, 1850, 1771, 1765, 1766]
    random2 = [8599, 8348, 7587, 7252, 8436, 8270, 8121, 8291, 8094, 8206]
    random3 = [19741, 12493, 12360, 8161, 7893, 7470, 3880, 2987, 1747, 1646]
    random4 = [8203, 4367, 2989, 4432, 2989, 3906, 1799, 1662, 1661, 1660]
    random5 = [7854, 7049, 6746, 5730, 5553, 4849, 3200, 4044, 3012, 3854]
    ourseedrun1 = [103, 103, 103, 102,102,102,102,102,102,102]
    ourseedrun2 = [246, 162, 151, 134, 134, 132, 133,133,133,133]
    ourseedrun3 = [266, 697, 97, 94, 94,94,94,94,94,94]
    ourseedrun4 =[415, 105, 104, 103,103,103,103,103,103,103]
    ourseedrun5 = [401, 146, 109, 114, 115, 113,113,113,113,113]
 
 
 
    ouravg = []
    randomavg =[]
    xes = [1,2,3,4,5,6,7,8,9,10]
    for i in range(10):
        temp1 = (ourseedrun1[i]+ourseedrun2[i]+ourseedrun3[i]+ourseedrun4[i]+ourseedrun5[i])/5
        temp2 = (random1[i]+random2[i]+random3[i]+random4[i]+random5[i])/5
        ouravg.append(temp1)
        randomavg.append(temp2)
    if method == "Random":
        plt.plot(xes, randomavg, '-o', label = "Random Averages")
    else: plt.plot(xes, ouravg, '-o', label = "Our Seeds Averages")
    plt.xlabel("Iterations")
    plt.ylabel("Average Cost over 5 Runs")

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
#clusterroundAverages("test")
#plt.legend()

graphk10Centers()
graphAll()
plt.show()