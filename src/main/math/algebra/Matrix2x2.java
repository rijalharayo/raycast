package main.math.algebra;

// Represents a standard 2x2 matrix
public class Matrix2x2 {
     // Internal version of the matrix
     private final double[][] internalMatrix = new double[2][2];

     // Represents the standard identity matrix
     public static final Matrix2x2 IDENTITY = new Matrix2x2(
                                                  1, 0,
                                                  0, 1
                                              );

     // Represents the zero matrix
     public static final Matrix2x2 ZERO = new Matrix2x2(0, 0, 0, 0);
     // Represents a standard anti-clockwise 90deg or π/2 rotation
     public static final Matrix2x2 ROTATE_ANTI_CLOCKWISE_90 = new Matrix2x2(
                                                       0, -1,
                                                       1, 0
                                                  );
     
     // Constructors
     public Matrix2x2(Vector2 column1, Vector2 column2) {
          // 1st column
          this.internalMatrix[0][0] = column1.getX();
          this.internalMatrix[1][0] = column1.getY();
          // 2nd column
          this.internalMatrix[0][1] = column2.getX();
          this.internalMatrix[1][1] = column2.getY();
     }

     public Matrix2x2(float m_00, float m_01, float m_10, float m_11) {
          // 1st column
          this.internalMatrix[0][0] = m_00;
          this.internalMatrix[1][0] = m_10;
          // 2nd column
          this.internalMatrix[0][1] = m_01;
          this.internalMatrix[1][1] = m_11;
     }

     public Matrix2x2(double[][] mArr) {
          for(int i = 0; i < 2; i++) {
               for(int j = 0; j < 2; j++) {
                    this.internalMatrix[i][j] = mArr[i][j];
               }
          }
     }

     // Returns a specific element of the matrix
     public float getElement(int row, int column) {
          if(row > 2 || column > 2 || row <= 0 || column <= 0) {
               throw new IndexOutOfBoundsException("Element at those index doesn't exist");
          }

          return (float) internalMatrix[row - 1][column - 1];
     }

     // Returns a column of the matrix
     public Vector2 getColumn(int columnNum) {
          if(columnNum <= 0 || columnNum > 2) {
               throw new IndexOutOfBoundsException("Invalid column index");
          }

          float c1 = getElement(1, columnNum);
          float c2 = getElement(2, columnNum);

          return new Vector2(c1, c2);
     }

     // Returns a row of the matrix
     public Vector2 getRow(int rowNum) {
          if(rowNum <= 0 || rowNum > 2) {
               throw new IndexOutOfBoundsException("Invalid row index");
          }

          float r1 = getElement(rowNum, 1);
          float r2 = getElement(rowNum, 2);

          return new Vector2(r1, r2);
     }

     // Returns the list of rows
     public Vector2[] getRows() {
          return new Vector2[] { getRow(1), getRow(2) };
     }

     // Returns the list of columns
     public Vector2[] getColumns() {
          return new Vector2[] { getColumn(1), getColumn(2) };
     }

     // Gets the determinant of the matrix
     public float getDeterminant() {
          /* 
               Given a 2x2 matrix A,
               det(A) or |A| = a11 * a22 - a21 * a12
          */

          double c1 = internalMatrix[0][0] * internalMatrix[1][1];
          double c2 = internalMatrix[0][1] * internalMatrix[1][0];

          return (float) (c1 - c2);
     }

     // Returns the transpose of a matrix
     public Matrix2x2 getTranspose() {
          // Sets transposed elements
          float m_00 = getElement(1, 1);
          float m_01 = getElement(2, 1);
          float m_10 = getElement(1, 2);
          float m_11 = getElement(2, 2);

          return new Matrix2x2(m_00, m_01, m_10, m_11);
     }

     // Scales the matrix by a scalar
     public Matrix2x2 multiply(float k) {
          double[][] newArr = new double[2][2];

          for(int i = 0; i < 2; i++) {
               for(int j = 0; j < 2; j++) {
                    newArr[i][j] = internalMatrix[i][j] * k;
               }
          }

          return new Matrix2x2(newArr);
     }

     // Applies the linear transformation to a vector
     public Vector2 transform(Vector2 v) {
          /* 
               Given a 2x2 matrix A and a 2D vector v where:
                    A = [a b]
                        [c d]

                    v = <x, y>

               The transformation/multiplication of v by A is:
                    Av = <ax + by, cx + dy>
               Or,
                    A ⋅ v = [ax + by]
                           [cx + dy]
          */

          float x = 
               getElement(1, 1) * v.getX() +
               getElement(1, 2) * v.getY();

          float y =
               getElement(2, 1) * v.getX() +
               getElement(2, 2) * v.getY();

          return new Vector2(x, y);
     }

     // Multiplies two matrices
     public Matrix2x2 multiply(Matrix2x2 m2) {
          /* 
               Given two 2x2 matrices A and B where:
                    A = [a b]
                        [c d]

                    B = [e f]
                        [g h]

               The composition matrix (A ⋅ B) = [ae + bg     af + bh]
                                               [ce + dg     cf + dh]
          */

          Vector2[] m1Rows = this.getRows();
          Vector2[] m2Columns = m2.getColumns();

          double[][] result = new double[2][2];

          // Loops through rows & columns and multiplies
          for(int row = 0; row < m1Rows.length; row++) {
               for(int col = 0; col < m2Columns.length; col++) {
                    result[row][col] = m1Rows[row].dot(m2Columns[col]);
               }
          }

          return new Matrix2x2(result);
     }

     // Returns the standard rotation matrix of an angle (in radians)
     public static Matrix2x2 getRotationMatrix(float thetaRads) {
          /* 
               The standard 2D rotation matrx is:
                    R(θ) = [cosθ -sinθ]
                           [sinθ  cosθ]
          */

          float m_00 = (float) Math.cos(thetaRads);
          float m_10 = (float) Math.sin(thetaRads);

          float m_01 = (float) -Math.sin(thetaRads);
          float m_11 = (float) Math.cos(thetaRads);

          return new Matrix2x2(m_00, m_01, m_10, m_11);
     }
}