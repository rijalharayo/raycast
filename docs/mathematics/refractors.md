# Refractor Mathematics

This document explains the mathematical formulation used to calculate the path of a light ray when it enters or leaves a refracting medium.

The implementation is based on **vector decomposition, surface normals, Snell's Law, and Total Internal Reflection**.

---

## 1. Incident Ray

Let the normalized incident ray direction be

$$
I
$$

At the point where the ray intersects the refracting surface, the surface provides two perpendicular directions:

* \(N\) — surface normal
* \(T\) — surface tangent

Both are normalized vectors.

Since the normal and tangent are perpendicular, they form a coordinate basis for the surface.

---

## 2. Decomposing the Incident Vector

Any vector can be decomposed into components along the normal and tangent.

Therefore, the incident vector can be written as

$$
I=I_N+I_T
$$

where:

$$
I_N=\operatorname{proj}_N(I)
$$

and

$$
I_T=\operatorname{proj}_T(I)
$$

The implementation performs this directly using vector projections:

```java id="49317a"
Vector2 incidentAlongNormal =
     incidentVector.projectOnto(normal);

Vector2 incidentAlongTangent =
     incidentVector.projectOnto(tangent);
```

Because \(N\) and \(T\) are perpendicular, these two components completely describe the incident ray.

---

## 3. Angle of Incidence

The angle between the incident vector and the surface normal is the angle of incidence:

$$
\theta_i=\angle(I,N)
$$

The sine of this angle is required by Snell's Law:

$$
\sin\theta_i
$$

The implementation obtains it using:

```java id="x3zqv4"
float angleOfIncidence =
     incidentVector.getAngleBetween(normal);

float sinIncident =
     (float) Math.sin(angleOfIncidence);
```

---

## 4. Snell's Law

When light travels from one medium into another, its direction changes according to Snell's Law:

$$
n_1\sin\theta_i=n_2\sin\theta_r
$$

where:

* \(n_1\) is the refractive index of the current medium
* \(n_2\) is the refractive index of the next medium
* \(\theta_i\) is the angle of incidence
* \(\theta_r\) is the angle of refraction

Rearranging for the refracted angle:

$$
\sin\theta_r=
\frac{n_1\sin\theta_i}{n_2}
$$

Therefore,

$$
\boxed{
\theta_r=
\sin^{-1}
\left(
\frac{n_1\sin\theta_i}{n_2}
\right)
}
$$

The implementation calculates this using:

```java id="oy9e4m"
float sinRefracted =
     (n1 * sinIncident) / n2;

sinRefracted =
     Math.clamp(sinRefracted, -1f, 1f);

float angleOfRefraction =
     (float) Math.asin(sinRefracted);
```

---

## 5. Why Total Internal Reflection Happens

Snell's Law requires

$$
\sin\theta_r\leq1
$$

However, when light travels from a medium with a higher refractive index into one with a lower refractive index,

$$
n_1>n_2
$$

there is a point where

$$
\frac{n_1\sin\theta_i}{n_2}>1
$$

At that point, no real refracted angle exists.

This produces **Total Internal Reflection (TIR)**.

The condition is therefore

$$
n_1>n_2
$$

and

$$
\sin\theta_i>\frac{n_2}{n_1}
$$

So the TIR condition is

$$
\boxed{
n_1>n_2
\quad\text{and}\quad
\sin\theta_i>\frac{n_2}{n_1}
}
$$

---

## 6. Critical Angle

The critical angle occurs when the refracted ray travels exactly along the surface.

Therefore,

$$
\theta_r=90^\circ
$$

Since

$$
\sin90^\circ=1
$$

Snell's Law becomes

$$
n_1\sin\theta_c=n_2
$$

Therefore,

$$
\boxed{
\sin\theta_c=\frac{n_2}{n_1}
}
$$

and

$$
\boxed{
\theta_c=
\sin^{-1}
\left(
\frac{n_2}{n_1}
\right)
}
$$

Angles greater than the critical angle result in total internal reflection.

---

## 7. Reflection During TIR

When TIR occurs, the ray is reflected instead of refracted.

The incident vector is already decomposed:

$$
I=I_N+I_T
$$

Reflection preserves the tangential component while reversing the normal component.

Therefore,

$$
R=I_T-I_N
$$

or equivalently,

$$
\boxed{
R=I_T+(-I_N)
}
$$

The implementation uses:

```java id="3w9h1d"
Vector2 reflectionVector =
     incidentAlongTangent.add(
          incidentAlongNormal.multiply(-1f)
     );
```

This is the same vector decomposition used by the mirror implementation.

---

## 8. Constructing the Refracted Vector

When refraction occurs, the refracted vector must have the correct angle relative to the normal.

Let the refracted vector be

$$
R
$$

Since it is normalized,

$$
|R|=1
$$

Its components relative to the normal and tangent are determined by the angle of refraction.

The normal component has magnitude

$$
\cos\theta_r
$$

while the tangent component has magnitude

$$
\sin\theta_r
$$

Therefore,

$$
R_N=\cos\theta_r\,N
$$

and

