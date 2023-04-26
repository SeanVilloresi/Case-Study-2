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

##if you wish to visualize simplified, simply add the word "simplified" ahead of every "trajectory" (for all 3 times its stated)
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
plt.plot(x_coordscenter1, y_coordscenter1, '-o', zorder = 3, color = "blue", markersize = 7, alpha = 0.3)
plt.plot(x_coordscenter2, y_coordscenter2, '-o', zorder = 3, color = "red", markersize = 7, alpha = 0.3)


for i in range(10):
    plt.plot(trajectories[i]['x'], trajectories[i]['y'], '-o', alpha = .3, color = "yellow", markersize = 10)

plt.title('Comparing Approaches 1 and 2 (Epsilon: 0.3)')
plt.legend(['Approach 1 (Brute Force)', 'Approach 2', 'Original Trajectories'])
plt.show()


