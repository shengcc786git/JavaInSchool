import java.awt.Color;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
public class hw3 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		BufferedImage image = ImageIO.read(new File("t3.jpg"));
		int width = image.getWidth();
		int height = image.getHeight();
		int[] pixel = image.getRGB(0, 0, width, height, null, 0, width);
		System.out.println("width:"+width+"height:"+height);
		
		int group_val = 5;//分群數目
		Color[] center = new Color[group_val];//群中心點array
		for(int i=0; i<center.length; i++){//第一次取群中心點,取random
				center[i] = new Color(pixel[(int)(Math.random()*(width*height))]);
				System.out.println(center[i]+" ");
		}
		
		ArrayList<ArrayList<Integer>> groups = new ArrayList<ArrayList<Integer>>();//群
		for(int i=0; i<group_val; i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			groups.add(temp);
		}

		for(int i=0; i<pixel.length; i++){
			ArrayList<Double> distant_List = new ArrayList<Double>();//距離k群長度list
			Color color_temp = new Color(pixel[i]);
			for(int j=0; j<center.length; j++){
				double distance = Math.sqrt(Math.pow((center[j].getRed()-color_temp.getRed()),2)+
					Math.pow((center[j].getGreen()-color_temp.getGreen()),2) + Math.pow((center[j].getBlue()-color_temp.getBlue()),2));
				distant_List.add(distance);
				
			}
			int group_num = distant_List.indexOf(Collections.min(distant_List));//取min
			groups.get(group_num).add(i);
			distant_List.clear();
		}
		//System.out.println("groups size:"+groups.size()+" ");
		int count=0;
		int[][] newCenter = new int[group_val][3]; //New群中心
		int R=0, G=0, B=0;
		for(int i=0; i<groups.size(); i++){
			for(int j=0; j<groups.get(i).size(); j++){
				Color color_temp = new Color(pixel[groups.get(i).get(j)]);
				R += color_temp.getRed();
				G += color_temp.getGreen();
				B += color_temp.getBlue();
				count++;
			}
			newCenter[i][0] = R / (groups.get(i).size()+1);
			newCenter[i][1] = G / (groups.get(i).size()+1);
			newCenter[i][2] = B / (groups.get(i).size()+1);
			R=0;
			G=0;
			B=0;
			//System.out.println("c:"+count+"R:"+newCenter[i][0]+"G:"+newCenter[i][1]+"B:"+newCenter[i][2]);
		}
		//System.out.println("newCenetr size:"+newCenter.length);
		/*------------------------------------while分群------------------------------------*/
		ArrayList<ArrayList<Integer>> groups_2 = new ArrayList<ArrayList<Integer>>();//新群做比較
		for(int i=0; i<group_val; i++){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			groups_2.add(temp);
		}
		
		boolean Groups_chang = false;
		while(!Groups_chang){
			for(int i=0; i<pixel.length; i++){
				ArrayList<Double> distant_List = new ArrayList<Double>();//距離k群長度list
				Color color_temp = new Color(pixel[i]);
				for(int j=0; j<newCenter.length; j++){
					double distance = Math.sqrt(Math.pow((newCenter[j][0]-color_temp.getRed()),2)+
						Math.pow((newCenter[j][1]-color_temp.getGreen()),2) + Math.pow((newCenter[j][2]-color_temp.getBlue()),2));
					//System.out.println("dis:"+distance+" ");
					distant_List.add(distance);
					
				}
				int group_num = distant_List.indexOf(Collections.min(distant_List));//取min
				//System.out.println("group_num:"+group_num+" ");
				groups_2.get(group_num).add(i);
				distant_List.clear();
			}
			
			//System.out.println("check_val:"+check_val);
			if(groups.equals(groups_2)){
				Groups_chang = true;
			}else{
				//System.out.println("False");
				for(int i=0; i<groups.size(); i++){
					groups.get(i).clear();
				}
				
				for(int i=0; i<groups_2.size(); i++){
					for(int j=0; j<groups_2.get(i).size(); j++){
						groups.get(i).add(groups_2.get(i).get(j));
					}
				}

				for(int i=0; i<groups_2.size(); i++)
					groups_2.get(i).clear();
				
					
						
				/*------------------------------------NewColor------------------------------------*/
				count=0;
				for(int i=0; i<groups.size(); i++){
					for(int j=0; j<groups.get(i).size(); j++){
						Color color_temp = new Color(pixel[groups.get(i).get(j)]);
						R += color_temp.getRed();
						G += color_temp.getGreen();
						B += color_temp.getBlue();
						count++;
						//System.out.println("C"+count+"R:"+R+"G:"+G+"B:"+B);
					}
					//System.out.println("R:"+R+"G:"+G+"B:"+B);
					newCenter[i][0] = R / (groups.get(i).size()+1);
					newCenter[i][1] = G / (groups.get(i).size()+1);
					newCenter[i][2] = B / (groups.get(i).size()+1);
					//System.out.println("R:"+newCenter[i][0]+"G:"+newCenter[i][1]+"B:"+newCenter[i][2]);
					R=0;
					G=0;
					B=0;
					//System.out.println("c:"+count+"R:"+newCenter[i][0]+"G:"+newCenter[i][1]+"B:"+newCenter[i][2]);
				}
				//System.out.println("newCenetr size:"+newCenter.length);
			}
			//System.out.println("?:"+groups_2.get(2).size());
		}
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for(int i=0; i<groups_2.size(); i++){
			for(int j=0; j<groups_2.get(i).size(); j++){
				int X = groups_2.get(i).get(j)/width;
				int Y = groups_2.get(i).get(j)%width;
				//System.out.println("a"+(a++)+"X:"+X+"Y:"+Y);
				Color color_temp = new Color(newCenter[i][0], newCenter[i][1], newCenter[i][2],255);
				//System.out.println("color_temp::"+color_temp);
				newImage.setRGB(Y, X, color_temp.getRGB());
			}
		}
		ImageIO.write(newImage, "JPG", new File("NewImage.jpg"));
		System.out.println("OK!");
	}	
}