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

public class Exam extends JPanel {
	//수정점 사이즈 
	private int SIZE = 8;
  //수정점 좌표 및 만들기 
  private Rectangle2D[] points = { new Rectangle2D.Double(50, 50,SIZE, SIZE), 
                                   new Rectangle2D.Double(150, 50,SIZE, SIZE),
                                   new Rectangle2D.Double(50, 150,SIZE, SIZE),
                                   new Rectangle2D.Double(150, 150,SIZE, SIZE),
                                   new Rectangle2D.Double(100, 100,SIZE, SIZE)
                                   };
  Rectangle2D s = new Rectangle2D.Double();

  ShapeResizeHandler ada = new ShapeResizeHandler();

  public Exam() {
    addMouseListener(ada);
    addMouseMotionListener(ada);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;
  //수정점 그리기 
    for (int i = 0; i < points.length; i++) {
      g2.fill(points[i]);
    }
  //사각형 그리기  x,y,w,h 받아서 
    s.setFrame(points[0].getCenterX(), points[0].getCenterY(),
        Math.abs(points[3].getCenterX()-points[0].getCenterX()),
        Math.abs(points[3].getCenterY()- points[0].getCenterY()));
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
      
      if(pos != 4){ 
    	  // 수정 할 수 있는 수정점 기능 
         points[pos].setRect(event.getPoint().x,event.getPoint().y,points[pos].getWidth(),points[pos].getHeight());
    	  
          // 반대 기준 점 변수 
          int otherEnd = (pos==0)?3:(pos==3)?0:(pos==1)?2:1;
          
          //Get the end other than what is being dragged (top-left or bottom-right)
          //Get the x,y of the center of the line joining the 2 new diagonal vertices, which will be new points[2]
          // 중앙점 위치 
         double newPoint2X = points[otherEnd].getX() + (points[pos].getX() - points[otherEnd].getX())/2;
         double newPoint2Y = points[otherEnd].getY() + (points[pos].getY() - points[otherEnd].getY())/2;
 
         // 수정 기준 점 
         points[4].setRect(newPoint2X, newPoint2Y, points[4].getWidth(), points[4].getHeight());
         
      }
      else{ //4번 이동 기능 가진 수정점 이동시 수정점을 다 가지고 이동하게끔 진행한다.
          Double deltaX = event.getPoint().x - lastPoints[4].getX();
          Double deltaY = event.getPoint().y - lastPoints[4].getY();
          
         for(int j = 0; j < 5; j++)
            points[j].setRect((lastPoints[j].getX() + deltaX),(lastPoints[j].getY() + deltaY),points[j].getWidth(),
                 points[j].getHeight());

      }
      repaint();
    }
  }

  public static void main(String[] args) {

    JFrame frame = new JFrame("Resize Shape2D");

    frame.add(new Exam());
    frame.setSize(500, 500);
    frame.setLocationRelativeTo(null);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
}