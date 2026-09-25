# Line Mathematics

This document explains the mathematical formulations used by the `Line` class.

The class provides the fundamental geometric operations used throughout the engine, including **direction vectors, surface normals, parallel detection, infinite-line intersection, and finite line-segment intersection**.

---

## 1. Representing a Line

A line segment is represented by two points:

$$
A=(x_1,y_1)
$$

and

$$
B=(x_2,y_2)
$$

The segment extends from \(A\) to \(B\).

In the implementation, these are stored as:

```java
protected Vector2 start;
protected Vector2 end;
```

The line can therefore be represented mathematically by its two endpoints.

---

## 2. Direction Vector

The direction of a line is obtained by subtracting its starting point from its ending point:

$$
\boxed{
\vec{AB}=B-A
}
$$

Therefore:

$$
\vec{AB}=
\begin{bmatrix}
x_2-x_1\\
y_2-y_1
\end{bmatrix}
$$

The implementation uses:

```java
public Vector2 getLineVector() {
     return end.subtract(start);
}
```

This vector contains both the direction and the length of the segment.

---

## 3. Length of a Line

The magnitude of the direction vector gives the length of the line segment.

For

$$
\vec{AB}=
\begin{bmatrix}
x_2-x_1\\
y_2-y_1
\end{bmatrix}
$$

the length is

$$
\boxed{
|\vec{AB}|=
\sqrt{(x_2-x_1)^2+(y_2-y_1)^2}
}
$$

This is the standard Euclidean distance formula.

The implementation delegates this calculation to the vector's magnitude:

```java
public float getLength() {
     return getLineVector().getMagnitude();
}
```

---

## 4. Normalized Direction

A normalized direction vector has a magnitude of \(1\).

For a direction vector \(v\):

$$
\boxed{
\hat v=\frac{v}{|v|}
}
$$

The implementation obtains this using:

```java
public Vector2 getNormalizedDirection() {
     return getLineVector().getNormalized();
}
```

Normalization is important when comparing directions because it removes the effect of different line lengths.

---

## 5. Slope

For two points

$$
A=(x_1,y_1)
$$

and

$$
B=(x_2,y_2)
$$

the slope is

$$
\boxed{
m=\frac{y_2-y_1}{x_2-x_1}
}
$$

The implementation calculates this from the line vector:

```java
public float getSlope() {
     float m =
          (float) getLineVector().getY()
          / getLineVector().getX();

     return m;
}
```

A vertical line has an undefined slope because its \(x\)-difference is zero.

---

## 6. Surface Normal

A normal vector is perpendicular to the line.

If the normalized direction of the line is

$$
\hat v=
\begin{bmatrix}
x\\
y
\end{bmatrix}
$$

then rotating it \(90^\circ\) counter-clockwise gives

$$
\boxed{
N=
\begin{bmatrix}
-y\\
x
\end{bmatrix}
}
$$

This is because the \(90^\circ\) counter-clockwise rotation matrix is

$$
R_{90}=
\begin{bmatrix}
0&-1\\
1&0
\end{bmatrix}
$$

and

$$
R_{90}
\begin{bmatrix}
x\\
y
\end{bmatrix}
=
\begin{bmatrix}
-y\\
x
\end{bmatrix}
$$

The implementation performs exactly this transformation:

```java
public Vector2 getNormal() {
     return Matrix2x2.ROTATE_ANTI_CLOCKWISE_90
          .transform(getNormalizedDirection());
}
```

Because the direction vector is normalized first, the resulting normal is also a unit vector.

---

## 7. Parallel and Collinear Lines

Two normalized direction vectors are parallel when they point in either the same or opposite directions.

Let the normalized directions be

$$
\hat v_1
$$

and

$$
\hat v_2
$$

Their dot product is

$$
\hat v_1\cdot\hat v_2
=
|\hat v_1||\hat v_2|\cos\theta
$$