$$
R_T=\sin\theta_r\,T
$$

Combining the components:

$$
\boxed{
R=
\cos\theta_r\,N+
\sin\theta_r\,T
}
$$

---

## 9. Preserving the Correct Direction

The normalized incident components are used as the basis directions:

```java id="x31k9m"
Vector2 normalizedIncidentAlongNormal =
     incidentAlongNormal.getNormalized();

Vector2 normalizedIncidentAlongTangent =
     incidentAlongTangent.getNormalized();
```

The refracted components are then constructed:

```java id="8yq3sa"
Vector2 refractedAlongNormal =
     normalizedIncidentAlongNormal.multiply(
          (float) Math.cos(angleOfRefraction)
     );

Vector2 refractedAlongTangent =
     normalizedIncidentAlongTangent.multiply(
          (float) Math.sin(angleOfRefraction)
     );
```

Finally:

```java id="y6v1pe"
Vector2 refractedVector =
     refractedAlongNormal.add(
          refractedAlongTangent
     );
```

This gives the required refracted direction while preserving which side of the surface the ray is travelling toward.

---

## 10. Why Vector Decomposition Is Useful

Instead of directly rotating the incident ray by some angle, the implementation works relative to the surface itself.

The surface provides:

$$
N,\quad T
$$

The incident vector provides:

$$
I_N,\quad I_T
$$

Snell's Law determines the new angle:

$$
\theta_r
$$

The new vector is then reconstructed from the same surface basis:

$$
R=
\cos\theta_r\,N+
\sin\theta_r\,T
$$

This makes the calculation independent of the surface's world-space orientation.

The same method therefore works for:

* horizontal surfaces
* vertical surfaces
* curved surfaces
* rotated objects
* arbitrary surface orientations

---

## 11. Complete Refraction Process

The complete calculation can be summarized as:

$$
I
\rightarrow
(I_N,I_T)
\rightarrow
\theta_i
\rightarrow
\theta_r
\rightarrow
(R_N,R_T)
\rightarrow
R
$$

More explicitly:

1. Obtain the incident ray direction.
2. Obtain the surface normal and tangent.
3. Determine the next medium.
4. Obtain \(n_1\) and \(n_2\).
5. Calculate the angle of incidence.
6. Check for total internal reflection.
7. If TIR occurs, reverse the normal component.
8. Otherwise, use Snell's Law to calculate \(\theta_r\).
9. Construct the refracted normal component.
10. Construct the refracted tangent component.
11. Add the two components to obtain the final ray direction.

---

## 12. Vector Form of the Method

The entire refraction calculation can be represented using the surface basis.

Incident vector:

$$
I=I_N+I_T
$$

For TIR:

$$
\boxed{
R=I_T-I_N
}
$$

For refraction:

$$
\boxed{
R=
\cos\theta_r\,N+
\sin\theta_r\,T
}
$$

where

$$
\boxed{
\theta_r=
\sin^{-1}
\left(
\frac{n_1\sin\theta_i}{n_2}
\right)
}
$$

provided that

$$
n_1>n_2
$$

does not produce total internal reflection.

---

## 13. Medium Transition

The refractive indices depend on the medium the ray is currently travelling through and the medium it is entering.

Let

$$
n_1=n_{\text{current}}
$$

and

$$
n_2=n_{\text{next}}
$$

For example, when entering glass from air:

$$
n_1=n_{\text{air}}
$$

$$
n_2=n_{\text{glass}}
$$

When leaving the glass:

$$
n_1=n_{\text{glass}}
$$

$$
n_2=n_{\text{air}}
$$

The direction change therefore depends on the direction of the transition, not simply on the material being intersected.

---

## 14. Numerical Stability

Floating-point calculations can occasionally produce values slightly outside the valid range of the inverse sine function.

For example, a value mathematically equal to \(1\) might become

$$
1.0000001
$$

because of floating-point error.

Since

$$
-1\leq\sin\theta\leq1
$$

the calculated value is clamped before calling `asin()`:

```java id="u8w2qk"
sinRefracted =
     Math.clamp(sinRefracted, -1f, 1f);
```

This prevents numerical errors from producing an invalid result.

---

## 15. Summary

The refractor is based on three main ideas:

### Vector decomposition

$$
\boxed{
I=I_N+I_T
}
$$

The incident ray is separated into components perpendicular and parallel to the surface.

### Snell's Law

$$
\boxed{
n_1\sin\theta_i=n_2\sin\theta_r
}
$$

This determines how much the ray bends.

### Total Internal Reflection

$$
\boxed{
n_1>n_2
\quad\text{and}\quad
\sin\theta_i>\frac{n_2}{n_1}
}
$$

When refraction is impossible, the normal component is reversed:

$$
\boxed{
R=I_T-I_N
}
$$

Otherwise, the refracted vector is reconstructed from the surface basis:

$$
\boxed{
R=
\cos\theta_r\,N+
\sin\theta_r\,T
}
$$

The implementation therefore treats refraction as a combination of **surface-relative vector decomposition and physical optical laws**, rather than as a special-case geometric rotation.
