# Case-Study-2 

## List of Files
- task_two.java $\rightarrow$ only used for simplification method
- task_three.java $\rightarrow$ only used for DTW 
- task_four.java $\rightarrow$ contains centering methods Approach I and II
-  task_five.java $\rightarrow$ contains Lloyds and seeding algorithms
-  Trajectory, Clustering, Point, Edge, File_methods, and Assignment java files are object types that we use throughout our code
-  The folders with trajectory points (simplified and unsimplified) are used for calculating our centers in task 4
-  The "center" text files are the output of task4 centering methods for Approach I and II, which we use in visualizations
-  The "averages" text files are the output of Lloyd's algorithm which we use to make our visaulization for clustering cost for different values of k
-  Python files contain matplotlib visualizations for task 4 and 5

## Reproducing Experimental Results

- For task 4, change the epsilon value in the main method to see centering cost for both Approach I and Approach II
- To visualize the computed center, run code with desired epsilon and bottom two File_methods lines in main method will write our the data. Then go to task 4 Python file to visualize
- For task 5, run Lloyds in main method with desired seeding and k value. If you would like to see printed costs over each iteration, uncomment the print statement inside Lloyds. 
- For computing averages over different k values, uncomment File_methods.computeAverages() calls at the bottom. This will write out data that you can visualize in task 5 Python
- To visualize actual centers on the map, uncomment File_Methods.createClusterCenterFile() call which will write out to a file name that you specify. This can then be visualize in task 5 Python