Since both vectors have magnitude \(1\):

$$
\hat v_1\cdot\hat v_2=\cos\theta
$$

For parallel lines:

$$
\theta=0^\circ
$$

or

$$
\theta=180^\circ
$$

Therefore:

$$
\boxed{
\hat v_1\cdot\hat v_2=\pm1
}
$$

The implementation checks whether the dot product is sufficiently close to \(+1\) or \(-1\):

```java
float dotProduct =
     l1.getNormalizedDirection()
       .dot(l2.getNormalizedDirection());

float d1 = Math.abs(dotProduct - 1.0f);
float d2 = Math.abs(dotProduct + 1.0f);

float epsilon = 0.0001f;

return d1 <= epsilon || d2 <= epsilon;
```

The epsilon accounts for floating-point precision errors.

---

# 8. Standard Form of a Line

For two points

$$
A=(x_1,y_1)
$$

and

$$
B=(x_2,y_2)
$$

the line can be written in standard form:

$$
\boxed{
Ax+By=C
}
$$

The coefficients can be obtained as:

$$
A=y_1-y_2
$$

$$
B=x_2-x_1
$$

and

$$
C=Ax_1+By_1
$$

Therefore:

$$
\boxed{
A=(y_1-y_2)
}
$$

$$
\boxed{
B=(x_2-x_1)
}
$$

$$
\boxed{
C=(y_1-y_2)x_1+(x_2-x_1)y_1
}
$$

These are the values calculated by `findIntersection()`.

---

# 9. Intersection of Two Infinite Lines

Suppose two lines are represented by:

$$
A_1x+B_1y=C_1
$$

and

$$
A_2x+B_2y=C_2
$$

These equations can be represented as a matrix equation:

$$
\begin{bmatrix}
A_1&B_1\\
A_2&B_2
\end{bmatrix}
\begin{bmatrix}
x\\
y
\end{bmatrix}
=
\begin{bmatrix}
C_1\\
C_2
\end{bmatrix}
$$

The coefficient matrix is:

$$
M=
\begin{bmatrix}
A_1&B_1\\
A_2&B_2
\end{bmatrix}
$$

Its determinant is:

$$
\boxed{
\det(M)=A_1B_2-A_2B_1
}
$$

If

$$
\det(M)=0
$$

then the lines are parallel or coincident and there is no unique intersection.

---

# 10. Cramer's Rule

To solve the system, Cramer's Rule replaces one column of the coefficient matrix with the constants.

For \(x\):

$$
M_x=
\begin{bmatrix}
C_1&B_1\\
C_2&B_2
\end{bmatrix}
$$

Therefore:

$$
\boxed{
x=\frac{\det(M_x)}{\det(M)}
}
$$

For \(y\):

$$
M_y=
\begin{bmatrix}
A_1&C_1\\
A_2&C_2
\end{bmatrix}
$$

Therefore:

$$
\boxed{
y=\frac{\det(M_y)}{\det(M)}
}
$$

The determinant of a \(2\times2\) matrix is:

$$
\boxed{
\det
\begin{bmatrix}
a&b\\
c&d
\end{bmatrix}
=ad-bc
}
$$

The implementation constructs these matrices and obtains their determinants:

```java
Matrix2x2 Mx =
     new Matrix2x2(
          constantVector,
          coefficientMatrix.getColumn(2)
     );

Matrix2x2 My =
     new Matrix2x2(
          coefficientMatrix.getColumn(1),
          constantVector
     );

float x = Mx.getDeterminant() / determinant;
float y = My.getDeterminant() / determinant;
```

Therefore, `findIntersection()` calculates the intersection of the **infinite lines** containing the two segments.

---

# 11. Determinant and Parallel Lines

The determinant also provides a mathematical test for parallelism.

For:

$$
M=
\begin{bmatrix}
A_1&B_1\\
A_2&B_2
\end{bmatrix}
$$

