# Portal Mathematics

This document explains the mathematical formulation used to transform points and directions from one portal to its linked portal.

The portal transformation is based on **local coordinate systems, matrix transformations, rotation, scaling, and reflection**.

---

## 1. Portal Coordinate System

Each portal has its own local coordinate system.

For a portal with width \(w\) and height \(h\), the initial transformation from portal-local coordinates to world coordinates can be represented by the matrix

$$
M =
\begin{bmatrix}
\frac{w}{2} & 0\\
0 & \frac{h}{2}
\end{bmatrix}
$$

This matrix scales a normalized local coordinate according to the actual dimensions of the portal.

For example, a normalized point

$$
v =
\begin{bmatrix}
x\\
y
\end{bmatrix}
$$

is transformed into the portal's local dimensions by

$$
Mv =
\begin{bmatrix}
\frac{w}{2}x\\
\frac{h}{2}y
\end{bmatrix}
$$

---

## 2. Portal Rotation

A portal can be rotated in the world.

The standard 2D rotation matrix is

$$
R(\theta)=
\begin{bmatrix}
\cos\theta & -\sin\theta\\
\sin\theta & \cos\theta
\end{bmatrix}
$$

where \(\theta\) is the portal's rotation angle.

Applying the rotation to the portal matrix gives

$$
M' = R(\theta)M
$$

The resulting matrix represents both the portal's dimensions and its orientation.

Therefore, the matrix stored for a portal represents its local coordinate system in world space.

---

## 3. Converting World Coordinates to Portal Coordinates

Suppose a point has world-space vector

$$
v
$$

and belongs to portal \(1\).

Its coordinates relative to portal \(1\) are obtained by applying the inverse of the portal matrix:

$$
v_1=M_1^{-1}v
$$

This effectively reverses the transformation performed by portal \(1\).

---

## 4. Transforming Between Two Portals

After converting the point into the coordinate system of the first portal, it can be transformed into the coordinate system of the second portal.

Let

$$
v_1=M_1^{-1}v
$$

and

$$
v_2=M_2v_1
$$

Substituting the first equation into the second gives

$$
v_2=M_2M_1^{-1}v
$$

Therefore, the complete transformation between two portals is

$$
\boxed{T=M_2M_1^{-1}}
$$

where:

* \(M_1\) is the source portal matrix
* \(M_2\) is the destination portal matrix
* \(T\) transforms vectors from the source portal's coordinate system to the destination portal's coordinate system

This matrix is used for transforming ray directions through portals.

---

## 5. Why Points Need Reflection

A point passing through a portal must appear on the corresponding side of the linked portal.

Simply applying

$$
M_2M_1^{-1}
$$

does not account for the fact that the point enters one portal from one side and emerges from the opposite side of the destination portal.

Therefore, a reflection transformation is applied to the point before the portal transformation.

Let \(F_r\) represent the reflection matrix.

The point transformation becomes

$$
v_2=M_2M_1^{-1}F_rv
$$

Therefore,

$$
\boxed{P=M_2M_1^{-1}F_r}
$$

where \(P\) is the complete point transformation matrix.

---

## 6. Reflection Matrix

The reflection matrix is constructed from the portal surface that the ray intersects.

If the portal surface has a tangent direction and a normal direction, the reflection changes the component perpendicular to the surface while preserving the component along the surface.

For a normalized normal vector

$$
n=
\begin{bmatrix}
n_x\\
n_y
\end{bmatrix}
$$

the reflection matrix can be expressed as

$$
F_r=I-2nn^T
$$

where \(I\) is the \(2\times2\) identity matrix.

Expanding this gives

$$
F_r=
\begin{bmatrix}
1-2n_x^2 & -2n_xn_y\\
-2n_xn_y & 1-2n_y^2
\end{bmatrix}
$$

This reflection is applied to the position of the ray relative to the portal.

---

## 7. Transforming the Ray Direction

The ray's direction is different from its position.

A direction represents an orientation rather than a point in space, so it does not need the positional reflection described above.

The direction transformation is therefore simply

$$
d_2=M_2M_1^{-1}d_1
$$

or

$$
\boxed{D=M_2M_1^{-1}}
$$

After applying the transformation, the resulting direction is normalized.

This produces the direction that the ray should travel after emerging from the linked portal.

---

## 8. Complete Point Transformation

Combining the transformations gives

$$
P=M_2M_1^{-1}F_r
$$

The transformation occurs in this order:

$$
v
\rightarrow
F_rv
\rightarrow
M_1^{-1}F_rv
\rightarrow
M_2M_1^{-1}F_rv
$$

So the process is:

1. Reflect the point relative to the portal surface.
2. Convert it from world coordinates into the source portal's coordinate system.
3. Convert that local coordinate into the destination portal's coordinate system.

---

## 9. Complete Direction Transformation

For a ray direction:

$$
D=M_2M_1^{-1}
$$

The process is:

1. Convert the direction into the source portal's coordinate system.
2. Convert that direction into the destination portal's coordinate system.
3. Normalize the resulting vector.

Unlike a point, the direction does not require the positional reflection matrix.

---

## 10. Position Relative to the Portal

The intersection point must first be expressed relative to the portal's position.

If

$$
p
$$

is the world-space intersection point and

$$
c
$$

is the portal's world-space position, then

$$
p_{\text{relative}}=p-c
$$

The transformation is then applied to this relative vector.

After transforming it through the linked portal, the destination portal's position is added back:

$$
p_{\text{world}}=
c_2+Pp_{\text{relative}}
$$

where \(c_2\) is the position of the linked portal.

---

## 11. Preventing Immediate Re-Collision

After calculating the emergence point, the ray is moved slightly forward along its new direction.

If the transformed point is

$$
p
$$

and the normalized outgoing direction is

$$
d
$$

then the final ray origin is

$$
p'=p+\epsilon d
$$

where \(\epsilon\) is a very small value.

In the implementation, this prevents the newly created ray from immediately intersecting the same portal again due to floating-point precision.

---

## 12. Implementation

The mathematical transformation is implemented using the portal matrices:

```java
Matrix2x2 reflectionMatrix = cachedReflectionMatrix;
Matrix2x2 portalInverse = this.portalMatrix.getInverse();

Matrix2x2 portalTransformMatrix =
     linkedPortal.portalMatrix.multiply(portalInverse);

Matrix2x2 pointTransformMatrix =
     portalTransformMatrix.multiply(reflectionMatrix);
```

The point uses

```java
Matrix2x2 pointTransformMatrix =
     portalTransformMatrix.multiply(reflectionMatrix);
```

which corresponds to

$$
P=M_2M_1^{-1}F_r
$$

The ray direction uses

```java
Vector2 newRayDirection =
     portalTransformMatrix.transform(incidentVector);
```

which corresponds to

$$
D=M_2M_1^{-1}
$$

The transformed point is then converted back to world coordinates by adding the linked portal's position.

---

## 13. Summary

The portal system can therefore be reduced to two important transformations.

### Point

$$
\boxed{P=M_2M_1^{-1}F_r}
$$

The point is reflected and then transformed from the source portal coordinate system into the destination portal coordinate system.

### Direction

$$
\boxed{D=M_2M_1^{-1}}
$$

The direction is transformed between the two portal coordinate systems without applying the positional reflection.

This separation is important because a **point describes where the ray is**, while a **direction describes how the ray is oriented**.

The portal implementation is therefore fundamentally a coordinate-system transformation rather than a special-case teleportation calculation.
