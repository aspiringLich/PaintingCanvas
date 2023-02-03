# ToDo

 - Clean up imports
 - Check constructors for Color thingy
 - Change the color to to _not_ use bytes please
 - 

## Animation System Syntax

```
// animate method

// option 1:
obj.animate(..)
    .then()
    .animate(..)
    .then()
    .animate(..);

// option 2:
obj.animateParallel(
  colorTo(..),
  moveTo(..),
  100
);

// builder:
obj.animate()
   .add(colorTo(..), 100)
   .wait(100)
   .add(moveTo(..), 100)

obj.animate()
   .add(colorTo(..), 100)
   .with(moveTo(..), 100)
   .schedule(0, colorTo(..), 100)
```