if

$$
\det(M)=0
$$

then:

$$
A_1B_2-A_2B_1=0
$$

This means the two equations do not have a unique solution.

In practice, floating-point calculations rarely produce an exact zero, so the implementation uses:

```java
if (Math.abs(determinant) < 0.0001f) {
     return null;
}
```

The determinant is therefore considered zero when it falls within the chosen numerical tolerance.

---

# 12. Infinite Lines vs Line Segments

Finding the intersection of two infinite lines is not enough for the engine.

For example, two finite segments may lie on intersecting infinite lines but be positioned far apart:

```text
A---------B


                  C---------D
```

Their infinite lines intersect, but the actual segments do not.

Therefore, the engine performs an additional check to determine whether the intersection lies within both segments.

---

# 13. Determining Whether a Point Lies Between Two Points

Suppose:

$$
A
$$

and

$$
B
$$

are the endpoints of a segment and \(P\) is a candidate point.

Define:

$$
\vec{AB}=B-A
$$

$$
\vec{AP}=P-A
$$

$$
\vec{BP}=P-B
$$

If \(P\) lies between \(A\) and \(B\), then:

$$
\vec{AB}\cdot\vec{AP}\geq0
$$

because \(P\) lies in the same direction from \(A\) as \(B\).

At the same time:

$$
\vec{AB}\cdot\vec{BP}\leq0
$$

because \(P\) lies in the opposite direction from \(B\).

Therefore:

$$
\boxed{
(\vec{AB}\cdot\vec{AP})
(\vec{AB}\cdot\vec{BP})\leq0
}
$$

This is the test used by `liesBetween()`.

```java
Vector2 AB = pB.subtract(pA);
Vector2 AP = position.subtract(pA);
Vector2 BP = position.subtract(pB);

float d1 = AB.dot(AP);
float d2 = AB.dot(BP);

return d1 * d2 <= 0;
```

This works because the two dot products have opposite signs when the point lies between the endpoints.

---

# 14. Segment Intersection

Two non-parallel line segments intersect if their infinite lines intersect at a point that lies within both segments.

Let the segments be:

$$
AB
$$

and

$$
CD
$$

First, the intersection of their infinite lines is calculated:

$$
P=L_{AB}\cap L_{CD}
$$

Then two conditions must hold:

$$
\boxed{
P\in AB
}
$$

and

$$
\boxed{
P\in CD
}
$$

The implementation performs:

```java
boolean onThisSegment =
     liesBetween(this.start, this.end, intersection);

boolean onLine2Segment =
     liesBetween(
          line2.getStart(),
          line2.getEnd(),
          intersection
     );
```

Only when both are true is the intersection considered a valid segment intersection.

---

# 15. Early Intersection Test

Before calculating the actual intersection, the implementation checks whether the endpoints of either segment lie within the other segment's projection.

For segments \(AB\) and \(CD\), it checks:

$$
C\in AB
$$

$$
D\in AB
$$

$$
A\in CD
$$

$$
B\in CD
$$

The implementation calculates:

```java
boolean C_on_AB =
     liesBetween(start, end, line2.getStart());

boolean D_on_AB =
     liesBetween(start, end, line2.getEnd());

boolean A_on_CD =
     liesBetween(line2.getStart(), line2.getEnd(), start);

boolean B_on_CD =
     liesBetween(line2.getStart(), line2.getEnd(), end);
```

If none of these conditions are satisfied, the method returns early:

```java
boolean possibleIntersection =
     C_on_AB ||
     D_on_AB ||
     A_on_CD ||
     B_on_CD;

if (!possibleIntersection) {
     return null;
}
```

This avoids performing the more expensive intersection calculation when the segments cannot intersect according to this preliminary test.

---

# 16. Identical Segments

Two line segments are geometrically identical when they have the same endpoints, regardless of endpoint ordering.

For example:

$$
A\rightarrow B
$$

