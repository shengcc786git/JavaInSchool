#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#define N 8
/* run this program using the console pauser or add your own getch, c or input loop */
void random();				//亂數 
int check_conflict(int*);	//計算衝突數 
void print();				//輸出 
void search();				//尋找最佳解 
int queen[N] = {0};
int conflict = 0;
int worst_count = (N*(N-1))/2;

int main(int argc, char** argv) {
	random();
	//check_conflict(queen);
	if(conflict!=0)
		search();
	print();
	return 0;
}
void search(){
	int min = conflict, min_status[2] = {0}, min_count = 0;
	
	for(int i=0; i<N; i++){
		int j_temp = queen[i];
		for(int j=0; j<N; j++){
			if(j != j_temp){
				queen[i] = j;	//換位 check衝突數 
				int min_temp = check_conflict(queen);
				if(min > min_temp){
					min_count = 1;
					min = min_temp;
					min_status[0] = i;
					min_status[1] = j;
				}else if(min==min_temp){
					min_count++;
					min_status[0] = i;
					min_status[1] = j;
				}
			}
		}
		queen[i] = j_temp;
	}
	
	if(min_count > 1){
		random();
		if(check_conflict(queen)!=0)
			search();
	}else{
		queen[min_status[0]] = min_status[1];
		//printf("■■■\n"); 
			print();
		//system("pause"); 
		if(check_conflict(queen)!=0)
			search();
	}
	
}
void random(){
	srand(time(NULL));
	do{
		for(int i=0; i<N; i++){
			queen[i] = (rand()%N);
		}
	}while(check_conflict(queen)==worst_count);	
	printf("-\n");
	print();	
}

int check_conflict(int queen[N]){
	conflict = 0;
	for(int i=0; i<N; i++){
		for(int j=i+1; j<N; j++){
			if(queen[i]==queen[j])
				conflict++;
			if(abs(i-j) == abs(queen[i]-queen[j]))
				conflict++;
		}
	}
	//printf("%d\n",conflict);
	return conflict;
}
void print(){
	for(int i=0; i<N; i++){
		for(int j=0; j<N; j++){
			printf("%d", queen[i]==j ? 1 : 0);
		}
		printf("\n");
	}
	printf("\n");
}
