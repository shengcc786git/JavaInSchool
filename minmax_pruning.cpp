#include <iostream>
#include <stdio.h>      
#include <stdlib.h>
#include <fstream>
#include <string>
#include <string.h>
#include <math.h>
#include <vector>
/* run this program using the console pauser or add your own getch, system("pause") or input loop */
using namespace std;
int treeBranch, treeHeight;

struct minmaxTree{
	int value;
	int location;
	int depth;
	int branch;
	struct minmaxTree *parent;
	struct minmaxTree *child[3];
};
vector<struct minmaxTree*>showNode;
void bfs(struct minmaxTree *treeTemp){//bfs輸出tree 
	int i=0;
	int total = 0;
	showNode.push_back(treeTemp);
	printf("[depth:%d node:%d loc:%d]\n", treeTemp->depth, treeTemp->value, treeTemp->location);
	total += treeTemp->value;
	while(!showNode.empty()){
		for(int i=0; i<treeBranch; i++){
			showNode.push_back(treeTemp->child[i]);
			printf("[depth:%d node:%d loc:%d parent(loc):%d]", 
			treeTemp->child[i]->depth, treeTemp->child[i]->value, treeTemp->child[i]->location, treeTemp->child[i]->parent->location);
			//printf("[node:%d loc:%d]", treeTemp->child[i]->value, treeTemp->child[i]->location);
			printf("\n");
			if(treeTemp->child[i]->depth==treeHeight){
				total += treeTemp->child[i]->value;
			//	printf("t:%d",total);	
			}
			
		}
		printf("\n");
		showNode.erase(showNode.begin());
		treeTemp = showNode[0];
		
	}

}
int Get_minmax2(int value, struct minmaxTree *treeTemp){//取min or max 
	int min = treeTemp->child[0]->value, max = treeTemp->child[0]->value;
	if(value==0){
		for(int i=0; i<treeBranch; i++){
			if(max<treeTemp->child[i]->value)
				max = treeTemp->child[i]->value;
		}
		return max;
	}else{
		for(int i=0; i<treeBranch; i++){
			if(min>treeTemp->child[i]->value)
				min = treeTemp->child[i]->value;
		}
		return min;
	}
}
int Get_minmax(int value, int a, int b){//取min or max 
	if(value==0){//max
		if(a>b)
			return a;
		else
			return b;
	}else{//min
		if(a<b)
			return a;
		else
			return b;
	}
}
vector<int>testData;
vector<int>prune_node;
vector<struct minmaxTree*>curNode;
int nodeNum = 0, nodeLoc = 0, testLoc = 0, dfs_branh=0;

