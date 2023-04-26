import numpy as np
import matplotlib.pyplot as plt
import os
import glob
import csv


## Creating Graphs for Average Cost
k_values = [4,6,8,10,12]
random_averages = []
ourSeed_averages = []

##Averages across runs and iterations
def clusterroundAverages(method):
    #acquired just by running code and copying results(sorry)
     

    random0 = [8982, 6959, 5703, 5703, 5704, 5703, 5702, 5704, 5703, 5702]
    random1 = [17302, 12593, 5706, 5706, 5708, 5705, 5707, 5705, 5707, 5705]
    random2 = [3894, 3779, 3683, 3697, 3649, 3686, 3660, 3686, 3456, 3416]
    random3 = [15360, 14826, 8018, 5709, 5706, 5705, 5727, 5704, 5706, 5706]
    random4 = [12251, 5975, 5976, 5980, 5976, 6034, 5705, 5703, 5705, 5703 ]
    ourseedrun0 = [90, 46, 51, 46, 45, 45, 43, 49, 48, 47]
    ourseedrun1 = [55, 57, 53, 51, 50, 49, 51, 50, 51, 50]
    ourseedrun2 = [82, 57, 57, 54, 53, 50, 48, 51, 50, 51]
    ourseedrun3 = [78, 71, 61, 61, 59, 67, 63, 63, 61, 58]
    ourseedrun4 = [50, 54, 47, 44, 47, 47, 44, 51, 47, 47]

    ouravg = []
    randomavg =[]
    xes = [1,2,3,4,5,6,7,8,9,10]
    for i in range(10):
        temp1 = (ourseedrun0[i]+ourseedrun1[i]+ourseedrun2[i]+ourseedrun3[i]+ourseedrun4[i])/5
        temp2 = (random0[i]+random1[i]+random2[i]+random3[i]+random4[i])/5
        ouravg.append(temp1)
        randomavg.append(temp2)
    if method == "Random":
        plt.plot(xes, randomavg, '-o', label = "Random Averages")
    else: plt.plot(xes, ouravg, '-o', label = "Our Seeds Averages")
    plt.xlabel("Iterations")
    plt.ylabel("Average Cost over 5 Runs")

##Produces our visualizations for our averages over k for both random and our seed
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
    plt.xlabel("k (Number of clusters)")
    plt.ylabel("Average Cost over 3 runs")
    

## Graphs our 12 trajectories returned from our java cluster file
def graphk12Centers():
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
    for i in range(12):
        plt.plot(trajectories[i][0], trajectories[i][1], alpha = .5)




##Plots a scatterplot of all the points, just used as a check(yes all 1.3 million)
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
#clusterroundAverages("Random")
#plt.legend()

graphk12Centers()
#graphAll()
plt.show()