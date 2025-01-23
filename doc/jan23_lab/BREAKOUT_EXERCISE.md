# Breakout Abstractions Lab Discussion
#### Kyaira Boughton, Jessica Chen, Calvin Chen


### Block

This superclass's purpose as an abstraction:
```java
 public class Brick {
     public abstract boolean checkBallCollision(Ball ball)
     // checks if ball collides, but each subclass does different stuff based on if ball collided
     // lucky block triggers powerup, sturdy block decrements life until last life, sticky block removes ball that collided with it

     public Type getSomething()
     // everything else was a get method of the Brick
 }
```

```java
 public class Paddle {
     public void moveLeft()
     public void moveRight()
     // all Paddles have the same movement

     public abstract boolean checkBallCollision(Ball ball)
     // on normal or laser mode the ball bounces just like reflection, in catch and release the ball should not move and stay in the center of the paddle, in random the ball should be set to random movement

     public abstract void onMousePress()
     // normal and random mode do nothing, laser mode spawns balls that shoot straight up, catch and release launches ball in dragged direction

     public Type getSomething()
     public void setSomething(Type something)
     // everything else was a get or set method

 }
```

#### Subclasses

Each subclass's high-level behavorial differences from the superclass
- these were described in the comments of the superclass


#### Affect on Game/Level class

Which methods are simplified by using this abstraction and why
- a lot of the GameScene used to have a really long call based what the ball collided with, now the Brick and Paddle class handle these differences.

