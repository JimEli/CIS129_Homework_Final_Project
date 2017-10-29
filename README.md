# Pima CC CIS129 Homework Assignment Final Project

## Magic Triangle Puzzle Game 

This program plays the "*magic triangle*" puzzle game. It includes a nested MagicTriangle class which constructs, displays and solves the "magic triangle" puzzle. The magic triangle is a diagrammatic representation of a math problem involving a system of 3 linear equations. 

## Magic Triangle Puzzle Description

The triangle has 3 numbers in squares, and three unknown numbers in circles. The 3 numbers in the squares (left-side, right-side, and bottom), mid span between the triangle vertices are the sums of the numbers in the circles (top, left-bottom, and right-bottom) to which they are connected. The user enters the numbers for the squares and the program attempts to calculate the numbers in the circles. 
 
Here is an example of an unsolved magic triangle, and its solution:
```text 
           (  )                        ( 6)
          /    \                      /    \
        [14]  [20]                  [14]  [20]
        /        \                  /        \
     (  )--[22]--(  )            ( 8)--[22]--(14)
``` 

## Solving the Magic Triangle Puzzle

To determine the numbers in the circles, the program must solve the following system of 3 linear equations. Using Cramer's Rule and optimizing for the constraints of the problem, yields the following 3 equations:
```C
TopCircle = (BottomSquare - RightSquare - LeftSquare)/-2 
LeftBottomCircle = (RigthSquare - LeftSquare - BottomSquare)/-2
RightBottomCircle = (LeftSquare - RightSquare - BottomSquare)/-2
```
It is important to note, there may be 0 or more solutions for the combination of numbers selected for the squares. If solvable, the program only determines one solution. The input for the squares is limited between -40 to 40, inclusively.

* Uses Scanner class, so is not safe for multi-threaded use.
 
*Submitted in partial fulfillment of the requirements of PCC CIS-129.*
