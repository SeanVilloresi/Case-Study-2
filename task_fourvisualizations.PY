import numpy as np
import matplotlib.pyplot as plt
import os
import glob

## Task Four Visualization

x_coordscenter1 = []
y_coordscenter1 = []
x_coordscenter2 = []
y_coordscenter2 = []


##We could just make 10 seperate x and y arrays but that feels inefficient but also whocares?

with open("center1.txt", 'r') as my_file:
    for line in my_file:
        values = line.strip().split(', ')
        x = float(values[0])
        y = float(values[1])
        x_coordscenter1.append(x)
        y_coordscenter1.append(y)
with open("center2.txt", 'r') as my_file:
    for line in my_file:
        values = line.strip().split(', ')
        x = float(values[0])
        y = float(values[1])
        x_coordscenter2.append(x)
        y_coordscenter2.append(y)

text_files = glob.glob(os.path.join("simplifiedtrajectory-points", '*.txt'))
trajectories = {}
##read in all the trajectories
for file in text_files:
    with open(file, 'r') as my_file:
        #print(file)
        if file.endswith('.txt'):
            traj_num = int(file.replace('simplifiedtrajectory-points\\simplifiedtrajectory', '').replace('.txt', ''))
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
plt.plot(x_coordscenter2, y_coordscenter2, '-o', zorder = 3)

for i in range(10):
    plt.plot(trajectories[i]['x'], trajectories[i]['y'], '-o', alpha = .5)

plt.show()


## Question is what is best way to store all the data for the other points
