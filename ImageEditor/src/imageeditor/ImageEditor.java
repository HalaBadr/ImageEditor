package imageeditor;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import imageeditor.LoadImage;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Circle;
import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageEditor extends JFrame {

    static public BufferedImage image = null;
    int yp = 0;
    Stack<BufferedImage> Undo = new Stack();
    Stack<BufferedImage> Redo = new Stack();
    String path = null;
    int Rectangle = 1;
    Rectangle Rect;
    Area circle;
    JFrame f = new JFrame();
    JLabel screenLabel;
    JButton Choose, select, light, dark, blure, inverte, save, undo, redo;
    JScrollPane screenScroll;
    BufferedImage screenCopy;

    ImageEditor() {
        Choose = new JButton("Choose Image");
        select = new JButton("Rectangle");
        light = new JButton("Light");
        dark = new JButton("Dark");
        blure = new JButton("Blure");
        inverte = new JButton("Inverte");
        save = new JButton("Save");
        undo = new JButton("Undo");
        redo = new JButton("Redo");

        Choose.setBounds(50, 600, 150, 40);
        select.setBounds(250, 600, 100, 40);
        light.setBounds(400, 600, 100, 40);
        dark.setBounds(550, 600, 100, 40);
        blure.setBounds(700, 600, 100, 40);
        inverte.setBounds(850, 600, 100, 40);
        save.setBounds(1000, 600, 100, 40);
        undo.setBounds(1140, 600, 100, 40);
        redo.setBounds(1260, 600, 100, 40);

        f.add(Choose);
        f.add(select);
        f.add(light);
        f.add(dark);
        f.add(blure);
        f.add(inverte);
        f.add(save);
        f.add(undo);
        f.add(redo);

        //Select listener
        select.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                if (Rectangle == 1) {
                    Rectangle = 0;
                    select.setText("Circle");
                } else {
                    Rectangle = 1;
                    select.setText("Rectangle");
                }
            }
        });

        //blure listener
        blure.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Rect != null || circle != null) {
                    float data[] = {0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
                        0.1625f, 0.125f, 0.1625f};
                    Kernel kernel = new Kernel(3, 3, data);
                    ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
                            null);
                    if (Rectangle == 1) {
                        convolve.filter(image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height),
                                image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height));
                    } else {
                        convolve.filter(image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()
                        ), image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()));
                    }
                    Rect = null;
                    circle = null;
                    repaint(image, screenCopy);
                    screenLabel.repaint();
                    Undo.add(Copy(image));
                }
            }
        });

        //Light listener
        light.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Rect != null || circle != null) {
                    float scaleFactor = 5;
                    RescaleOp op = new RescaleOp(scaleFactor, 0, null);
                    if (Rectangle == 1) {
                        op.filter(image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height),
                                image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height));
                    } else {
                        op.filter(image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()
                        ), image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()));
                    }
                    Rect = null;
                    circle = null;
                    repaint(image, screenCopy);
                    screenLabel.repaint();
                    Undo.add(Copy(image));
                }
            }
        });

        //Dark listener
        dark.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Rect != null || circle != null) {
                    float scaleFactor = 0.5f;
                    RescaleOp op = new RescaleOp(scaleFactor, 0, null);
                    if (Rectangle == 1) {
                        op.filter(image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height),
                                image.getSubimage(Rect.x, Rect.y, Rect.width, Rect.height));
                    } else {
                        op.filter(image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()
                        ), image.getSubimage(
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getHeight()));
                    }
                    Rect = null;
                    circle = null;
                    repaint(image, screenCopy);
                    screenLabel.repaint();
                    Undo.add(Copy(image));
                }
            }
        });

        //Invert listener
        inverte.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Rect != null || circle != null) {
                    Graphics g = image.createGraphics();
                    if (Rectangle == 1) {
                        g.drawImage(image,
                                Rect.x, Rect.y,
                                Rect.x + Rect.width, Rect.y + Rect.height,
                                Rect.x + Rect.width, Rect.y,
                                Rect.x, Rect.y + Rect.height,
                                null);

                    } else {
                        g.drawImage(image,
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getX() + (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getY() + (int) circle.getBounds2D().getHeight(),
                                (int) circle.getBounds2D().getX() + (int) circle.getBounds2D().getWidth(),
                                (int) circle.getBounds2D().getY(),
                                (int) circle.getBounds2D().getX(),
                                (int) circle.getBounds2D().getY() + (int) circle.getBounds2D().getHeight(),
                                null);
                    }
                    Rect = null;
                    circle = null;
                    repaint(image, screenCopy);
                    screenLabel.repaint();

                    Undo.add(Copy(image));
                }
            }
        });

        //Save listener
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (image != null) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter filter = new FileNameExtensionFilter(
                            "JPG, GIF, and PNG Images", "jpg", "gif", "png");
                    chooser.setFileFilter(filter);
                    int returnVal = chooser.showOpenDialog(null);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        System.out.println("You chose to open this file: "
                                + file.getName());

                        try {
                            ImageIO.write(image, "jpg", new File(file.getAbsolutePath()));
                        } catch (IOException ex) {
                            Logger.getLogger(ImageEditor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }

            }
        });

        //Undo listener
        undo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(Undo.size());
                if (Undo.size() != 0 && Undo.size() > 1) {

                    Redo.add(Copy(Undo.pop()));
                    System.out.println(image.equals(Undo.peek()));
                    image = Copy(Undo.peek());

                    repaint(image, screenCopy);
                    screenLabel.repaint();
                }
                /* else if(Undo.size()==1){
             Redo.add(Copy(Undo.pop()));
             image = null;
             //repaint(image, null);
             screenScroll.repaint();
             f.repaint();
             f.setVisible(true);
             //screenLabel.repaint();
            
        }*/

            }
        });

        //Redo listener
        redo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (Redo.size() != 0) {
                    Undo.add(Copy(Redo.pop()));
                    image = Copy(Undo.peek());
                    repaint(image, screenCopy);
                    screenLabel.repaint();
                    /*if(Undo.size()==1){
                 screenCopy = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                image.getType());
                
        screenLabel = new JLabel(new ImageIcon(screenCopy));
                 screenScroll = new JScrollPane(screenLabel);
                 screenScroll.setBounds(300,50,400,400);
                 repaint(image, screenCopy);
                 screenLabel.repaint();
                 f.add(screenScroll);
                 f.setVisible(true);}*/
                }
                /*else{
            repaint(image, screenCopy);
        screenLabel.repaint();}*/

            }
        });

        Choose.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                LoadImage loadImage = new LoadImage();
                path = loadImage.getPath();
                if (path != null) {
                    image = null;
                    int imageWidth = -1;
                    int imageHeight = -1;
                    int fileSize = -1;
                    try {
                        File imageFile = new File(path);
                        fileSize = (int) imageFile.length();
                        image = ImageIO.read(imageFile);
                        imageWidth = image.getWidth();
                        imageHeight = image.getHeight();
                        //////////////
                        screenCopy = new BufferedImage(
                                image.getWidth(),
                                image.getHeight(),
                                image.getType());
                        Undo.add(Copy(image));
                        screenLabel = new JLabel(new ImageIcon(screenCopy));
                        screenScroll = new JScrollPane(screenLabel);
                        screenScroll.setBounds(300, 50, 400, 400);
                        repaint(image, screenCopy);
                        screenLabel.repaint();
                        screenScroll.repaint();

                        f.add(screenScroll);

                        f.setVisible(true);
                        screenLabel.addMouseMotionListener(new MouseMotionAdapter() {

                            Point start = new Point();

                            @Override
                            public void mouseMoved(MouseEvent me) {

                                start = me.getPoint();
                            }

                            @Override
                            public void mouseDragged(MouseEvent me) {
                                Point end = me.getPoint();
                                Rect = new Rectangle(start,
                                        new Dimension(end.x - start.x, end.y - start.y));
                                Ellipse2D.Double ellipse1 = new Ellipse2D.Double(
                                        start.x, start.y, end.x - start.x, end.y - start.y);

                                circle = new Area(ellipse1);
                                repaint(image, screenCopy);
                                screenLabel.repaint();
                                //selectionLabel.setText("Rectangle: " + captureRect);
                            }

                        });

                        //JOptionPane.showMessageDialog(null, panel);
                        System.out.println("Rectangle of interest: " + Rect);

                        ///////////////
                    } catch (IOException erorr) {
                        erorr.printStackTrace();
                    }
                }
            }

        });
        //JOptionPane.showMessageDialog(null, panel);  

        f.setLayout(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        //f.setSize(700,400);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
        f.setVisible(true);

    }

    public BufferedImage Copy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public void repaint(BufferedImage orig, BufferedImage copy) {
        Graphics2D g = copy.createGraphics();
        g.drawImage(orig, 0, 0, null);
        if (Rect != null || circle != null) {
            if (Rectangle == 1) {
                g.setColor(Color.BLACK);
                g.draw(Rect);
                g.setColor(new Color(10, 10, 10, 150));
                g.fill(Rect);
            } else {
                g.setColor(Color.BLACK);
                g.draw(circle);
                g.setColor(new Color(10, 10, 10, 150));
                g.fill(circle);
            }
        }
        g.dispose();
        //Undo.add(image);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageEditor();

            }
        });
        /*Robot robot = new Robot();
        final Dimension screenSize = Toolkit.getDefaultToolkit().
                getScreenSize();
        final BufferedImage screen = robot.createScreenCapture(
                new Rectangle(screenSize));

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ImageEditor(screen);
            }
        });*/
    }
}