and

$$
B\rightarrow A
$$

represent the same geometric segment.

Therefore, two cases are checked.

### Same direction

$$
A=A'
$$

and

$$
B=B'
$$

### Opposite direction

$$
A=B'
$$

and

$$
B=A'
$$

The implementation checks both:

```java
boolean sameDirection =
     start.isGeometricallyEqual(other.start, epsilon) &&
     end.isGeometricallyEqual(other.end, epsilon);

boolean oppositeDirection =
     start.isGeometricallyEqual(other.end, epsilon) &&
     end.isGeometricallyEqual(other.start, epsilon);
```

Therefore:

$$
\boxed{
\text{same segment}
=
\text{same direction}
\lor
\text{opposite direction}
}
$$

---

# 17. Translation

A line can be translated by adding the same displacement vector to both endpoints.

For an offset

$$
T=
\begin{bmatrix}
t_x\\
t_y
\end{bmatrix}
$$

the translated endpoints become:

$$
A'=A+T
$$

$$
B'=B+T
$$

Therefore:

$$
\boxed{
L'=L+T
}
$$

The implementation applies the translation to both endpoints:

```java
return new Line(
     getStart().add(offset),
     getEnd().add(offset)
);
```

Translation changes the position of the line but does not change its direction or length.

---

# 18. Rotation

A line can be rotated by rotating both of its endpoints.

For a point \(P\), rotation around the origin is:

$$
P'=R(\theta)P
$$

where:

$$
R(\theta)=
\begin{bmatrix}
\cos\theta&-\sin\theta\\
\sin\theta&\cos\theta
\end{bmatrix}
$$

Therefore, for a line:

$$
A'=R(\theta)A
$$

$$
B'=R(\theta)B
$$

The resulting line is:

$$
\boxed{
L'=
\overline{R(\theta)A\,R(\theta)B}
}
$$

The `rotateAround()` operation first treats the selected point as the rotation center and rotates both endpoints around it.

---

# 19. Mathematical Role of the Line Class

The `Line` class acts as a basic geometric primitive for the rest of the engine.

Its mathematical operations support:

* ray intersections
* polygon edges
* mirror surfaces
* lens surfaces
* portal surfaces
* surface normals
* collision detection
* raycasting
* optical calculations

For example, a reflector can obtain a surface normal from a line:

$$
\boxed{
N=R_{90}(\hat v)
}
$$

A ray can then be reflected using that normal.

Similarly, refractors can use the line's tangent and normal as the basis for their refraction calculations.

---

# 20. Summary

The main mathematical operations of the `Line` class are:

### Direction

$$
\boxed{
\vec{AB}=B-A
}
$$

### Length

$$
\boxed{
|\vec{AB}|=
\sqrt{(x_2-x_1)^2+(y_2-y_1)^2}
}
$$

### Normal

$$
\boxed{
N=
\begin{bmatrix}
-y\\
x
\end{bmatrix}
}
$$

for a normalized direction \((x,y)\).

### Parallel detection

$$
\boxed{
\hat v_1\cdot\hat v_2\approx\pm1
}
$$

### Infinite-line intersection

$$
\boxed{
x=\frac{\det(M_x)}{\det(M)}
}
$$

$$
\boxed{
y=\frac{\det(M_y)}{\det(M)}
}
$$

using Cramer's Rule.

### Point-between-segment test

$$
\boxed{
(\vec{AB}\cdot\vec{AP})
(\vec{AB}\cdot\vec{BP})\leq0
}
$$

### Segment intersection

$$
\boxed{
P\in AB
\quad\text{and}\quad
P\in CD
}
$$

### Identical segments

$$
\boxed{
(A=A'\land B=B')
\lor
(A=B'\land B=A')
}
$$

The `Line` class therefore provides much of the geometric foundation on which the engine's **raycasting, collision, reflection, refraction, lens, and portal systems** are built.
