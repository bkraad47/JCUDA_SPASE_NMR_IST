/*
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
    
    by Badruddin Kamal developed as part of updates to my ANU COMP8715 Project
*/
    package JCUDAsparseNMR;


    import java.io.BufferedReader;
    import java.io.BufferedWriter;
    import java.io.File;
    import java.io.FileOutputStream;
    import java.io.FileReader;
    import java.io.IOException;
    import java.io.OutputStreamWriter;
    import java.io.Writer;
    import java.nio.charset.StandardCharsets;
    import jcuda.jcufft.*;

    /**
     *
     * @author Badruddin Kamal
     */
    public class JCUDAsparseNMR {

       
       public void calc_ist(int nx,int ny,int iter,float thresh_percent,String file_in,String file_out){
           float[] in=new float[nx*ny];
           float[] work_arr;
           float[] work_final=new float[nx*ny];
           float max_val=0.0f;
           float thresh;
           boolean file_read_flag=false, processing_flag=false;
           cufftHandle plan_forward,plan_backward;
           
           // Read input file
           try {
			File file = new File(file_in);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
                        String [] line_contents;
			while ((line = bufferedReader.readLine()) != null) {
                                line=line.trim();
                                line_contents=line.split(",");
                                int x=Integer.parseInt(line_contents[0].trim());
                                int y=Integer.parseInt(line_contents[1].trim());
                                in[x*ny+y]=Float.parseFloat(line_contents[2].trim());
			}
			fileReader.close();
                        file_read_flag=true;
		} catch (IOException e) {
                        System.out.println("File Read Error!!!");
			e.printStackTrace();
		}
             
                if(file_read_flag){
                  
                  try{ 
                    
                  // Initialize work array
                  work_arr=in.clone();
                    
                  //Ittereation times
                  int iter_count=0;
                  
                  while(iter_count<iter){
                  
                  // Do fft transform  
                  //NOTE** R2C and R2R have same results 
                  plan_forward = new cufftHandle(); 
                  JCufft.cufftPlan1d(plan_forward, nx*ny, cufftType.CUFFT_R2C, 1);
                  JCufft.cufftExecR2C(plan_forward,work_arr, work_arr);
                  JCufft.cufftDestroy(plan_forward);
                  
                  //Format out data & find max data point
                  for(int i=0;i<nx*ny;i++){
                   work_arr[i]=work_arr[i]/( nx * ny*2 );
                   if(work_arr[i]>max_val){
                       max_val=work_arr[i];
                   }
                  }
                  
                  //Define max threshold and chop off chunks
  
                  thresh=max_val*thresh_percent;
                  
                  //chop off peaks and store in out
                  for(int i=0;i<nx*ny;i++){
                    if(work_arr[i]>=thresh){
                      work_final[i] = work_final[i]+ (work_arr[i]-thresh) ;
                      work_arr[i] =thresh;
                    }  
                  }
                  
                  // Do ift transform  
                  //NOTE** R2C and R2R have same results 
                  plan_backward = new cufftHandle(); 
                  JCufft.cufftPlan1d(plan_backward, nx*ny, cufftType.CUFFT_R2C, 1);
                  JCufft.cufftExecR2C(plan_backward,work_arr, work_arr);
                  JCufft.cufftDestroy(plan_backward);
                  
                  //set empty points
                  for(int i=0;i<nx*ny;i++){
                   if(in[i]==0.0f){
                       work_arr[i]=0.0f;
                   }
                  }
                  
                  // initialize max in every itteration
                  max_val=0.0f;
                  
                  //Iteration increment
                  iter_count++;
                  }
                  
                  processing_flag=true;
                  
                } catch (Exception ex) {
                    System.out.println("File Processing Error!!!");
                    ex.printStackTrace();
                }
                 
                }
                if(processing_flag){
                // Write output file
                try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file_out), StandardCharsets.UTF_8))) {
                    for (int i=0; i < nx; i++){
                        for (int j=0; j < ny; j++){ 
                            writer.write(i+" , "+j+" , "+work_final[i*ny+j]+"\n");
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("File Write Error!!!");
                    ex.printStackTrace();
                } 
                }
                
           
       }
    }
