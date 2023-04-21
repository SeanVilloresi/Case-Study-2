import numpy as np
import matplotlib.pyplot as plt
import os
import glob

## Task Four Visualization

x_coordscenter1 = []
y_coordscenter1 = []

##We could just make 10 seperate x and y arrays but that feels inefficient but also whocares?

with open("center1.txt", 'r') as my_file:
    for line in my_file:
        values = line.strip().split(', ')
        x = float(values[0])
        y = float(values[1])
        x_coordscenter1.append(x)
        y_coordscenter1.append(y)

text_files = glob.glob(os.path.join("trajectory-points", '*.txt'))

##read in all the trajectories
for file in text_files:
    with open(file, 'r') as my_file:


## Question is what is best way to store all the data for the other points
