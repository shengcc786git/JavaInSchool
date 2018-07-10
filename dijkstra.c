#include <stdio.h>
#include <stdlib.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
//浮木兩端點結構
struct point{	 
	int X;
	int Y;
};

int N;			//浮木個數 
//輸入浮木兩端點數值之副程式
void inputPoint(){ 
	struct point Point[N];	//N個浮木
	int x, y;
	for(int i=0; i<N; i++){
		do{
			scanf("%d %d", &x, &y);
			if(x >= y || x < 1 || y > 10000)
				printf("輸入數值不符規定(1 <= X < Y <= 10000)，請重新輸入\n");
		}while(x >= y || x < 1 || y > 10000);
		
		Point[i].X = x;
		Point[i].Y = y;
	}

	buildMatrix(Point);
}	
//建立相鄰矩陣之副程式
void buildMatrix(struct point Point[N]){
	int adjMatrix[N][N];	//相鄰矩陣 
	//初始化 相鄰矩陣
	for(int i=0; i<N; i++){ 
		for(int j=0; j<N; j++){
			adjMatrix[i][j] = 0;
		}
	}
	//建立 相鄰矩陣
	for(int i=0; i<N; i++){
		for(int j=0; j<N; j++){
			if((Point[j].X >= Point[i].X && Point[j].X <= Point[i].Y)
				||(Point[j].Y >= Point[i].X && Point[j].Y <= Point[i].Y)){
				adjMatrix[i][j] = 1;
			}
		}
	}

	dijkstra(adjMatrix);
}
//利用 dijkstra algo 找出第一個浮木至個浮木最短路徑長度 
void dijkstra(int adjMatrix[N][N]){
	int costMatrix[N][N];	//成本矩陣
	int distMatrix[N];		//第一個浮木至各浮木目前之最短路徑長度
	int determine[N];		//確定第一個浮木至各浮木之最短路徑長度 1:ok 0:Not yet 
	//初始化matrix
	for(int i=0; i<N; i++){ 
		for(int j=0; j<N; j++){
			if(adjMatrix[i][j]!=0 && i!=j)
				costMatrix[i][j] = 1;
			else if(i==j)
				costMatrix[i][j] = 0;
			else
				costMatrix[i][j] = 9999;
		}
	}

	for(int i=0; i<N; i++){ 
		distMatrix[i] = costMatrix[0][i];
		determine[i] = 0;
	}
	distMatrix[0] = 0; //起點自身 
	determine[0] = 1;

	int step = 2;
	int minVertex;
	int minValue = 9999;
	
	while(step < N){
		for(int i=0; i<N; i++){
			if(determine[i]==0 && distMatrix[i] < minValue){
				minValue = distMatrix[i];
				minVertex = i;
			}
		}
		determine[minVertex] = 1;
		
		for(int i=0; i<N; i++){
			if(determine[i]==0){
				int distTemp = distMatrix[minVertex] + costMatrix[minVertex][i];
				if(distTemp < distMatrix[i])
					distMatrix[i] = distTemp;
			}
		}
		step++;
		minValue=9999;
	}
 
	printf("∴第一根浮木至最後一根浮木，最少跳躍次數：%d", distMatrix[N-1]);
}

int main(int argc, char *argv[]) {
	//輸入浮木個數
	do{
		printf("請輸入浮木個數(2<=N<=1000)：");
		scanf("%d", &N);
		if(N < 2 || N > 1000)
			printf("浮木個數請輸入2~1000，請重新輸入\n");
		else
			printf("您輸入的數字：%d\n", N);
	}while(N < 2 || N > 1000);
	
	printf("以下請輸入各浮木兩端點，兩數值請用空白分開\n");
	inputPoint();

	return 0;
}
