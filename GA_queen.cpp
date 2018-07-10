#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <algorithm>
#define K 100
#define N 8 
#define MAX 9999
#define mutationRate 0.001 

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
void random(); 		//亂數 K 個棋盤(母) 
void printK();		//輸出 K 個棋盤
void printParent();
void select();		//選擇最優 2 個棋盤
void mating();		//交配 
void mutation();	//突變 
int checkCollision(int k); // 計數棋盤 Collision
int checkCollision2(int k);
int k_queen[K][N] = {0};	//K 個 N x N 棋盤
int k_collision[K] = {0};	//計數 K 個棋盤 collision
int parentLoc1, parentLoc2; //選出最佳兩個母棋盤
int parentQueen[2][N] = {0}; //交配用
int resCollision[2] = {(N*(N-1))/2, (N*(N-1))/2}; //交配後collision , 初值最高衝突數 
int MatCount = 0;	//交替用存result 計數count 
int *min;
int parent_count = 0;

int main(int argc, char** argv) {
	random();
		//print();
	select(); 
		//int *min = std::min_element(k_collision,k_collision+K);
		//std::cout << "min element is " << *min << '\n';
		//std::cout << "Index of min element: " << std::distance(k_collision, min) << '\n';
	if(k_collision[parentLoc1]==0||k_collision[parentLoc2]==0){
		printf("母代找到!");
		//printK();
	}else{
		while(resCollision[0]!=0 && resCollision[1]!=0 ){
			printf("交配 ");
			mating();
			mutation();
			min = std::min_element(k_collision,k_collision+K);
			if(min==0){
				printf("變形最佳解：\n");
				int Loc = std::distance(k_collision, min);
				for(int i=0; i<N; i++){
					for(int j=0; j<N; j++){
						printf("%d", k_queen[Loc][i] == j ? 1 : 0);
					}
					printf("\n");
				}
				printf("\n");
				break;
			}
			select();
			//MatCount++;
		}
		printf("\nresult：\n");
		printParent();
		printf("parent_count:%d",parent_count);
	}	 
	return 0;
}
 
void random(){
	srand(time(NULL));
	for(int i=0; i<K; i++){
		for(int j=0; j<N; j++){
			k_queen[i][j] = (rand()%N);
		}
		k_collision[i] = checkCollision(i);
	}	
}

void printK(){
	for(int k=0; k<K; k++){
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				printf("%d", k_queen[k][i] == j ? 1 : 0);
			}
			printf("\n");
		}
		printf("\n");
	}
	printf("\n");
	
}

int checkCollision(int k){
	int conflict = 0;
	for(int i=0; i<N; i++){
		for(int j=i+1; j<N; j++){
			if(k_queen[k][i]==k_queen[k][j])
				conflict++;
			if(abs(i-j) == abs(k_queen[k][i]-k_queen[k][j]))
				conflict++;
		}
	}
	//printf("%d\n",conflict);
	return conflict;
}

void select(){	//選擇 
	min = std::min_element(k_collision,k_collision+K);
	parentLoc1 = std::distance(k_collision, min);
		//std::cout << "min element is " << *min << '\n';
		//std::cout << "Index of min element: " << std::distance(k_collision, min) << '\n';
	int minTemp = *min;	//暫存第一個min 
	k_collision[std::distance(k_collision, min)] = MAX;
	min = std::min_element(k_collision,k_collision+K);
	parentLoc2 = std::distance(k_collision, min);
	k_collision[parentLoc1] = minTemp;	//還原第一個min 
		//std::cout << "min element is " << *min << '\n';
		//std::cout << "Index of min element: " << std::distance(k_collision, min) << '\n';
		//printf("%d %d\n", parentLoc1, parentLoc2);
}

void mating(){	//交配
	parent_count++;
	int cutLoc = (rand()%(N));
	printf("切割點：%d\n", cutLoc);
		//printParent();
	for(int i=0; i<cutLoc; i++){
		parentQueen[0][i] = k_queen[parentLoc1][i];
		parentQueen[1][i] = k_queen[parentLoc2][i];
	}
	for(int i=cutLoc; i<N; i++){
		parentQueen[0][i] = k_queen[parentLoc2][i];
		parentQueen[1][i] = k_queen[parentLoc1][i];
	}
	
	resCollision[0] = checkCollision2(0);
	resCollision[1] = checkCollision2(1);
	//printParent();
	
	for(int i=0; i<N; i++){		//覆蓋原母代 
		if(resCollision[0]<k_collision[parentLoc1]){
			k_queen[parentLoc1][i] = parentQueen[0][i];
			k_collision[parentLoc1] = resCollision[0];
		}else{
			break;
		}
	}
	for(int i=0; i<N; i++){		//覆蓋原母代 
		if(resCollision[1]<k_collision[parentLoc2]){
			k_queen[parentLoc2][i] = parentQueen[1][i];
			k_collision[parentLoc2] = resCollision[1];
		}else{
			break;
		}
	}
	
	printf("Collision:[%d %d]", resCollision[0], resCollision[1]);
}

void printParent(){
	for(int k=0; k<2; k++){
		for(int i=0; i<N; i++){
			for(int j=0; j<N; j++){
				printf("%d", parentQueen[k][i] == j ? 1 : 0);
			}
			printf("\n");
		}
		printf("\n");
	}
	printf("\n");
}

int checkCollision2(int k){
	int conflict = 0;
	for(int i=0; i<N; i++){
		for(int j=i+1; j<N; j++){
			if(parentQueen[k][i]==parentQueen[k][j])
				conflict++;
			if(abs(i-j) == abs(parentQueen[k][i]-parentQueen[k][j]))
				conflict++;
		}
	}
	//printf("%d\n",conflict);
	return conflict;
}

void mutation(){	//突變 
	for(int i=0; i<K; i++){
		double mRate = (double)(rand() % 10000) / 10000.0f;
		if(mRate < mutationRate){
			printf("-");
			int mutLoc = rand()%N;		//決定突變位置
			int collisionTemp = k_collision[i];	//暫存原衝突數
			int dataTemp = k_queen[i][mutLoc];
			k_queen[i][mutLoc] = rand()%N;
			k_collision[i] = checkCollision(i);
			if(collisionTemp<k_collision[i]){	//若原本較優覆蓋回來
				k_queen[i][mutLoc] = dataTemp;
				k_collision[i] = collisionTemp;
			}	
				
		}
	}
}
