package machinelearning.util;


import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public final class ColorStyleTransition extends Transition {


    private Node node;
    private String styleName;
    private Color start;
    private Color end;

    private Color s;
    private Color e;

    public ColorStyleTransition(double seconds) {
        setCycleDuration(Duration.seconds(seconds));
    }

    @Override
    protected void interpolate(double frac) {
        final Color newColor = start.interpolate(end, frac);
        updateColor(newColor);
    }

    private void updateColor(Color newColor) {
        int red = toRGB(newColor.getRed());
        int green = toRGB(newColor.getGreen());
        int blue = toRGB(newColor.getBlue());

        String styleColor = String.format("rgb(%d, %d, %d);", red, green, blue);
        String style = styleName + ": " + styleColor;
        node.setStyle(style);
    }

    @Override
    public void play() {
        start = s;
        end = e;
        super.play();
    }

    public void playInverse() {
        start = e;
        end = s;
        super.play();
    }

    private int toRGB(double value) {
        return (int) (255 * value);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setStart(Color start) {
        this.start = start;
        s = start;
    }

    public void setEnd(Color end) {
        this.end = end;
        e = end;
    }

}
