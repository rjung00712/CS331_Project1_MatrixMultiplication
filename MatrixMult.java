/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixmult;
import java.util.Random;

public class MatrixMult {

    public static final int N = 2;
    
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        
        int [][] arrayA = new int[N][N];
        int [][] arrayB = new int[N][N];
        int [][] arrayC = new int[N][N];
        
        Random rand = new Random();
        
//        int [][] arrayA = {{1, 1, 1, 1},{2, 2, 2, 2}, {3, 3, 3, 3}, {2, 2, 2, 2}};
//        int [][] arrayB = {{2, 2, 2, 2},{1, 1, 1, 1}, {0, 0, 0, 0}, {1, 1, 1, 1}};
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                arrayA[i][j] = 2;
                arrayB[i][j] = 2;
            }
        }
        
//        arrayC = traditionalMult(N, arrayA, arrayB, arrayC);
        arrayC = divAndConqMult(N, arrayA, arrayB);
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                System.out.print(arrayC[i][j] + "\t");
                if(j == N-1)
                    System.out.println();
            }
        }
    }
    
    // traditional way of matrix multiplication
    /**
     *
     * @param N
     * @param A
     * @param B
     * @param C
     */
    // traditional method matrix multiplication
    public static int [][] traditionalMult(int N, int [][] A, int [][] B, int [][] C) {
        
        for(int i = 0; i < N; i++) {        // n passes on the current row
            for(int j = 0; j < N; j++) {    // n passes on the current column
                C[i][j] = 0;                // initialize the value to 0
                for(int k = 0; k < N; k++)  
                    // sop of current row of A and column of B
                    C[i][j] = C[i][j] + A[i][k] * B[k][j];
            }
        }       
        return C;
    }  
    // recursive divide-and-conquer matrix mutiplication
    public static int [][] divAndConqMult(int N, int [][] A, int [][] B) {
        int[][] C = new int[N][N];  // resulting matrix
        int n = N/2;  // size of partition matrix
        
        if(N == 1){ // base case
            C[0][0] = A[0][0] * B[0][0];
            return C;
        }
            
        // partition the matrix into submatrices
        int [][] a11 = new int[n][n];   // top left
        int [][] a12 = new int[n][n];   // top right
        int [][] a21 = new int[n][n];   // bottom left
        int [][] a22 = new int[n][n];   // bottom right
              
        int [][] b11 = new int[n][n];   // top left
        int [][] b12 = new int[n][n];   // top right
        int [][] b21 = new int[n][n];   // bottom left
        int [][] b22 = new int[n][n];   // bottom right
        
        int [][] c11 = new int[n][n];
        int [][] c12 = new int[n][n];
        int [][] c21 = new int[n][n];
        int [][] c22 = new int[n][n];
        
        // copy to partition arrays
        for(int i = 0; i < n; i++) {  // row 
            for(int j = 0; j < n; j++) {  // column
                a11[i][j] = A[i][j];    // top left a
                b11[i][j] = B[i][j];    // top left b
                    
                a12[i][j] = A[i][j + n];    // top right a
                b12[i][j] = B[i][j + n];    // top right b
                
                a21[i][j] = A[i + n][j];    // bottom left a
                b21[i][j] = B[i + n][j];    // bottom left b
                
                a22[i][j] = A[i + n][j + n];    // bottom right a
                b22[i][j] = B[i + n][j + n];    // bottom right b
            }
        }
        // recursive calls of partitions of array
        c11 = addMatrices(n, divAndConqMult(n, a11, b11), divAndConqMult(n, a12, b21));
        c12 = addMatrices(n, divAndConqMult(n, a11, b12), divAndConqMult(n, a12, b22));
        c21 = addMatrices(n, divAndConqMult(n, a21, b11), divAndConqMult(n, a22, b21));
        c22 = addMatrices(n, divAndConqMult(n, a21, b12), divAndConqMult(n, a22, b22));
        
        // fill up the resulting array
        for(int i = 0; i < n; i++)
            for(int j = 0; j < n; j++)
                C[i][j] = c11[i][j];
        for(int i = n; i < 2*n; i++) 
            for(int j = 0; j < n; j++)
                C[i][j] = c21[i-n][j];
        for(int i = 0; i < n; i++)
            for(int j= n; j < 2*n; j++)
                C[i][j] = c12[i][j-n];
        for(int i = n; i < 2*n; i++)
            for(int j = n; j < 2*n; j++)
                C[i][j] = c22[i-n][j-n];
            
        return C;   // return result
    }
    
//    public static int[][] strassenMult(int N, int[][] A, int [][] B) {
//        if(N == 2) {
//            
//        }
//    }
    
    public static int[][] addMatrices(int N, int[][] A, int[][] B) {
        int[][] C = new int[N][N];  // resulting matrix
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }
}
