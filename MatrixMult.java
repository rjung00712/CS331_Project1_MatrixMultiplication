/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matrixmult;
import java.util.Random;

public class MatrixMult {
    
    static final int N = 16;     // size of matrix 2^k
    static final int M = 1;    // number of test iterations
    static final int MAX = 9;   
    static final int MIN = -9;
    
    @SuppressWarnings("empty-statement")
    public static void main(String[] args) {
        long start, end, nanoseconds = 0;   // start and end timers
        double avgNanoseconds, milliseconds = 0.0;           
        
        int [][] arrayA = new int[N][N];
        int [][] arrayB = new int[N][N];
        int [][] arrayC = new int[N][N];
        
//        int [][] arrayA = {{1,3,0,1},{1,4,0,2},{1,4,1,3},{2,4,0,4}};
//        int [][] arrayB = {{2,4,1,5},{3,3,1,6},{3,5,6,7},{4,8,0,8}};   
        
        for(int i = 0; i < N; i++) {    // fill arrays A & B with 
            for(int j = 0; j < N; j++) {// random integers ins specified range
                Random rand = new Random();
                arrayA[i][j] = rand.nextInt(MAX - MIN) + MIN;
                arrayB[i][j] = rand.nextInt(MAX - MIN) + MIN;
            }
        }
        ////////////////// traditional multiplication ///////////////////////
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            arrayC = traditionalMult(N, arrayA, arrayB, arrayC);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Traditional Multiplication average milliseconds: ");
        System.out.printf("%.5f", milliseconds);
        System.out.println();
        //////////////////////////////////////////////////////////////////////
        
        avgNanoseconds = milliseconds = 0.0;  // reset
        start = end = nanoseconds = 0;

        ////////////// divide & conquer multiplication ///////////////////////
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            arrayC = divAndConqMult(N, arrayA, arrayB);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Divide & Conquer average milliseconds: ");
        System.out.printf("%.6f", milliseconds);
        System.out.println();
        //////////////////////////////////////////////////////////////////////
        
        avgNanoseconds = milliseconds = 0.0;  // reset
        start = end = nanoseconds = 0;
        
        ////////////////// Strassen multiplication ///////////////////////
        for(int i = 1; i <= M; i++) {   
            start = System.nanoTime();  // start
            arrayC = strassenMult(N, arrayA, arrayB);
            end = System.nanoTime();    // End         
            nanoseconds += (end - start);    
        }
        
        avgNanoseconds = (double)nanoseconds / M;
        milliseconds = avgNanoseconds / 1000000;    // convert nano to milli
       
        System.out.print("Strassen Multiplication average milliseconds: ");
        System.out.printf("%.6f", milliseconds);
        System.out.println();
        //////////////////////////////////////////////////////////////////////
        
        print(arrayC);
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
        
        if(N == 2) {
            traditionalMult(N, A, B, C);
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
    
    public static int[][] strassenMult(int N, int[][] A, int [][] B) {
        int [][] C = new int [N][N];
        
        int n = N/2;  // size of partition matrix
        
        if(N == 2) {
            traditionalMult(N, A, B, C);
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
        
        // get the 7 strassen sub-matrices
        int [][] P = new int[n][n];
        int [][] Q = new int[n][n];
        int [][] R = new int[n][n];
        int [][] S = new int[n][n];
        int [][] T = new int[n][n];
        int [][] U = new int[n][n];
        int [][] V = new int[n][n];
        
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
        // all the strassen calculations
        P = strassenMult(n, addMatrices(n, a11, a22), addMatrices(n, b11, b22));
        Q = strassenMult(n, addMatrices(n, a21, a22), b11);
        R = strassenMult(n, a11, subtractMatrices(n, b12, b22));
        S = strassenMult(n, a22, subtractMatrices(n, b21, b11));
        T = strassenMult(n, addMatrices(n, a11, a12), b22);
        U = strassenMult(n, subtractMatrices(n, a21, a11), addMatrices(n, b11, b12));
        V = strassenMult(n, subtractMatrices(n, a12, a22), addMatrices(n, b21, b22));
        
        c11 = addMatrices(n, subtractMatrices(n, addMatrices(n, P, S), T), V);
        c12 = addMatrices(n, R, T);
        c21 = addMatrices(n, Q, S);
        c22 = addMatrices(n, subtractMatrices(n, addMatrices(n, P, R), Q), U);
        
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
    
    public static int[][] addMatrices(int N, int[][] A, int[][] B) {
        int[][] C = new int[N][N];  // resulting matrix
        
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                C[i][j] = A[i][j] + B[i][j];    // add matrices
            }
        }
        return C;   // return resulting matrix
    }
    
    public static int[][] subtractMatrices(int N, int[][] A, int [][] B) {
        int [][] C = new int [N][N];    // resulting matrix
        
        for(int i = 0; i < N; i++) {    // subtract matrices
            for(int j = 0; j < N; j++) {
                C[i][j] = A[i][j] - B[i][j];    
            }
        }
        return C;   // return resulting matrix
    }
    
    public static void print(int [][] C) {
        for(int i = 0; i < N; i++) {
            for(int j = 0; j < N; j++) {
                System.out.print(C[i][j] + "\t");
                if(j == N-1)
                    System.out.println();
            }
        }
    }
}
