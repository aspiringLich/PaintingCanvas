# ToDo

- [X] Cleanup userspace interface
- [x] Event queue so changes like color can be scheduled in constructor
- [x] Alpha support on color tween
- [ ] RotateAround
- [x] consolidate center functions to just (Point center(..) {...})
- [ ] Think of a nice way to allow for more advanced (repeating) animations
- [ ] waitUntilAnimationsDone()
- [X] make animation syntax better
- [X] auto-center when resizing
- [ ] Change App.(width/height) to get(Witdh/Height) funcs
- [ ] Take doubles everyware not floats
- [ ] **Use the @see option in doc comments**
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