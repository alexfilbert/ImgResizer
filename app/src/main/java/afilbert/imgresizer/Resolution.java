package afilbert.imgresizer;

public class Resolution {
    private int x;
    private int y;
    private String dispRes;

    public Resolution(int x, int y){
        x = x;
        y = y;
        dispRes = String.valueOf(x) + " x " + String.valueOf(y);
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY() {
        return y;
    }

    public String getResString(){
        dispRes = String.valueOf(x) + " x " + String.valueOf(y);
        return dispRes;
    }


}
