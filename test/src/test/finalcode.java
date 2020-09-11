package test;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class finalcode extends JPanel {
  private int SIZE = 8;
  //Below are 3 points, points[0] and [1] and top-left and bottom-right of the shape.
  // points[2] is the center of the shape
  private Rectangle2D[] points = { new Rectangle2D.Double(50, 50,SIZE, SIZE), 
                                   new Rectangle2D.Double(150, 150,SIZE, SIZE),
                                   new Rectangle2D.Double(100, 100,SIZE, SIZE),
                                   new Rectangle2D.Double(50, 150,SIZE, SIZE),
                                   new Rectangle2D.Double(150, 50,SIZE, SIZE)  };
  Rectangle2D s = new Rectangle2D.Double();

  ShapeResizeHandler ada = new ShapeResizeHandler();

  public finalcode() {
    addMouseListener(ada);
    addMouseMotionListener(ada);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    for (int i = 0; i < points.length; i++) {
      g2.fill(points[i]);
    }
    s.setFrame(points[0].getCenterX(), points[0].getCenterY(),
        Math.abs(points[1].getCenterX()-points[0].getCenterX()),
        Math.abs(points[1].getCenterY()- points[0].getCenterY()));
    g2.draw(s);
  }

  class ShapeResizeHandler extends MouseAdapter {

    private Point2D[] lastPoints = new Point2D[5];
    private int pos = -1;
    public void mousePressed(MouseEvent event) {
      Point p = event.getPoint();

      for (int i = 0; i < points.length; i++) {
        if (points[i].contains(p)) {
          pos = i;
          // initialize preDrag points
          for(int j = 0; j < 5; j++){
              lastPoints[j] = new Point2D.Double(points[j].getX(), points[j].getY());
          }
          return;
        }
      }
    }

    public void mouseReleased(MouseEvent event) {
      pos = -1;
    }

    public void mouseDragged(MouseEvent event) {
      if (pos == -1)
        return;
      if(pos != 2){ //if 2, it's a shape drag
          if(pos == 0) {
    	  points[0].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
          points[3].setRect(event.getPoint().x,points[1].getY(),points[pos].getWidth(),points[pos].getHeight());
          points[4].setRect(points[1].getX(),event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
          }
          else if(pos == 1) {
        	  points[1].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
              points[3].setRect(event.getPoint().x,points[0].getY(),points[pos].getWidth(),points[pos].getHeight());
              points[4].setRect(points[0].getX(),event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
          }
          else if(pos == 3) {
        	  points[3].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
              points[0].setRect(event.getPoint().x,points[4].getY(),points[pos].getWidth(),points[pos].getHeight());
              points[1].setRect(points[4].getX(),event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
          }
          else if(pos == 4) {
        	  points[4].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
              points[1].setRect(event.getPoint().x,points[3].getY(),points[pos].getWidth(),points[pos].getHeight());
              points[0].setRect(points[3].getX(),event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
          }
          //int otherEnd = (pos==1)?0:1; //Get the end other than what is being dragged (top-left or bottom-right)
          //Get the x,y of the centre of the line joining the 2 new diagonal vertices, which will be new points[2]
          int otherEnd = pos;
          
          if(pos ==0) 
        	  otherEnd = 1; 
          else if(pos ==1) 
        	  otherEnd = 0;
          else if(pos ==3) 
        	  otherEnd = 4;
          else if(pos ==4)
        	  otherEnd = 3;
          
          double newPoint2X = points[otherEnd].getX() + (points[pos].getX() - points[otherEnd].getX())/2;
          double newPoint2Y = points[otherEnd].getY() + (points[pos].getY() - points[otherEnd].getY())/2;
          points[2].setRect(newPoint2X, newPoint2Y, points[2].getWidth(), points[2].getHeight());
      }
      else{ //Shape drag, 1,2,3 points/marker rects need to move equal amounts
          Double deltaX = event.getPoint().x - lastPoints[2].getX();
          Double deltaY = event.getPoint().y - lastPoints[2].getY();
          for(int j = 0; j < 5; j++)
              points[j].setRect((lastPoints[j].getX() + deltaX),(lastPoints[j].getY() + deltaY),points[j].getWidth(),
                  points[j].getHeight());

      }
      repaint();
    }
  }

  public static void main(String[] args) {

    JFrame frame = new JFrame("각 꼭지점에 수정점을 가진 사각형 그리기");

    frame.add(new finalcode());
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}