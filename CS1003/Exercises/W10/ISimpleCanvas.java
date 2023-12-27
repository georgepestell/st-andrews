import java.awt.Color;

public interface ISimpleCanvas {
    public boolean isPointBlack(int x, int y);
    public boolean isPointWhite(int x, int y);
    public Color getPoint(int x, int y);
    public void setPointBlack(int x, int y);
    public void setPointWhite(int x, int y);
    public void setPointColour(int x, int y, Color c);
    public int[] getMouseClick();
}