int dfs_minmax(struct minmaxTree *treeTemp,  int alpha, int beta){	//dfs賦予值
	//printf("[a:%d b:%d]\n", alpha, beta);
	if(treeTemp->value==-9999){
		if(treeTemp->depth!=treeHeight-1){
			for(int i=0; i<treeBranch; i++){
				if(beta<=alpha){
						if((treeTemp->depth)%2==0)
							for(int j=i; j<treeBranch; j++){
								treeTemp->child[j]->value = -99999;
								prune_node.push_back(treeTemp->child[j]->location);
							}		 
						else
							for(int j=i; j<treeBranch; j++){
								treeTemp->child[j]->value = 99999;
								prune_node.push_back(treeTemp->child[j]->location);
							}
						break;
				}else{
					if((treeTemp->depth)%2==0){//max
						dfs_branh++;
						//alpha = dfs_minmax(treeTemp->child[i], alpha, beta)
						alpha = Get_minmax(0, alpha, dfs_minmax(treeTemp->child[i], alpha, beta));
					}else{//min
						dfs_branh++;
						//beta = dfs_minmax(treeTemp->child[i], alpha, beta)
						beta = Get_minmax(1, beta, dfs_minmax(treeTemp->child[i], alpha, beta));	
					}
				} 
				
			}
			int min = treeTemp->child[0]->value, max = treeTemp->child[0]->value;
			if((treeTemp->depth)%2==0){//max
				for(int i=1; i<treeBranch; i++){
					min = Get_minmax(0, min, treeTemp->child[i]->value);
				}
				treeTemp->value = min;
				return Get_minmax(0, min, alpha);
			}else{
				for(int i=1; i<treeBranch; i++){
					max = Get_minmax(1, max, treeTemp->child[i]->value);
				}
				treeTemp->value = max;
				return Get_minmax(1, max, beta);
			}
		}else{
			dfs_branh+=treeBranch;
			int min = treeTemp->child[0]->value;
			beta = min;
			printf("a:%d b:%d", alpha, min);
			for(int i=1; i<treeBranch; i++){
				if(beta<=alpha){
					for(int j=i; j<treeBranch; j++){
						dfs_branh--;
						treeTemp->child[j]->value = -99999;
						prune_node.push_back(treeTemp->child[j]->location);
					}
					break;
				}else{
					min = Get_minmax(1, min, treeTemp->child[i]->value);
					beta = min;
				}	
			}
			treeTemp->value = min;
			printf("a:%d b:%d", alpha, min);
			return treeTemp->value;//beta
		}		
	}
} 
void build_Ntree(int depth){//建空tree 
	if(!curNode.empty()){
		struct minmaxTree *node;
		node = (struct minmaxTree*)malloc(sizeof(struct minmaxTree));
		if(depth==treeHeight-1){
			node->value = testData[testLoc++];
		}else{
			node->value = -9999;	
		}
		node->depth = depth+1; 
		node->branch = 0;
		node->location = ++nodeNum;
		curNode.push_back(node);
		
		curNode[nodeLoc]->child[((node->location+treeBranch-1)%treeBranch)] = node;
		curNode[nodeLoc]->child[((node->location+treeBranch-1)%treeBranch)]->parent = curNode[nodeLoc];
		curNode[nodeLoc]->branch++;
		
		if(curNode[nodeLoc]->branch==treeBranch){
			nodeLoc++;
		}	 
	}
}
void readFile(){
	fstream FileData;
	FileData.open("測試檔.txt",ios::in);
	char line[9999], *tmp;
	const char *seg = ",";
	int lineCount = 0;
	while(!FileData.eof()){
		FileData.getline(line,sizeof(line));
		//printf("[%s]",line);
		lineCount++;
		if(lineCount==1){
			treeBranch = atoi(line);
			printf("treeBranch:%d\n",treeBranch);
		}else if(lineCount==2){
			treeHeight = atoi(line);
			printf("treeHeight:%d\n",treeHeight);
		}else{
			tmp = strtok(line,seg);
			while(tmp){
				testData.push_back(atoi(tmp));
			//	printf("[%s]",tmp);
				tmp = strtok(NULL,seg);
			}
		}
			
	}
	FileData.close();
	for(int i=0; i<testData.size(); i++)
		printf("%d ",testData[i]);
	printf("\n");
}
int main(int argc, char** argv) {
	readFile();

	struct minmaxTree *minmax;
	minmax = (struct minmaxTree*)malloc(sizeof(struct minmaxTree));
	minmax->value = -9999;
	minmax->branch = 0;
	minmax->location = 0;
	minmax->depth = 0;
	curNode.push_back(minmax);
	//printf("%d",curNode[0]->location);
	for(int depth=0; depth<treeHeight; depth++){
		for(int j=0; j<int(pow(treeBranch,depth+1)); j++){
			build_Ntree(depth);
		}	
	}

	//printf("[%d]",Get_minmax(1,0,5));
	dfs_minmax(minmax, -999999, 999999);
	printf("Branch:%d\n被修剪的位置：",dfs_branh);
	for(int i=0; i<prune_node.size(); i++)
		printf("%d ",prune_node[i]);
	printf("\n");
	bfs(minmax);
	
	return 0;
}
