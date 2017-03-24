/**
 * 
 * @author Yingyi Zhang
 * @username yxz559
 * @studentID 1544458
 * 
 */


******       Project Name        ******

Recovery of Graph Data from Academic Publications.



******   Project Main Purposes   ******

1. Extract images from PDF files
2. Divide combined images
3. Classify graphs from images
4. Recover data from graphs
5. Detect data plagiarism



******  How to run this program  ******

In order to run this program, you will need
to download the following java libraries and
set up the environment following some tutorials:

1. PDFBox

https://pdfbox.apache.org/

2. OpenCV

http://opencv.org/

You may also need to build the OpenCV 
before using it, following the tutorial:
http://opencv-java-tutorials.readthedocs.io/en/
latest/01-installing-opencv-for-java.html

3. Tess4J

http://tess4j.sourceforge.net/

You need to put the tessdata/ folder under the
project's root directory, in this project it is
already there.

4. CommonMath

http://commons.apache.org/proper/commons-math/

All the libraries are open-source and free to use.

Then you can run the program by running the file:
DataRecovery/src/view/DataPlagiarismDetect.java



******     Project Structure     ******

DataRecovery
| - src
|
|   -- view
|      --- DataPlagiarismDetect.java (Program entry)
|      --- ProgramFrame.java
|
|   -- model
|      --- GraphPoint.java
|      --- Graph.java
|
|   -- processPDF
|      --- PDFProcessor.java
|      --- PDFProcessorThreadPool.java
|      --- PDFImageExtractor.java
|
|   -- processImage
|      --- ImageProcessor.java
|      --- ImageDivision.java
|      --- ImageDivisionThreadPool.java
|      --- ImageOperations.java
|
|   -- processGraph
|      --- GraphProcessor.java
|      --- GraphClassifier.java
|      --- GraphDataRecover.java
|      --- AxisDetector.java
|      --- DataRecover.java
|      --- PlagiasirmDetect.java
|      --- PPCsCalculator.java
|
|   -- junitTest
|      --- All the JUnit test file
|
| - PDFs
|   -- All the PDF files
|
| - images
|   -- Images that are extracted from PDFs
|
| - divImages
|   -- Divided images
|
| - graphs
|   -- Graphs that are classified from images
|
| - classifierTraining
|   -- Positive and negative training samples, useful tools
|
| - trainedClassifier
|   -- 5 different trained classifiers
|
| - tessdata
|   -- OCR(Optical Character Recognition) sample data, provided by Google tesseract
|
| - recoveredGraphs
|   -- All the recovered graphs and their data.txt files
|
| - testData
|   -- Data and files used for testing
       