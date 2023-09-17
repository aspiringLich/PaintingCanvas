package paintingcanvas.animation;

/**
 * Special easing animations for animations.
 * <p>
 * Predefined easings copied from <a href="https://easings.net/">https://easings.net</a>
 * <pre>{@code
 * Circle circle = new Circle(100, 100, 50);
 * circle.animate().add(Animation.moveTo(300, 300, 1000)
 *                 .ease(Easing.easeInBack()));
 * }</pre>
 */
public interface Easing {
    /**
     * Linear easing, aka no easing.
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing linear() {
        return t -> t;
    }

    /**
     * Ease in
     *
     * @param n the degree
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeIn(double n) {
        return t -> Math.pow(t, n);
    }

    /**
     * Ease out
     *
     * @param n the degree
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeOut(double n) {
        return t -> Math.pow(t, n);
    }

    /**
     * Exponentially ease in and out
     *
     * @param n the degree
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInOut(double n) {
        return t -> {
            if (t < 0.5) return Math.pow(2 * t, n) / 2;
            else return 1 - Math.pow(2 * (1 - t), n) / 2;
        };
    }

    /**
     * Ease in sinusoidally
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInSine() {
        return t -> 1 - Math.cos((t * Math.PI) / 2);
    }

    /**
     * Ease out sinusoidally
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeOutSine() {
        return t -> Math.sin((t * Math.PI) / 2);
    }

    /**
     * Ease in and out sinusoidally
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInOutSine() {
        return t -> -(Math.cos(Math.PI * t) - 1) / 2;
    }

    /**
     * Ease in quadratically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInQuad() {
        return t -> t * t;
    }

    /**
     * Ease out quadratically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeOutQuad() {
        return t -> 1 - (1 - t) * (1 - t);
    }

    /**
     * Ease in and out quadratically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInOutQuad() {
        return t -> {
            if (t < 0.5) return 2 * t * t;
            else return 1 - Math.pow(-2 * t + 2, 2) / 2;
        };
    }

    /**
     * Ease in cubically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInCubic() {
        return t -> t * t * t;
    }

    /**
     * Ease out cubically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeOutCubic() {
        return t -> 1 - Math.pow(1 - t, 3);
    }

    /**
     * Ease in and out cubically
     *
     * @return an {@code Easing} object representing this easing.
     */
    static Easing easeInOutCubic() {
        return t -> {
            if (t < 0.5) return 4 * t * t * t;
            else return 1 - Math.pow(-2 * t + 2, 3) / 2;
        };
    }

    static Easing easeInQuart() {
        return t -> t * t * t * t;
    }

    static Easing easeOutQuart() {
        return t -> 1 - Math.pow(1 - t, 4);
    }

    static Easing easeInOutQuart() {
        return t -> {
            if (t < 0.5) return 8 * t * t * t * t;
            else return 1 - Math.pow(-2 * t + 2, 4) / 2;
        };
    }

    static Easing easeInQuint() {
        return t -> t * t * t * t * t;
    }

    static Easing easeOutQuint() {
        return t -> 1 - Math.pow(1 - t, 5);
    }

    static Easing easeInOutQuint() {
        return t -> {
            if (t < 0.5) return 16 * t * t * t * t * t;
            else return 1 - Math.pow(-2 * t + 2, 5) / 2;
        };
    }

    static Easing easeInExpo() {
        return t -> Math.pow(2, 10 * t - 10);
    }

    static Easing easeOutExpo() {
        return t -> 1 - Math.pow(2, -10 * t);
    }

    static Easing easeInOutExpo() {
        return t -> {
            if (t < 0.5) return Math.pow(2, 20 * t - 10) / 2;
            else return (2 - Math.pow(2, -20 * t + 10)) / 2;
        };
    }

    static Easing easeInCirc() {
        return t -> 1 - Math.sqrt(1 - Math.pow(t, 2));
    }

    static Easing easeOutCirc() {
        return t -> Math.sqrt(1 - Math.pow(t - 1, 2));
    }

    static Easing easeInOutCirc() {
        return t -> {
            if (t < 0.5) return (1 - Math.sqrt(1 - Math.pow(2 * t, 2))) / 2;
            else return (Math.sqrt(1 - Math.pow(-2 * t + 2, 2)) + 1) / 2;
        };
    }

    static Easing easeInBack() {
        return t -> t * t * t - t * Math.sin(t * Math.PI);
    }

    static Easing easeOutBack() {
        return t -> 1 - easeInBack().ease(1 - t);
    }

    static Easing easeInOutBack() {
        return t -> {
            if (t < 0.5) return easeInBack().ease(2 * t) / 2;
            else return (2 - easeInBack().ease(2 - 2 * t)) / 2;
        };
    }

    static Easing easeInElastic() {
        return t -> {
            var c4 = (2 * Math.PI) / 3;
            return t == 0 || t == 1 ? t :
                    -Math.pow(2, 10 * t - 10) * Math.sin((t * 10 - 10.75) * c4);
        };
    }

    static Easing easeOutElastic() {
        return t -> 1 - easeInElastic().ease(1 - t);
    }

    static Easing easeInOutElastic() {
        return t -> {
            if (t < 0.5) return easeInElastic().ease(2 * t) / 2;
            else return (2 - easeInElastic().ease(2 - 2 * t)) / 2;
        };
    }

    static Easing easeInBounce() {
        return t -> 1 - easeOutBounce().ease(1 - t);
    }

    static Easing easeOutBounce() {
        return t -> {
            if (t < 4 / 11.0) return (121 * t * t) / 16.0;
            else if (t < 8 / 11.0) return (363 / 40.0 * t * t) - (99 / 10.0 * t) + 17 / 5.0;
            else if (t < 9 / 10.0) return (4356 / 361.0 * t * t) - (35442 / 1805.0 * t) + 16061 / 1805.0;
            else return (54 / 5.0 * t * t) - (513 / 25.0 * t) + 268 / 25.0;
        };
    }

    static Easing easeInOutBounce() {
        return t -> {
            if (t < 0.5) return easeInBounce().ease(2 * t) / 2;
            else return (2 - easeInBounce().ease(2 - 2 * t)) / 2;
        };
    }

    double ease(double t);
}
