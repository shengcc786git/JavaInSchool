#include <stdio.h>
#include <stdlib.h>

/* run this program using the console pauser or add your own getch, system("pause") or input loop */
//�B�����I���c
struct point{	 
	int X;
	int Y;
};

int N;			//�B��Ӽ� 
//��J�B�����I�ƭȤ��Ƶ{��
void inputPoint(){ 
	struct point Point[N];	//N�ӯB��
	int x, y;
	for(int i=0; i<N; i++){
		do{
			scanf("%d %d", &x, &y);
			if(x >= y || x < 1 || y > 10000)
				printf("��J�ƭȤ��ųW�w(1 <= X < Y <= 10000)�A�Э��s��J\n");
		}while(x >= y || x < 1 || y > 10000);
		
		Point[i].X = x;
		Point[i].Y = y;
	}

	buildMatrix(Point);
}	
//�إ߬۾F�x�}���Ƶ{��
void buildMatrix(struct point Point[N]){
	int adjMatrix[N][N];	//�۾F�x�} 
	//��l�� �۾F�x�}
	for(int i=0; i<N; i++){ 
		for(int j=0; j<N; j++){
			adjMatrix[i][j] = 0;
		}
	}
	//�إ� �۾F�x�}
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
//�Q�� dijkstra algo ��X�Ĥ@�ӯB��ܭӯB��̵u���|���� 
void dijkstra(int adjMatrix[N][N]){
	int costMatrix[N][N];	//�����x�}
	int distMatrix[N];		//�Ĥ@�ӯB��ܦU�B��ثe���̵u���|����
	int determine[N];		//�T�w�Ĥ@�ӯB��ܦU�B�줧�̵u���|���� 1:ok 0:Not yet 
	//��l��matrix
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
	distMatrix[0] = 0; //�_�I�ۨ� 
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
 
	printf("��Ĥ@�گB��̫ܳ�@�گB��A�ָ̤��D���ơG%d", distMatrix[N-1]);
}

int main(int argc, char *argv[]) {
	//��J�B��Ӽ�
	do{
		printf("�п�J�B��Ӽ�(2<=N<=1000)�G");
		scanf("%d", &N);
		if(N < 2 || N > 1000)
			printf("�B��Ӽƽп�J2~1000�A�Э��s��J\n");
		else
			printf("�z��J���Ʀr�G%d\n", N);
	}while(N < 2 || N > 1000);
	
	printf("�H�U�п�J�U�B�����I�A��ƭȽХΪťդ��}\n");
	inputPoint();

	return 0;
}
