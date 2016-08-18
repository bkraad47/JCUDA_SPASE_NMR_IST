This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
    
    by Badruddin Kamal developed as part of an update to my ANU COMP8715 Project


More details at -https://cs.anu.edu.au/courses/csprojects/15S1/Reports/Badruddin_Kamal_Report.pdf

	------------------------------------------------------------------------------	
	ITTERATIVE SOFT THRESHOLDING(IST) FOR SPARSE NUCLEAR MAGNETIC RESONANCE(NMR):
	------------------------------------------------------------------------------
	Requirements:
	JAVA-8
	CUDA 7.5
	JCUDA 0.7.5b
	Netbeans

	To Make & Run:

	Always set Java VM run options to 
       -Djava.library.path="PATH/TO/JCUDA/Library"

	Also include JCUDA library options found here-http://www.jcuda.org/tutorial/TutorialIndex.html

	However, if using netbeans the 2nd option isn't required, if you just include the JARS in build path.

The required JCUDA files can also be found in the Libs folder.
Required JAR's and DLL's:
"JCUDA"
"JCUFFT"
		
		Now, Just compile and run JAR with appropriate VM run options

		We set the IN/OUT files.

		In the GUI for dimensions we insert the original nx*ny dimensions

		For the best performance iterations should be > 200 and 0.6 > thereshold < 0.9 (Comparing to 128*128 grid)

		Click calculate.
		
		Data will be written in output file and spectra contours shown
            
	To Use:

	IN/OUT File format per line
	x,y,I

	Note*- In the in file we take the sparse file of a nx*ny(Ignoring Sigma) NMR Data in a 2nx*2ny grid in the time domain(This data will be sparse in the Time 		Domain Not Frequency Domain). This will generate the output file in the original nx*ny domain.

	To Test:
       		Use sample_sparse_NMR.txt(sparsified version of sample_NMR.txt in **time domain**) as input file and output file istout.txt