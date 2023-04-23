import numpy as np
import matplotlib.pyplot as plt
import os
import glob
import csv

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

for i in range(10):
    plt.plot(trajectories[i][0], trajectories[i][1], '-o', alpha = .5)




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

plt.show()