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
void random(); 		//�ü� K �ӴѽL(��) 
void printK();		//��X K �ӴѽL
void printParent();
void select();		//��ܳ��u 2 �ӴѽL
void mating();		//��t 
void mutation();	//���� 
int checkCollision(int k); // �p�ƴѽL Collision
int checkCollision2(int k);
int k_queen[K][N] = {0};	//K �� N x N �ѽL
int k_collision[K] = {0};	//�p�� K �ӴѽL collision
int parentLoc1, parentLoc2; //��X�̨Ψ�ӥ��ѽL
int parentQueen[2][N] = {0}; //��t��
int resCollision[2] = {(N*(N-1))/2, (N*(N-1))/2}; //��t��collision , ��ȳ̰��Ĭ�� 
int MatCount = 0;	//����Φsresult �p��count 
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
		printf("���N���!");
		//printK();
	}else{
		while(resCollision[0]!=0 && resCollision[1]!=0 ){
			printf("��t ");
			mating();
			mutation();
			min = std::min_element(k_collision,k_collision+K);
			if(min==0){
				printf("�ܧγ̨θѡG\n");
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
		printf("\nresult�G\n");
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

void select(){	//��� 
	min = std::min_element(k_collision,k_collision+K);
	parentLoc1 = std::distance(k_collision, min);
		//std::cout << "min element is " << *min << '\n';
		//std::cout << "Index of min element: " << std::distance(k_collision, min) << '\n';
	int minTemp = *min;	//�Ȧs�Ĥ@��min 
	k_collision[std::distance(k_collision, min)] = MAX;
	min = std::min_element(k_collision,k_collision+K);
	parentLoc2 = std::distance(k_collision, min);
	k_collision[parentLoc1] = minTemp;	//�٭�Ĥ@��min 
		//std::cout << "min element is " << *min << '\n';
		//std::cout << "Index of min element: " << std::distance(k_collision, min) << '\n';
		//printf("%d %d\n", parentLoc1, parentLoc2);
}

void mating(){	//��t
	parent_count++;
	int cutLoc = (rand()%(N));
	printf("�����I�G%d\n", cutLoc);
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
	
	for(int i=0; i<N; i++){		//�л\����N 
		if(resCollision[0]<k_collision[parentLoc1]){
			k_queen[parentLoc1][i] = parentQueen[0][i];
			k_collision[parentLoc1] = resCollision[0];
		}else{
			break;
		}
	}
	for(int i=0; i<N; i++){		//�л\����N 
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

void mutation(){	//���� 
	for(int i=0; i<K; i++){
		double mRate = (double)(rand() % 10000) / 10000.0f;
		if(mRate < mutationRate){
			printf("-");
			int mutLoc = rand()%N;		//�M�w���ܦ�m
			int collisionTemp = k_collision[i];	//�Ȧs��Ĭ��
			int dataTemp = k_queen[i][mutLoc];
			k_queen[i][mutLoc] = rand()%N;
			k_collision[i] = checkCollision(i);
			if(collisionTemp<k_collision[i]){	//�Y�쥻���u�л\�^��
				k_queen[i][mutLoc] = dataTemp;
				k_collision[i] = collisionTemp;
			}	
				
		}
	}
}
