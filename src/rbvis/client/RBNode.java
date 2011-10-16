package rbvis.client;

public class RBNode {
    public boolean isBlack = true;
    public RBNode left, right, parent;
    public Object value;

    @Override
    public String toString() {
        String color = isBlack ? "BLACK" : "RED";
        return "<" + color + ">"+value+ left + right + "</"+color+">";
    }
}
