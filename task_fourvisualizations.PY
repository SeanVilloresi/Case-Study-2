import numpy as np
import matplotlib.pyplot as plt
import os
import glob

## Task Four Visualization

x_coordscenter1 = []
y_coordscenter1 = []
x_coords0 = []
y_coords0 = []
x_coords1 = []
y_coords1 = []
x_coords2 = []
y_coords2= []
x_coords3 = []
y_coords3 = []
x_coords4 = []
y_coords4 = []
x_coords5 = []
y_coords5 = []
x_coords6 = []
y_coords6 = []
x_coords7 = []
y_coords7 = []
x_coords8 = []
y_coords8 = []
x_coords9 = []
y_coords9 = []
x_coords10 = []
y_coords10 = []

##We could just make 10 seperate x and y arrays but that feels inefficient but also whocares?

with open("center1.txt", 'r') as my_file:
    for line in my_file:
        values = line.strip().split(', ')
        x = float(values[0])
        y = float(values[1])
        x_coordscenter1.append(x)
        y_coordscenter1.append(y)

text_files = glob.glob(os.path.join("trajectory-points", '*.txt'))
trajectories = {}
##read in all the trajectories
for file in text_files:
    with open(file, 'r') as my_file:
        #print(file)
        if file.endswith('.txt'):
            traj_num = int(file.replace('trajectory-points\\trajectory', '').replace('.txt', ''))
            
            a = []
            b = []
        for line in my_file:
            
            values = line.strip().split(', ')
            
            x = float(values[0])
            y = float(values[1])
            a.append(x)
            b.append(y)

    trajectories[traj_num] = {'x' : a, 'y' : b}

#print(trajectories)
plt.plot(x_coordscenter1, y_coordscenter1, '-o')

for i in range(10):
    plt.plot(trajectories[i]['x'], trajectories[i]['y'], '-o')

plt.show()


## Question is what is best way to store all the data for the other points
