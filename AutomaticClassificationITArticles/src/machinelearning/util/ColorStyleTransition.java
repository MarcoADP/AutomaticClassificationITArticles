package machinelearning.util;


import javafx.animation.Transition;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public final class ColorStyleTransition extends Transition {


    private Pane pane;
    private String styleName;
    private Color start;
    private Color end;

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
        pane.setStyle(style);
    }

    private int toRGB(double value) {
        return (int) (255 * value);
    }

    public void setPane(Pane pane) {
        this.pane = pane;
    }

    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    public void setStart(Color start) {
        this.start = start;
    }

    public void setEnd(Color end) {
        this.end = end;
    }

}
