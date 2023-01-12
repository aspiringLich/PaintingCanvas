# ToDo

- [X] Cleanup userspace interface
- [ ] Event queue so changes like color can be scheduled in constructor
- [ ] Alpha ()
- [ ] RotateAround
- [ ] consolidate center functions
- [ ] Think of a nice way to allow for more advanced (repeating) animations
- [ ] waitUntilAnimationsDone()
- [X] make animation syntax better
- [X] auto-center when resizing
- [ ] Use scheduler for all element sets (some function that takes a lambda for specific code)
    - Just use the scheduler for everything
    - also maybe make a generic scheduler / ticker
- [ ] Change App.(width/height) to get(Witdh/Height) funcs
- [ ] Take doubles everyware not floats
- [X] More shapes
    - [x] Text
    - [x] Circle
    - [x] Ellipse
    - [x] Square
    - [x] Rect
    - [X] Polygon
    - [X] Triangle
    - [ ] Line
    - [ ] Shape (defined with lines)

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