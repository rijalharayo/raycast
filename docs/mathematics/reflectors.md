# Reflector Mathematics

This document explains the mathematical formulation used to calculate the reflection of a light ray from a reflecting surface.

The implementation is based on **vector projection, surface normals, tangents, and vector decomposition**.

---

## 1. Incident Ray

Let the incident ray direction be represented by the vector

$$
I
$$

At the point where the ray intersects the reflecting surface, two perpendicular directions can be obtained:

* \(N\) — surface normal
* \(T\) — surface tangent

Both vectors describe the orientation of the surface.

The normal points perpendicular to the surface, while the tangent lies along the surface.

---

## 2. Decomposing the Incident Vector

The incident vector can be decomposed into two perpendicular components:

$$
I=I_N+I_T
$$

where:

$$
I_N=\operatorname{proj}_N(I)
$$

is the component perpendicular to the surface, and

$$
I_T=\operatorname{proj}_T(I)
$$

is the component parallel to the surface.

The implementation performs these projections directly:

```java id="47291a"
Vector2 incidentOnMirrorNormal =
     incidentVector.projectOnto(mirrorNormal);

Vector2 incidentOnMirrorSurface =
     incidentVector.projectOnto(mirrorSurface);
```

Therefore,

$$
\boxed{
I=
\operatorname{proj}_N(I)+
\operatorname{proj}_T(I)
}
$$

---

## 3. What Happens During Reflection

When a ray reflects from a surface, its tangential component remains unchanged.

Its normal component reverses direction.

Therefore:

$$
I_T\rightarrow I_T
$$

while

$$
I_N\rightarrow-I_N
$$

The reflected vector is consequently

$$
R=I_T-I_N
$$

or

$$
\boxed{
R=I_T+(-I_N)
}
$$

This is the core mathematical idea used by the reflector implementation.

---

## 4. Reflection in the Implementation

The reflected vector is constructed as:

```java id="3h2n5k"
Vector2 reflectionVector =
     incidentOnMirrorSurface.add(
          incidentOnMirrorNormal.multiply(-1f)
     );
```

This directly corresponds to

$$
\boxed{
R=I_T-I_N
}
$$

No special calculation for the mirror's angle is required.

The surface's normal and tangent already contain the required orientation information.

---

## 5. Standard Reflection Formula

The same result can be expressed using the standard vector reflection formula.

For a normalized surface normal \(N\):

$$
\boxed{
R=I-2(I\cdot N)N
}
$$

To see why this is equivalent to the projection method, the normal component of \(I\) is

$$
I_N=(I\cdot N)N
$$

and the tangent component is

$$
I_T=I-I_N
$$

Reflection reverses the normal component:

$$
R=I_T-I_N
$$

Substituting:

$$
R=(I-I_N)-I_N
$$

$$
R=I-2I_N
$$

and therefore:

$$
\boxed{
R=I-2(I\cdot N)N
}
$$

Thus, the projection-based implementation and the standard reflection equation are mathematically equivalent.

---

## 6. Why Use the Projection Method

The implementation explicitly calculates both components:

$$
I_N
$$

and

$$
I_T
$$

This makes the geometry easier to reason about.

Instead of thinking:

> "Calculate some dot product and subtract something."

the calculation becomes:

1. Find the part of the ray going into the surface.
2. Find the part travelling along the surface.
3. Reverse the part going into the surface.
4. Keep the surface-parallel part unchanged.

This is particularly useful for a custom geometry engine because the same decomposition can be reused for other optical calculations.

---

## 7. Surface Orientation

The reflection calculation does not depend on whether the mirror is:

* horizontal
* vertical
* rotated
* curved
* part of an arbitrary polygon

Only the local surface normal and tangent are required.

For a curved surface, the normal can be calculated from the surface's local geometry.

For a flat surface, the normal is constant along the surface.

Therefore, the same reflection calculation can be used for different types of mirrors.

---

## 8. Reflection Angle

The law of reflection states that the angle of incidence equals the angle of reflection:

$$
\boxed{
\theta_i=\theta_r
}
$$

The angles are measured relative to the surface normal.

The vector decomposition naturally produces this behavior because reflection only reverses the normal component.

The tangent component remains unchanged, so the angle between the reflected ray and the normal has the same magnitude as the angle between the incident ray and the normal.

---

## 9. Constructing the Reflected Ray

The calculated reflection vector gives the direction of the outgoing ray.

Let the intersection point be

$$
P
$$

and the reflected direction be

$$
R
$$

The reflected ray can be represented using two points:

$$
P_{\text{tail}}=P+\epsilon R
$$

and

$$
P_{\text{tip}}=P+R
$$

where \(\epsilon\) is a small offset.

The implementation uses:

```java id="g4x9pb"
Vector2 reflectionTip =
     intersectionData.getIntersectionPoint().add(
          reflectionVector
     );

Vector2 reflectionTail =
     intersectionData.getIntersectionPoint().add(
          reflectionVector.multiply(0.01f)
     );
```

The small offset prevents the newly created ray from immediately intersecting the same surface again because of floating-point precision.

---

## 10. Complete Reflection Process

The entire reflection calculation can be summarized as:

$$
I
\rightarrow
(I_N,I_T)
\rightarrow
(-I_N,I_T)
\rightarrow
R
$$

More explicitly:

1. Obtain the incident ray vector.
2. Obtain the surface normal.
3. Obtain the surface tangent.
4. Project the incident vector onto the normal.
5. Project the incident vector onto the tangent.
6. Reverse the normal component.
7. Add the tangent and reversed-normal components.
8. Use the resulting vector as the reflected ray direction.

---

## 11. Mathematical Form

The projection-based formulation is:

$$
\boxed{
I_N=\operatorname{proj}_N(I)
}
$$

$$
\boxed{
I_T=\operatorname{proj}_T(I)
}
$$

Then:

$$
\boxed{
R=I_T-I_N
}
$$

The equivalent standard formulation is:

$$
\boxed{
R=I-2(I\cdot N)N
}
$$

assuming \(N\) is normalized.

---

## 12. Relationship to Refraction

The same vector decomposition is also used by the refractor.

For reflection:

$$
I=I_N+I_T
$$

and:

$$
R=I_T-I_N
$$

For refraction, the incident ray is also decomposed into normal and tangent components, but instead of simply reversing the normal component, Snell's Law determines a new angle.

Thus, the reflector provides the simpler case of the same underlying geometric idea:

$$
\boxed{
\text{Vector}
=
\text{Normal component}
+
\text{Tangent component}
}
$$

This decomposition is useful throughout the optical system.

---

## 13. Summary

Reflection can be understood as a simple modification of the incident vector.

First decompose:

$$
\boxed{
I=I_N+I_T
}
$$

Then reverse only the normal component:

$$
\boxed{
R=I_T-I_N
}
$$

This is equivalent to the standard reflection equation:

$$
\boxed{
R=I-2(I\cdot N)N
}
$$

The implementation uses the projection-based form because it directly represents the geometry of the reflecting surface and integrates naturally with the engine's existing `Vector2`, `Line`, and `SurfaceData` classes.

The result is a reflection system that works with arbitrary surface orientations while keeping the underlying mathematics explicit and reusable.
